package com.vitorsousa.stallfit.data.repository

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.Energy
import com.vitorsousa.stallfit.data.local.entity.WorkoutSessionEntity
import java.time.Instant
import kotlin.math.roundToLong

/** Whether Health Connect can be written to right now, or the user needs to install/update it first via Play Store. */
enum class HealthConnectAvailability { NOT_INSTALLED, AVAILABLE }

/**
 * Write-only bridge to Health Connect for a finished [WorkoutSessionEntity]. Always writes an
 * [ExerciseSessionRecord] (start/end/title); when a paired watch captured sensor data during the
 * session (Fase 3's [com.vitorsousa.stallfit.data.wear.WearFinishSessionRequest]), also writes a
 * [HeartRateRecord] and a [TotalCaloriesBurnedRecord].
 */
class HealthConnectRepository(private val context: Context) {

    private val client: HealthConnectClient by lazy { HealthConnectClient.getOrCreate(context) }

    val requiredPermissions: Set<String> = setOf(
        HealthPermission.getWritePermission(ExerciseSessionRecord::class),
        HealthPermission.getWritePermission(HeartRateRecord::class),
        HealthPermission.getWritePermission(TotalCaloriesBurnedRecord::class)
    )

    fun availability(): HealthConnectAvailability =
        if (HealthConnectClient.getSdkStatus(context) == HealthConnectClient.SDK_AVAILABLE) {
            HealthConnectAvailability.AVAILABLE
        } else {
            HealthConnectAvailability.NOT_INSTALLED
        }

    fun requestPermissionsContract() = PermissionController.createRequestPermissionResultContract()

    suspend fun hasPermissions(): Boolean =
        client.permissionController.getGrantedPermissions().containsAll(requiredPermissions)

    suspend fun writeWorkoutSession(session: WorkoutSessionEntity): Result<Unit> = runCatching {
        val finishedAt = checkNotNull(session.finishedAt) { "Only a finished session can be synced" }
        val startTime = Instant.ofEpochMilli(session.startedAt)
        val endTime = Instant.ofEpochMilli(finishedAt)

        val records = mutableListOf<Record>(
            ExerciseSessionRecord(
                startTime = startTime,
                startZoneOffset = null,
                endTime = endTime,
                endZoneOffset = null,
                // The workout itself is logged manually by the user, not captured by a sensor.
                metadata = Metadata.manualEntry(),
                exerciseType = ExerciseSessionRecord.EXERCISE_TYPE_STRENGTH_TRAINING,
                title = session.name
            )
        )

        // Isolated in their own runCatching, separate from the ExerciseSessionRecord above: these
        // constructors validate their arguments (bpm range, non-negative energy), and a corrupt or
        // edge-case sensor value must never prevent the exercise record from syncing.
        session.avgHeartRateBpm?.let { avgHeartRateBpm ->
            runCatching {
                HeartRateRecord(
                    startTime = startTime,
                    startZoneOffset = null,
                    endTime = endTime,
                    endZoneOffset = null,
                    // Health Services only reports a session average, not a real sample series, so
                    // the average is repeated at both ends of the interval as the closest honest fit.
                    samples = listOf(
                        HeartRateRecord.Sample(time = startTime, beatsPerMinute = avgHeartRateBpm.roundToLong()),
                        HeartRateRecord.Sample(time = endTime, beatsPerMinute = avgHeartRateBpm.roundToLong())
                    ),
                    metadata = Metadata.manualEntry()
                )
            }.onSuccess { records.add(it) }
        }
        session.totalCalories?.let { totalCalories ->
            runCatching {
                TotalCaloriesBurnedRecord(
                    startTime = startTime,
                    startZoneOffset = null,
                    endTime = endTime,
                    endZoneOffset = null,
                    energy = Energy.kilocalories(totalCalories),
                    metadata = Metadata.manualEntry()
                )
            }.onSuccess { records.add(it) }
        }

        client.insertRecords(records)
        Unit
    }
}

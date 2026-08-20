package com.vitorsousa.stallfit.wear.sensors

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import androidx.health.services.client.ExerciseUpdateCallback
import androidx.health.services.client.HealthServices
import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.ExerciseConfig
import androidx.health.services.client.data.ExerciseLapSummary
import androidx.health.services.client.data.ExerciseType
import androidx.health.services.client.data.ExerciseUpdate
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.vitorsousa.stallfit.wear.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Owns the single [androidx.health.services.client.ExerciseClient] registration for the
 * lifetime of a workout, so heart-rate/calorie capture survives Activity recreation and screen-off
 * (a foreground service is required by Health Services for sessions longer than a couple minutes).
 *
 * Best-effort by design: any failure (unsupported device, denied permission, client exception)
 * stops the service quietly and leaves [metrics] at `null` — callers must never depend on this
 * service succeeding for the existing start/log-set/finish workout flow to keep working.
 */
class ExerciseTrackingService : LifecycleService() {

    private val exerciseClient by lazy { HealthServices.getClient(this).exerciseClient }

    private val _metrics = MutableStateFlow<ExerciseMetrics?>(null)
    val metrics: StateFlow<ExerciseMetrics?> = _metrics.asStateFlow()

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): ExerciseTrackingService = this@ExerciseTrackingService
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        // Must happen synchronously, before the async capability check below, or the OS kills the
        // process for missing the ~5s startForeground() window (ForegroundServiceDidNotStartInTimeException).
        ServiceCompat.startForeground(
            this,
            NOTIFICATION_ID,
            buildNotification(),
            ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH
        )
        lifecycleScope.launch { beginTracking() }
        return START_NOT_STICKY
    }

    private suspend fun beginTracking() {
        try {
            val permissionGranted = ContextCompat.checkSelfPermission(this, heartRateSensorPermission()) ==
                PackageManager.PERMISSION_GRANTED
            if (!permissionGranted) {
                stopSelfGracefully()
                return
            }

            val capabilities = exerciseClient.getCapabilitiesAsync().await()
            if (ExerciseType.STRENGTH_TRAINING !in capabilities.supportedExerciseTypes) {
                stopSelfGracefully()
                return
            }
            val typeCapabilities = capabilities.getExerciseTypeCapabilities(ExerciseType.STRENGTH_TRAINING)
            val supportedDataTypes = setOf(DataType.HEART_RATE_BPM, DataType.HEART_RATE_BPM_STATS, DataType.CALORIES_TOTAL)
                .intersect(typeCapabilities.supportedDataTypes)
            if (supportedDataTypes.isEmpty()) {
                stopSelfGracefully()
                return
            }

            exerciseUpdateFlow()
                .onEach { update -> _metrics.value = update.toMetrics() }
                .launchIn(lifecycleScope)

            exerciseClient.startExerciseAsync(
                ExerciseConfig(
                    exerciseType = ExerciseType.STRENGTH_TRAINING,
                    dataTypes = supportedDataTypes,
                    isAutoPauseAndResumeEnabled = false,
                    isGpsEnabled = false
                )
            ).await()
        } catch (e: Exception) {
            Log.w(TAG, "Sensor tracking unavailable, workout flow proceeds unaffected", e)
            stopSelfGracefully()
        }
    }

    private fun exerciseUpdateFlow(): Flow<ExerciseUpdate> = callbackFlow {
        val callback = object : ExerciseUpdateCallback {
            override fun onExerciseUpdateReceived(update: ExerciseUpdate) {
                trySend(update)
            }

            override fun onLapSummaryReceived(lapSummary: ExerciseLapSummary) = Unit
            override fun onAvailabilityChanged(dataType: DataType<*, *>, availability: Availability) = Unit
            override fun onRegistered() = Unit
            override fun onRegistrationFailed(throwable: Throwable) {
                close(throwable)
            }
        }
        exerciseClient.setUpdateCallback(callback)
        awaitClose { exerciseClient.clearUpdateCallbackAsync(callback) }
    }

    private fun ExerciseUpdate.toMetrics(): ExerciseMetrics {
        val liveHeartRate = latestMetrics.getData(DataType.HEART_RATE_BPM).lastOrNull()?.value?.roundToInt()
        val avgHeartRate = latestMetrics.getData(DataType.HEART_RATE_BPM_STATS)?.average
        val totalCalories = latestMetrics.getData(DataType.CALORIES_TOTAL)?.total
        return ExerciseMetrics(
            liveHeartRateBpm = liveHeartRate ?: _metrics.value?.liveHeartRateBpm,
            avgHeartRateBpm = avgHeartRate ?: _metrics.value?.avgHeartRateBpm,
            totalCalories = totalCalories ?: _metrics.value?.totalCalories
        )
    }

    /**
     * Idempotent — safe to call more than once (e.g. a phone-side timeout causing a retry).
     * Reads [metrics] *before* awaiting [androidx.health.services.client.ExerciseClient.endExerciseAsync]:
     * the average/total are already cumulative on every update, so the last received value is an
     * acceptable approximation of the final summary, and waiting for one more update after ending
     * the exercise isn't guaranteed to ever arrive.
     */
    suspend fun stopTrackingAndSnapshot(): ExerciseMetrics? {
        val snapshot = _metrics.value
        runCatching { exerciseClient.endExerciseAsync().await() }
        stopSelfGracefully()
        return snapshot
    }

    private fun stopSelfGracefully() {
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createNotificationChannel() {
        val manager = getSystemService(NotificationManager::class.java) ?: return
        val channel = NotificationChannel(CHANNEL_ID, "Monitoramento de treino", NotificationManager.IMPORTANCE_LOW)
        manager.createNotificationChannel(channel)
    }

    private fun buildNotification(): Notification =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Treino em andamento")
            .setContentText("Monitorando frequência cardíaca e calorias")
            .setSmallIcon(R.drawable.ic_notification_pulse)
            .setOngoing(true)
            .build()

    companion object {
        private const val TAG = "ExerciseTrackingService"
        private const val CHANNEL_ID = "exercise_tracking"
        private const val NOTIFICATION_ID = 1001
    }
}

package com.vitorsousa.stallfit.wear.sensors

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import androidx.annotation.RequiresPermission
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
import androidx.wear.ongoing.OngoingActivity
import androidx.wear.ongoing.Status
import com.vitorsousa.stallfit.wear.MainActivity
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

    /**
     * On any failure here, sensor capture is simply skipped — the foreground service (and its
     * Ongoing Activity indicator) stays up regardless, since neither depends on sensor data and
     * their lifetime is owned by [stopTrackingAndSnapshot], driven by the phone-side finish-workout
     * action, not by whether tracking itself succeeded.
     */
    private suspend fun beginTracking() {
        try {
            val permissionGranted = ContextCompat.checkSelfPermission(this, heartRateSensorPermission()) ==
                PackageManager.PERMISSION_GRANTED
            if (!permissionGranted) {
                return
            }

            val capabilities = exerciseClient.getCapabilitiesAsync().await()
            if (ExerciseType.STRENGTH_TRAINING !in capabilities.supportedExerciseTypes) {
                return
            }
            val typeCapabilities = capabilities.getExerciseTypeCapabilities(ExerciseType.STRENGTH_TRAINING)
            val supportedDataTypes = setOf(DataType.HEART_RATE_BPM, DataType.HEART_RATE_BPM_STATS, DataType.CALORIES_TOTAL)
                .intersect(typeCapabilities.supportedDataTypes)
            if (supportedDataTypes.isEmpty()) {
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

    private fun buildNotification(): Notification {
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Treino em andamento")
            .setContentText("Monitorando frequência cardíaca e calorias")
            .setSmallIcon(R.drawable.ic_notification_pulse)
            .setCategory(NotificationCompat.CATEGORY_WORKOUT)
            .setOngoing(true)

        // Guard is required for Lint to clear the @RequiresPermission on attachOngoingActivity() —
        // a runCatching alone isn't recognized as a permission check idiom. Best-effort: any
        // failure here must never affect the notification returned below.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
        ) {
            runCatching { attachOngoingActivity(notificationBuilder) }
                .onFailure { e -> Log.w(TAG, "Ongoing Activity indicator unavailable, notification proceeds unaffected", e) }
        }

        return notificationBuilder.build()
    }

    /**
     * Wear OS Ongoing Activity indicator: watch-face icon + elapsed-time stopwatch that, on tap,
     * brings the existing [MainActivity] instance forward (FLAG_ACTIVITY_SINGLE_TOP) instead of
     * recreating it, so [com.vitorsousa.stallfit.wear.ui.WearScreen.ActiveSession] state survives.
     */
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun attachOngoingActivity(notificationBuilder: NotificationCompat.Builder) {
        val touchIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        notificationBuilder.setContentIntent(touchIntent)

        val status = Status.Builder()
            .addTemplate("#time#")
            .addPart("time", Status.StopwatchPart(SystemClock.elapsedRealtime()))
            .build()

        OngoingActivity.Builder(applicationContext, NOTIFICATION_ID, notificationBuilder)
            .setStaticIcon(R.drawable.ic_notification_pulse)
            .setTouchIntent(touchIntent)
            .setStatus(status)
            .build()
            .apply(applicationContext)
    }

    companion object {
        private const val TAG = "ExerciseTrackingService"
        private const val CHANNEL_ID = "exercise_tracking"
        private const val NOTIFICATION_ID = 1001
    }
}

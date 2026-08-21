package com.vitorsousa.stallfit.wear.complication

import android.content.ComponentName
import android.content.Context
import androidx.core.content.edit
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceUpdateRequester
import java.time.Duration
import java.time.Instant

/**
 * Cross-process-safe snapshot of "is a workout currently being tracked", backed by
 * SharedPreferences rather than the in-memory StateFlows on ExerciseTrackingService /
 * ExerciseTrackingConnector — WorkoutComplicationDataSourceService can be instantiated by the
 * Wear OS system (e.g. after a reboot, or the app process being killed) without either of
 * those objects existing. Kept synchronous and limited to a handful of primitives so it's
 * cheap to read on every onComplicationRequest.
 */
class WorkoutComplicationState(private val context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    data class Snapshot(val isTracking: Boolean, val startInstant: Instant?, val lastHeartRateBpm: Int?)

    /** Call once the foreground service commits to running — mirrors the Ongoing Activity's own lifecycle. */
    fun markTrackingStarted() {
        prefs.edit {
            putBoolean(KEY_IS_TRACKING, true)
            putLong(KEY_START_EPOCH_MILLIS, Instant.now().toEpochMilli())
            remove(KEY_LAST_HEART_RATE_BPM)
        }
    }

    fun markTrackingStopped() {
        prefs.edit { putBoolean(KEY_IS_TRACKING, false) }
    }

    /** Best-effort cache for the next complication read; deliberately does not itself push an update. */
    fun updateHeartRate(bpm: Int?) {
        val current = prefs.getInt(KEY_LAST_HEART_RATE_BPM, -1).takeIf { it >= 0 }
        if (bpm == current) return
        prefs.edit {
            if (bpm == null) remove(KEY_LAST_HEART_RATE_BPM) else putInt(KEY_LAST_HEART_RATE_BPM, bpm)
        }
    }

    fun read(): Snapshot {
        val isTracking = prefs.getBoolean(KEY_IS_TRACKING, false)
        val startInstant = prefs.getLong(KEY_START_EPOCH_MILLIS, -1L).takeIf { it >= 0 }?.let(Instant::ofEpochMilli)
        val bpm = prefs.getInt(KEY_LAST_HEART_RATE_BPM, -1).takeIf { it >= 0 }
        // Defensive: a service killed without ever calling markTrackingStopped() (OOM, force-stop, crash)
        // must not leave the complication permanently stuck on an ever-growing stopwatch.
        val stillPlausible = startInstant != null && Duration.between(startInstant, Instant.now()) < MAX_PLAUSIBLE_DURATION
        return if (isTracking && stillPlausible) {
            Snapshot(isTracking = true, startInstant = startInstant, lastHeartRateBpm = bpm)
        } else {
            Snapshot(isTracking = false, startInstant = null, lastHeartRateBpm = null)
        }
    }

    companion object {
        private const val PREFS_NAME = "workout_complication_state"
        private const val KEY_IS_TRACKING = "is_tracking"
        private const val KEY_START_EPOCH_MILLIS = "start_epoch_millis"
        private const val KEY_LAST_HEART_RATE_BPM = "last_heart_rate_bpm"
        private val MAX_PLAUSIBLE_DURATION: Duration = Duration.ofHours(6)

        /** Fire-and-forget: asks the system to re-invoke WorkoutComplicationDataSourceService.onComplicationRequest. */
        fun requestComplicationUpdate(context: Context) {
            runCatching {
                ComplicationDataSourceUpdateRequester.create(
                    context.applicationContext,
                    ComponentName(context, WorkoutComplicationDataSourceService::class.java)
                ).requestUpdateAll()
            }
        }
    }
}

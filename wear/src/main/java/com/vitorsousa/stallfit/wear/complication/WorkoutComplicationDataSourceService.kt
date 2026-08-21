package com.vitorsousa.stallfit.wear.complication

import android.app.PendingIntent
import android.content.Intent
import android.graphics.drawable.Icon
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.CountUpTimeReference
import androidx.wear.watchface.complications.data.MonochromaticImage
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.data.TimeDifferenceComplicationText
import androidx.wear.watchface.complications.data.TimeDifferenceStyle
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import com.vitorsousa.stallfit.wear.MainActivity
import com.vitorsousa.stallfit.wear.R
import java.time.Instant

/**
 * Watch-face complication showing whether a StällFit workout is currently being tracked.
 * Instantiated directly by the Wear OS system — must never assume ExerciseTrackingService is
 * alive; all state comes from WorkoutComplicationState.
 */
class WorkoutComplicationDataSourceService : SuspendingComplicationDataSourceService() {

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData? {
        if (request.complicationType != ComplicationType.SHORT_TEXT) return null
        return buildComplicationData(WorkoutComplicationState(this).read())
    }

    override fun getPreviewData(type: ComplicationType): ComplicationData? {
        if (type != ComplicationType.SHORT_TEXT) return null
        return buildComplicationData(
            WorkoutComplicationState.Snapshot(
                isTracking = true,
                startInstant = Instant.now().minusSeconds(754),
                lastHeartRateBpm = 128
            )
        )
    }

    private fun buildComplicationData(snapshot: WorkoutComplicationState.Snapshot): ComplicationData {
        val icon = MonochromaticImage.Builder(Icon.createWithResource(this, R.drawable.ic_notification_pulse)).build()
        val tapAction = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val text = if (snapshot.isTracking && snapshot.startInstant != null) {
            TimeDifferenceComplicationText.Builder(TimeDifferenceStyle.STOPWATCH, CountUpTimeReference(snapshot.startInstant))
                .build()
        } else {
            PlainComplicationText.Builder(getString(R.string.workout_complication_idle_text)).build()
        }

        val contentDescription = PlainComplicationText.Builder(
            getString(
                if (snapshot.isTracking) R.string.workout_complication_active_description
                else R.string.workout_complication_idle_description
            )
        ).build()

        return ShortTextComplicationData.Builder(text = text, contentDescription = contentDescription)
            .setMonochromaticImage(icon)
            .setTapAction(tapAction)
            .apply {
                if (snapshot.isTracking) {
                    setTitle(PlainComplicationText.Builder(snapshot.lastHeartRateBpm?.toString() ?: "--").build())
                }
            }
            .build()
    }
}

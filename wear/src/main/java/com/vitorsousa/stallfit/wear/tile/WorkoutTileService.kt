package com.vitorsousa.stallfit.wear.tile

import android.content.ComponentName
import android.content.Context
import androidx.wear.protolayout.ActionBuilders
import androidx.wear.protolayout.TimelineBuilders.Timeline
import androidx.wear.protolayout.material3.MaterialScope
import androidx.wear.protolayout.material3.primaryLayout
import androidx.wear.protolayout.material3.text
import androidx.wear.protolayout.modifiers.LayoutModifier
import androidx.wear.protolayout.modifiers.clickable
import androidx.wear.protolayout.modifiers.contentDescription
import androidx.wear.protolayout.types.layoutString
import androidx.wear.tiles.Material3TileService
import androidx.wear.tiles.RequestBuilders.TileRequest
import androidx.wear.tiles.TileBuilders.Tile
import androidx.wear.tiles.TileService
import androidx.wear.tiles.tile
import com.vitorsousa.stallfit.wear.MainActivity
import com.vitorsousa.stallfit.wear.R
import com.vitorsousa.stallfit.wear.complication.WorkoutComplicationState
import java.time.Duration as JavaDuration
import java.time.Instant
import kotlin.time.Duration.Companion.seconds

/**
 * Tile showing whether a StällFit workout is currently being tracked — reuses the same
 * cross-process-safe [WorkoutComplicationState] the watch-face complication already reads from.
 * Unlike the complication, the tile has no self-updating stopwatch text (Tiles have no
 * TimeDifferenceComplicationText equivalent), so elapsed time is a snapshot recomputed on every
 * tileResponse, refreshed periodically via the tile's freshness interval while tracking is active.
 */
class WorkoutTileService : Material3TileService() {

    override suspend fun MaterialScope.tileResponse(requestParams: TileRequest): Tile {
        val snapshot = WorkoutComplicationState(context).read()

        val titleText = if (snapshot.isTracking && snapshot.startInstant != null) {
            formatElapsed(JavaDuration.between(snapshot.startInstant, Instant.now()))
        } else {
            context.getString(R.string.workout_complication_idle_text)
        }
        val mainText = if (snapshot.isTracking) {
            snapshot.lastHeartRateBpm?.let { "$it bpm" } ?: "-- bpm"
        } else {
            context.getString(R.string.workout_complication_idle_description)
        }
        val descriptionRes = if (snapshot.isTracking) {
            R.string.workout_complication_active_description
        } else {
            R.string.workout_complication_idle_description
        }

        val layout = primaryLayout(
            titleSlot = { text(titleText.layoutString) },
            mainSlot = {
                text(
                    text = mainText.layoutString,
                    modifier = LayoutModifier.contentDescription(context.getString(descriptionRes)),
                )
            },
            onClick = clickable(
                action = ActionBuilders.launchAction(ComponentName(context, MainActivity::class.java))
            ),
        )

        return tile(
            timeline = Timeline.fromLayoutElement(layout),
            freshness = if (snapshot.isTracking) FRESHNESS_INTERVAL else null,
            resourcesVersion = RESOURCES_VERSION,
        )
    }

    private fun formatElapsed(duration: JavaDuration): String {
        val totalSeconds = duration.seconds.coerceAtLeast(0)
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return if (hours > 0) {
            "%d:%02d:%02d".format(hours, minutes, seconds)
        } else {
            "%d:%02d".format(minutes, seconds)
        }
    }

    companion object {
        private const val RESOURCES_VERSION = "1"
        private val FRESHNESS_INTERVAL = 30.seconds

        /** Fire-and-forget: asks the system to re-invoke WorkoutTileService.tileResponse. */
        fun requestTileUpdate(context: Context) {
            runCatching {
                TileService.getUpdater(context).requestUpdate(WorkoutTileService::class.java)
            }
        }
    }
}

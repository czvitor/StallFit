package com.vitorsousa.stallfit.wear.tile

import android.content.ComponentName
import android.content.Context
import androidx.wear.protolayout.ResourceBuilders.Resources
import androidx.wear.protolayout.TimelineBuilders.Timeline
import androidx.wear.protolayout.material3.*
import androidx.wear.tiles.RequestBuilders.ResourcesRequest
import androidx.wear.tiles.RequestBuilders.TileRequest
import androidx.wear.tiles.TileBuilders.Tile
import androidx.wear.tiles.TileService
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.vitorsousa.stallfit.wear.MainActivity
import com.vitorsousa.stallfit.wear.R
import com.vitorsousa.stallfit.wear.complication.WorkoutComplicationState
import java.time.Duration
import java.time.Instant

/**
 * Tile showing whether a StällFit workout is currently being tracked — reuses the same
 * cross-process-safe [WorkoutComplicationState] the watch-face complication already reads from.
 * Unlike the complication, the tile has no self-updating stopwatch text (Tiles have no
 * TimeDifferenceComplicationText equivalent), so elapsed time is a snapshot recomputed on every
 * onTileRequest, refreshed periodically via setFreshnessIntervalMillis while tracking is active.
 */
class WorkoutTileService : TileService() {

    override fun onTileRequest(requestParams: TileRequest): ListenableFuture<Tile> {
        val snapshot = WorkoutComplicationState(this).read()

        val layout = materialScope(this, requestParams.deviceConfiguration) {
            primaryLayout(
                titleSlot = {
                    text(
                        if (snapshot.isTracking && snapshot.startInstant != null) {
                            formatElapsed(Duration.between(snapshot.startInstant, Instant.now())).layoutString
                        } else {
                            getString(R.string.workout_complication_idle_text).layoutString
                        }
                    )
                },
                mainSlot = {
                    text(
                        if (snapshot.isTracking) {
                            (snapshot.lastHeartRateBpm?.let { "$it bpm" } ?: "-- bpm").layoutString
                        } else {
                            getString(R.string.workout_complication_idle_description).layoutString
                        }
                    )
                },
                bottomSlot = {
                    textEdgeButton(
                        onClick = clickable(action = launchAction(ComponentName(this@WorkoutTileService, MainActivity::class.java))),
                        labelContent = { text(getString(R.string.workout_tile_idle_action_label).layoutString) }
                    )
                }
            )
        }

        val tileBuilder = Tile.Builder()
            .setResourcesVersion(RESOURCES_VERSION)
            .setTileTimeline(Timeline.fromLayoutElement(layout))
        if (snapshot.isTracking) {
            tileBuilder.setFreshnessIntervalMillis(FRESHNESS_INTERVAL_MILLIS)
        }
        return Futures.immediateFuture(tileBuilder.build())
    }

    override fun onTileResourcesRequest(requestParams: ResourcesRequest): ListenableFuture<Resources> {
        return Futures.immediateFuture(Resources.Builder().setVersion(RESOURCES_VERSION).build())
    }

    private fun formatElapsed(duration: Duration): String {
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
        private const val FRESHNESS_INTERVAL_MILLIS = 30_000L

        /** Fire-and-forget: asks the system to re-invoke WorkoutTileService.onTileRequest. */
        fun requestTileUpdate(context: Context) {
            runCatching {
                TileService.getUpdater(context).requestUpdate(WorkoutTileService::class.java)
            }
        }
    }
}

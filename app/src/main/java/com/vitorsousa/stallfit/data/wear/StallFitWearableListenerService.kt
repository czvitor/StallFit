package com.vitorsousa.stallfit.data.wear

import android.util.Log
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import com.vitorsousa.stallfit.StallFitApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Receives watch requests (see [WearMessagePaths]) and replies on the matching path, delegating
 * the actual work to [com.vitorsousa.stallfit.data.repository.WearRepository]. A plain [android.app.Service]
 * has no `lifecycleScope`, so it owns its own [CoroutineScope] for the lifetime of the process.
 */
class StallFitWearableListenerService : WearableListenerService() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onMessageReceived(event: MessageEvent) {
        val wearRepository = (application as StallFitApp).container.wearRepository
        val payload = event.data.decodeToString()

        scope.launch {
            try {
                val (replyPath, replyBody) = when (event.path) {
                    WearMessagePaths.GET_CATALOG ->
                        WearMessagePaths.CATALOG to Json.encodeToString(wearRepository.buildCatalog())

                    WearMessagePaths.START_SESSION -> {
                        val request = Json.decodeFromString<WearStartSessionRequest>(payload)
                        WearMessagePaths.SESSION_STARTED to Json.encodeToString(wearRepository.startSession(request.templateId))
                    }

                    WearMessagePaths.LOG_SET -> {
                        val request = Json.decodeFromString<WearLogSetRequest>(payload)
                        WearMessagePaths.SET_LOGGED to Json.encodeToString(wearRepository.logSet(request))
                    }

                    WearMessagePaths.FINISH_SESSION -> {
                        val request = Json.decodeFromString<WearFinishSessionRequest>(payload)
                        wearRepository.finishSession(request)
                        WearMessagePaths.SESSION_FINISHED to ""
                    }

                    else -> return@launch
                }
                Wearable.getMessageClient(this@StallFitWearableListenerService)
                    .sendMessage(event.sourceNodeId, replyPath, replyBody.encodeToByteArray())
            } catch (e: Exception) {
                Log.w(TAG, "Failed to handle ${event.path} from watch", e)
            }
        }
    }

    companion object {
        private const val TAG = "StallFitWearableService"
    }
}

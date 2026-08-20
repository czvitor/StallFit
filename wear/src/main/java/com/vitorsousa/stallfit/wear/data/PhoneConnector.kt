package com.vitorsousa.stallfit.wear.data

import android.content.Context
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import java.util.concurrent.ConcurrentHashMap
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PhoneConnectionException(message: String) : Exception(message)

/**
 * Talks to the phone's `StallFitWearableListenerService` over the Wearable Data Layer API.
 * There is at most one request in flight per screen in this phase, so a single
 * pending-deferred-per-reply-path map is enough to correlate replies with requests.
 */
class PhoneConnector(context: Context) : MessageClient.OnMessageReceivedListener {

    private val appContext = context.applicationContext
    private val messageClient = Wearable.getMessageClient(appContext)
    private val nodeClient = Wearable.getNodeClient(appContext)
    private val pending = ConcurrentHashMap<String, CompletableDeferred<String>>()

    init {
        messageClient.addListener(this)
    }

    override fun onMessageReceived(event: MessageEvent) {
        pending.remove(event.path)?.complete(event.data.decodeToString())
    }

    suspend fun getCatalog(): WearCatalogDto =
        Json.decodeFromString(request(path = WearMessagePaths.GET_CATALOG, body = "", replyPath = WearMessagePaths.CATALOG))

    suspend fun startSession(templateId: Long?): WearSessionStartedDto {
        val body = Json.encodeToString(WearStartSessionRequest(templateId))
        return Json.decodeFromString(request(WearMessagePaths.START_SESSION, body, WearMessagePaths.SESSION_STARTED))
    }

    suspend fun logSet(sessionId: Long, exerciseId: Long, reps: Int, weightKg: Double): WearSetLoggedDto {
        val body = Json.encodeToString(WearLogSetRequest(sessionId, exerciseId, reps, weightKg))
        return Json.decodeFromString(request(WearMessagePaths.LOG_SET, body, WearMessagePaths.SET_LOGGED))
    }

    suspend fun finishSession(sessionId: Long, avgHeartRateBpm: Double? = null, totalCalories: Double? = null) {
        val body = Json.encodeToString(WearFinishSessionRequest(sessionId, avgHeartRateBpm, totalCalories))
        request(WearMessagePaths.FINISH_SESSION, body, WearMessagePaths.SESSION_FINISHED)
    }

    private suspend fun request(path: String, body: String, replyPath: String): String {
        val node = nodeClient.connectedNodes.await().firstOrNull()
            ?: throw PhoneConnectionException("Não foi possível conectar ao celular")

        val deferred = CompletableDeferred<String>()
        pending[replyPath] = deferred
        try {
            messageClient.sendMessage(node.id, path, body.encodeToByteArray()).await()
            return withTimeout(10_000) { deferred.await() }
        } catch (e: Exception) {
            throw PhoneConnectionException("Não foi possível conectar ao celular")
        } finally {
            pending.remove(replyPath)
        }
    }

    fun dispose() {
        messageClient.removeListener(this)
    }
}

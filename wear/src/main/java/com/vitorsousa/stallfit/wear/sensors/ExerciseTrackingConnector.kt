package com.vitorsousa.stallfit.wear.sensors

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Owns bind/unbind of [ExerciseTrackingService] on behalf of [com.vitorsousa.stallfit.wear.ui.WearViewModel],
 * the same role [com.vitorsousa.stallfit.wear.data.PhoneConnector] plays for the phone connection.
 * Every method here is best-effort and never throws — sensor tracking is an enhancement on top of
 * the existing workout flow, never a requirement for it.
 */
class ExerciseTrackingConnector(context: Context) {

    private val appContext = context.applicationContext
    private val connectorScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private var isBound = false
    private var boundService: ExerciseTrackingService? = null
    private var collectJob: Job? = null

    private val _metrics = MutableStateFlow<ExerciseMetrics?>(null)
    val metrics: StateFlow<ExerciseMetrics?> = _metrics.asStateFlow()

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, binder: IBinder) {
            val service = (binder as ExerciseTrackingService.LocalBinder).getService()
            boundService = service
            collectJob = connectorScope.launch {
                service.metrics.collect { _metrics.value = it }
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            boundService = null
            collectJob?.cancel()
        }
    }

    /** Fire-and-forget: starts the foreground service and binds to it. Never throws. */
    fun startTracking() {
        if (isBound) return
        runCatching {
            val intent = Intent(appContext, ExerciseTrackingService::class.java)
            ContextCompat.startForegroundService(appContext, intent)
            isBound = appContext.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    /**
     * Idempotent, never throws. Returns `null` when tracking never connected in time (e.g. a very
     * short session finished before [android.content.ServiceConnection.onServiceConnected] fired)
     * — an accepted outcome, not an error.
     */
    suspend fun stopTracking(): ExerciseMetrics? {
        val snapshot = runCatching { boundService?.stopTrackingAndSnapshot() }.getOrNull() ?: _metrics.value
        if (isBound) {
            runCatching { appContext.unbindService(connection) }
            isBound = false
        }
        collectJob?.cancel()
        boundService = null
        _metrics.value = null
        return snapshot
    }

    fun dispose() {
        if (isBound) {
            runCatching { appContext.unbindService(connection) }
            isBound = false
        }
    }
}

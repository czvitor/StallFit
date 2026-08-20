package com.vitorsousa.stallfit.wear.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitorsousa.stallfit.wear.data.PhoneConnectionException
import com.vitorsousa.stallfit.wear.data.PhoneConnector
import com.vitorsousa.stallfit.wear.data.WearCatalogDto
import com.vitorsousa.stallfit.wear.data.WearExerciseDto
import com.vitorsousa.stallfit.wear.sensors.ExerciseTrackingConnector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Where the watch currently is: picking a template, or logging sets against an already-started session. */
sealed interface WearScreen {
    data object SelectTemplate : WearScreen
    data class ActiveSession(val sessionId: Long, val sessionName: String, val exercises: List<WearExerciseDto>) : WearScreen
}

sealed interface CatalogState {
    data object Loading : CatalogState
    data class Loaded(val catalog: WearCatalogDto) : CatalogState
    data class Error(val message: String) : CatalogState
}

/** Outcome of the in-flight start/log/finish request, driving a loading/error indicator on the active screen. */
sealed interface ActionState {
    data object Idle : ActionState
    data object Sending : ActionState
    data object Success : ActionState
    data class Error(val message: String) : ActionState
}

class WearViewModel(
    private val phoneConnector: PhoneConnector,
    private val exerciseTrackingConnector: ExerciseTrackingConnector
) : ViewModel() {

    private val _screen = MutableStateFlow<WearScreen>(WearScreen.SelectTemplate)
    val screen: StateFlow<WearScreen> = _screen.asStateFlow()

    val liveHeartRateBpm: StateFlow<Int?> = exerciseTrackingConnector.metrics
        .map { it?.liveHeartRateBpm }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val _catalogState = MutableStateFlow<CatalogState>(CatalogState.Loading)
    val catalogState: StateFlow<CatalogState> = _catalogState.asStateFlow()

    private val _actionState = MutableStateFlow<ActionState>(ActionState.Idle)
    val actionState: StateFlow<ActionState> = _actionState.asStateFlow()

    private val _lastLoggedSetNumber = MutableStateFlow<Int?>(null)
    val lastLoggedSetNumber: StateFlow<Int?> = _lastLoggedSetNumber.asStateFlow()

    init {
        loadCatalog()
    }

    fun loadCatalog() {
        viewModelScope.launch {
            _catalogState.value = CatalogState.Loading
            _catalogState.value = try {
                CatalogState.Loaded(phoneConnector.getCatalog())
            } catch (e: PhoneConnectionException) {
                CatalogState.Error(e.message.orEmpty())
            }
        }
    }

    /** Starts a session for [templateId], or a free workout when null, then moves to [WearScreen.ActiveSession]. */
    fun selectTemplate(templateId: Long?) {
        val catalog = (_catalogState.value as? CatalogState.Loaded)?.catalog ?: return
        val exercises = if (templateId != null) {
            catalog.templates.firstOrNull { it.id == templateId }?.exercises.orEmpty()
        } else {
            catalog.allExercises
        }
        viewModelScope.launch {
            _actionState.value = ActionState.Sending
            try {
                val started = phoneConnector.startSession(templateId)
                _screen.value = WearScreen.ActiveSession(started.sessionId, started.sessionName, exercises)
                _actionState.value = ActionState.Idle
                exerciseTrackingConnector.startTracking()
            } catch (e: PhoneConnectionException) {
                _actionState.value = ActionState.Error(e.message.orEmpty())
            }
        }
    }

    fun logSet(exerciseId: Long, reps: Int, weightKg: Double) {
        val session = _screen.value as? WearScreen.ActiveSession ?: return
        viewModelScope.launch {
            _actionState.value = ActionState.Sending
            try {
                val result = phoneConnector.logSet(session.sessionId, exerciseId, reps, weightKg)
                _lastLoggedSetNumber.value = result.setNumber
                _actionState.value = ActionState.Success
            } catch (e: PhoneConnectionException) {
                _actionState.value = ActionState.Error(e.message.orEmpty())
            }
        }
    }

    fun finishSession() {
        val session = _screen.value as? WearScreen.ActiveSession ?: return
        viewModelScope.launch {
            _actionState.value = ActionState.Sending
            val metrics = exerciseTrackingConnector.stopTracking()
            try {
                phoneConnector.finishSession(session.sessionId, metrics?.avgHeartRateBpm, metrics?.totalCalories)
                _lastLoggedSetNumber.value = null
                _screen.value = WearScreen.SelectTemplate
                _actionState.value = ActionState.Idle
                loadCatalog()
            } catch (e: PhoneConnectionException) {
                _actionState.value = ActionState.Error(e.message.orEmpty())
            }
        }
    }

    fun dismissActionMessage() {
        _actionState.value = ActionState.Idle
    }

    override fun onCleared() {
        phoneConnector.dispose()
        exerciseTrackingConnector.dispose()
    }
}

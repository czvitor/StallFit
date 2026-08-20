package com.vitorsousa.stallfit.ui.workout

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitorsousa.stallfit.data.local.entity.ExerciseEntity
import com.vitorsousa.stallfit.data.local.entity.SetEntryEntity
import com.vitorsousa.stallfit.data.local.entity.bestSet
import com.vitorsousa.stallfit.data.repository.HealthConnectAvailability
import com.vitorsousa.stallfit.data.repository.HealthConnectRepository
import com.vitorsousa.stallfit.data.repository.WorkoutRepository
import com.vitorsousa.stallfit.navigation.Destination
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Outcome of a Health Connect sync attempt for the current session, driving the UI's button/message state. */
sealed interface HealthSyncState {
    data object Idle : HealthSyncState
    data object Syncing : HealthSyncState
    data object Success : HealthSyncState
    data object Error : HealthSyncState
}

@OptIn(ExperimentalCoroutinesApi::class)
class ActiveWorkoutViewModel(
    savedStateHandle: SavedStateHandle,
    private val workoutRepository: WorkoutRepository,
    private val healthConnectRepository: HealthConnectRepository
) : ViewModel() {

    private val sessionId: Long = checkNotNull(savedStateHandle[Destination.WorkoutSession.ARG_SESSION_ID])

    val exercises: StateFlow<List<ExerciseEntity>> = workoutRepository.allExercises
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val uiState: StateFlow<ActiveWorkoutUiState> = combine(
        workoutRepository.getSessionFlow(sessionId),
        workoutRepository.getSetsForSession(sessionId),
        workoutRepository.getVolumeForSession(sessionId)
    ) { session, sets, volume ->
        ActiveWorkoutUiState(session, sets, volume)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ActiveWorkoutUiState()
    )

    private val _selectedExerciseId = MutableStateFlow<Long?>(null)
    val selectedExerciseId: StateFlow<Long?> = _selectedExerciseId.asStateFlow()

    // Driven off the live per-exercise history Flow (instead of one-shot suspend queries
    // re-fetched manually) so switching exercises, logging a set, and deleting a set all update
    // "Último registro"/"Recorde pessoal" automatically and can never show a stale snapshot.
    private val historyForSelected: StateFlow<List<SetEntryEntity>> = _selectedExerciseId
        .flatMapLatest { exerciseId ->
            exerciseId?.let(workoutRepository::getSetsHistoryForExercise) ?: flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val lastSetForSelected: StateFlow<SetEntryEntity?> = historyForSelected
        .map { it.lastOrNull() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val bestSetForSelected: StateFlow<SetEntryEntity?> = historyForSelected
        .map { it.bestSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun selectExercise(exerciseId: Long) {
        _selectedExerciseId.value = exerciseId
    }

    fun logSet(reps: Int, weightKg: Double) {
        val exerciseId = _selectedExerciseId.value ?: return
        viewModelScope.launch {
            val nextSetNumber = uiState.value.sets.count { it.setEntry.exerciseId == exerciseId } + 1
            workoutRepository.logSet(sessionId, exerciseId, nextSetNumber, reps, weightKg)
        }
    }

    fun deleteSet(setEntry: SetEntryEntity) {
        viewModelScope.launch { workoutRepository.deleteSet(setEntry) }
    }

    fun addCustomExercise(name: String, muscleGroup: String) {
        viewModelScope.launch {
            val id = workoutRepository.addCustomExercise(name, muscleGroup)
            selectExercise(id)
        }
    }

    fun finishSession() {
        viewModelScope.launch {
            uiState.value.session?.let { workoutRepository.finishSession(it) }
        }
    }

    fun discardSession() {
        viewModelScope.launch { workoutRepository.discardSession(sessionId) }
    }

    val healthConnectPermissions: Set<String> = healthConnectRepository.requiredPermissions
    val healthConnectPermissionsContract by lazy { healthConnectRepository.requestPermissionsContract() }

    /** Synchronous pre-flight check so the UI can decide, before syncing, whether to prompt for install or permission. */
    fun healthConnectAvailability(): HealthConnectAvailability = healthConnectRepository.availability()

    suspend fun hasHealthConnectPermissions(): Boolean = healthConnectRepository.hasPermissions()

    private val _healthSyncState = MutableStateFlow<HealthSyncState>(HealthSyncState.Idle)
    val healthSyncState: StateFlow<HealthSyncState> = _healthSyncState.asStateFlow()

    /** Assumes Health Connect is installed and write permission is already granted — the UI checks both first. */
    fun syncToHealthConnect() {
        val session = uiState.value.session ?: return
        viewModelScope.launch {
            _healthSyncState.value = HealthSyncState.Syncing
            val result = healthConnectRepository.writeWorkoutSession(session)
            _healthSyncState.value = result.fold(
                onSuccess = {
                    workoutRepository.markHealthConnectSynced(session, System.currentTimeMillis())
                    HealthSyncState.Success
                },
                onFailure = { HealthSyncState.Error }
            )
        }
    }

    fun dismissHealthSyncMessage() {
        _healthSyncState.value = HealthSyncState.Idle
    }
}

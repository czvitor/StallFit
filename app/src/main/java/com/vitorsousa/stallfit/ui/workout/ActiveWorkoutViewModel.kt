package com.vitorsousa.stallfit.ui.workout

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitorsousa.stallfit.data.local.entity.ExerciseEntity
import com.vitorsousa.stallfit.data.local.entity.SetEntryEntity
import com.vitorsousa.stallfit.data.repository.WorkoutRepository
import com.vitorsousa.stallfit.navigation.Destination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ActiveWorkoutViewModel(
    savedStateHandle: SavedStateHandle,
    private val workoutRepository: WorkoutRepository
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

    private val _lastSetForSelected = MutableStateFlow<SetEntryEntity?>(null)
    val lastSetForSelected: StateFlow<SetEntryEntity?> = _lastSetForSelected.asStateFlow()

    fun selectExercise(exerciseId: Long) {
        _selectedExerciseId.value = exerciseId
        viewModelScope.launch {
            _lastSetForSelected.value = workoutRepository.getLastSetForExercise(exerciseId)
        }
    }

    fun logSet(reps: Int, weightKg: Double) {
        val exerciseId = _selectedExerciseId.value ?: return
        viewModelScope.launch {
            val nextSetNumber = uiState.value.sets.count { it.setEntry.exerciseId == exerciseId } + 1
            workoutRepository.logSet(sessionId, exerciseId, nextSetNumber, reps, weightKg)
            _lastSetForSelected.value = workoutRepository.getLastSetForExercise(exerciseId)
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
}

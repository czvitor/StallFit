package com.vitorsousa.stallfit.ui.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitorsousa.stallfit.data.local.entity.ExerciseEntity
import com.vitorsousa.stallfit.data.local.entity.SetEntryEntity
import com.vitorsousa.stallfit.data.local.entity.bestSet
import com.vitorsousa.stallfit.data.repository.WorkoutRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class ExerciseProgressViewModel(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    val exercises: StateFlow<List<ExerciseEntity>> = workoutRepository.allExercises
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _selectedExerciseId = MutableStateFlow<Long?>(null)
    val selectedExerciseId: StateFlow<Long?> = _selectedExerciseId.asStateFlow()

    val history: StateFlow<List<SetEntryEntity>> = _selectedExerciseId
        .flatMapLatest { exerciseId ->
            exerciseId?.let(workoutRepository::getSetsHistoryForExercise) ?: flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val bestSet: StateFlow<SetEntryEntity?> = history
        .map { it.bestSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun selectExercise(exerciseId: Long) {
        _selectedExerciseId.value = exerciseId
    }
}

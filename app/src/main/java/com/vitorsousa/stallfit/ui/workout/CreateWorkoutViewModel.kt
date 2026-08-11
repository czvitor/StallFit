package com.vitorsousa.stallfit.ui.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitorsousa.stallfit.data.local.entity.Equipment
import com.vitorsousa.stallfit.data.local.entity.ExerciseEntity
import com.vitorsousa.stallfit.data.local.entity.TemplateExerciseEntity
import com.vitorsousa.stallfit.data.repository.WorkoutRepository
import com.vitorsousa.stallfit.data.repository.WorkoutTemplateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CreateWorkoutViewModel(
    private val workoutRepository: WorkoutRepository,
    private val workoutTemplateRepository: WorkoutTemplateRepository
) : ViewModel() {

    private val _draftRows = MutableStateFlow<List<DraftExerciseRow>>(emptyList())

    val uiState: StateFlow<CreateWorkoutUiState> = combine(
        workoutRepository.allExercises,
        _draftRows
    ) { exercises, rows -> CreateWorkoutUiState(exercises = exercises, draftRows = rows) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CreateWorkoutUiState())

    fun addExerciseRow(exercise: ExerciseEntity) {
        _draftRows.value = _draftRows.value + DraftExerciseRow(exerciseId = exercise.id, exerciseName = exercise.name)
    }

    fun removeExerciseRow(index: Int) {
        _draftRows.value = _draftRows.value.toMutableList().apply { removeAt(index) }
    }

    fun updateRow(index: Int, row: DraftExerciseRow) {
        _draftRows.value = _draftRows.value.toMutableList().apply { this[index] = row }
    }

    fun addCustomExercise(
        name: String,
        muscleGroup: String,
        equipment: Equipment,
        onAdded: (ExerciseEntity) -> Unit
    ) {
        viewModelScope.launch {
            val id = workoutRepository.addCustomExercise(name, muscleGroup, equipment)
            onAdded(ExerciseEntity(id = id, name = name, muscleGroup = muscleGroup, equipment = equipment, isCustom = true))
        }
    }

    fun saveTemplate(title: String, onSaved: () -> Unit) {
        val rows = _draftRows.value
        if (title.isBlank() || rows.isEmpty()) return
        viewModelScope.launch {
            val exercises = rows.mapIndexed { index, row ->
                TemplateExerciseEntity(
                    templateId = 0,
                    exerciseId = row.exerciseId,
                    orderIndex = index,
                    sets = row.sets,
                    repRangeMin = row.repRangeMin,
                    repRangeMax = row.repRangeMax,
                    restSeconds = row.restSeconds,
                    intensity = row.intensity
                )
            }
            workoutTemplateRepository.createTemplate(title.trim(), exercises)
            onSaved()
        }
    }
}

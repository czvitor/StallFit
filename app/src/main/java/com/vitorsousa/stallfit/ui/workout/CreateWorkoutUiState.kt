package com.vitorsousa.stallfit.ui.workout

import com.vitorsousa.stallfit.data.local.entity.ExerciseEntity
import com.vitorsousa.stallfit.data.local.entity.Intensity

data class DraftExerciseRow(
    val exerciseId: Long,
    val exerciseName: String,
    val sets: Int = 3,
    val repRangeMin: Int = 8,
    val repRangeMax: Int = 12,
    val restSeconds: Int = 60,
    val intensity: Intensity = Intensity.MODERADA
)

data class CreateWorkoutUiState(
    val exercises: List<ExerciseEntity> = emptyList(),
    val draftRows: List<DraftExerciseRow> = emptyList()
)

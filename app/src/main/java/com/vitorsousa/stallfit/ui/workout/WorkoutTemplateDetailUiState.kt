package com.vitorsousa.stallfit.ui.workout

import com.vitorsousa.stallfit.data.local.entity.WorkoutSessionEntity
import com.vitorsousa.stallfit.data.local.entity.WorkoutTemplateEntity
import com.vitorsousa.stallfit.data.local.relation.TemplateExerciseWithExercise

data class WorkoutTemplateDetailUiState(
    val template: WorkoutTemplateEntity? = null,
    val exercises: List<TemplateExerciseWithExercise> = emptyList(),
    val session: WorkoutSessionEntity? = null,
    val isLoading: Boolean = true
)

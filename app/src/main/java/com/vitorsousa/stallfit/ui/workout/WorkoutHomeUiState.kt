package com.vitorsousa.stallfit.ui.workout

import com.vitorsousa.stallfit.data.local.entity.WorkoutSessionEntity
import com.vitorsousa.stallfit.data.local.relation.TemplateWithExercises

data class WorkoutHomeUiState(
    val activeSession: WorkoutSessionEntity? = null,
    val completedSessions: List<WorkoutSessionEntity> = emptyList(),
    val volumeThisWeek: Double = 0.0,
    val templates: List<TemplateWithExercises> = emptyList()
)

package com.vitorsousa.stallfit.ui.workout

import com.vitorsousa.stallfit.data.local.entity.WorkoutSessionEntity

data class WorkoutHomeUiState(
    val activeSession: WorkoutSessionEntity? = null,
    val completedSessions: List<WorkoutSessionEntity> = emptyList(),
    val volumeThisWeek: Double = 0.0
)

package com.vitorsousa.stallfit.ui.dashboard

import com.vitorsousa.stallfit.data.local.entity.MacroGoalEntity
import com.vitorsousa.stallfit.data.local.entity.WorkoutSessionEntity
import com.vitorsousa.stallfit.domain.model.MetabolicResult

data class DashboardUiState(
    val volumeToday: Double = 0.0,
    val macroGoal: MacroGoalEntity? = null,
    val metabolicResult: MetabolicResult? = null,
    val activeSession: WorkoutSessionEntity? = null,
    val isLoading: Boolean = true
)

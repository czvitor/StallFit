package com.vitorsousa.stallfit.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitorsousa.stallfit.core.util.DateUtils
import com.vitorsousa.stallfit.data.repository.NutritionRepository
import com.vitorsousa.stallfit.data.repository.WorkoutRepository
import com.vitorsousa.stallfit.domain.model.toMacroTotals
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel(
    workoutRepository: WorkoutRepository,
    nutritionRepository: NutritionRepository
) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> = combine(
        workoutRepository.getVolumeToday(),
        nutritionRepository.getEntriesForDate(DateUtils.todayEpochDay()).map { it.toMacroTotals() },
        nutritionRepository.macroGoal,
        workoutRepository.activeSession
    ) { volume, macros, goal, active ->
        DashboardUiState(
            volumeToday = volume,
            macroTotals = macros,
            macroGoal = goal,
            activeSession = active,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DashboardUiState()
    )
}

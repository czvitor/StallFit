package com.vitorsousa.stallfit.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitorsousa.stallfit.data.repository.NutritionRepository
import com.vitorsousa.stallfit.data.repository.ProfileRepository
import com.vitorsousa.stallfit.data.repository.WorkoutRepository
import com.vitorsousa.stallfit.domain.model.MetabolicCalculator
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel(
    workoutRepository: WorkoutRepository,
    nutritionRepository: NutritionRepository,
    profileRepository: ProfileRepository
) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> = combine(
        workoutRepository.getVolumeToday(),
        nutritionRepository.macroGoal,
        workoutRepository.activeSession,
        profileRepository.profile
    ) { volume, goal, active, profile ->
        DashboardUiState(
            volumeToday = volume,
            macroGoal = goal,
            metabolicResult = profile?.let {
                MetabolicCalculator.calculate(
                    ageYears = it.ageYears,
                    weightKg = it.weightKg,
                    heightCm = it.heightCm,
                    sex = it.sex,
                    activityLevel = it.activityLevel,
                    goal = it.goal
                )
            },
            activeSession = active,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DashboardUiState()
    )
}

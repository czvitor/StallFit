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
        profileRepository.profile,
        profileRepository.latestMeasurement
    ) { volume, goal, active, profile, latestMeasurement ->
        DashboardUiState(
            volumeToday = volume,
            macroGoal = goal,
            metabolicResult = if (profile != null && latestMeasurement != null) {
                MetabolicCalculator.calculate(
                    ageYears = profile.ageYears,
                    weightKg = latestMeasurement.weightKg,
                    heightCm = latestMeasurement.heightCm,
                    sex = profile.sex,
                    activityLevel = profile.activityLevel,
                    goal = profile.goal
                )
            } else null,
            activeSession = active,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DashboardUiState()
    )
}

package com.vitorsousa.stallfit.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitorsousa.stallfit.data.local.entity.ActivityLevel
import com.vitorsousa.stallfit.data.local.entity.BiologicalSex
import com.vitorsousa.stallfit.data.local.entity.MacroGoalEntity
import com.vitorsousa.stallfit.data.local.entity.NutritionGoal
import com.vitorsousa.stallfit.data.local.entity.UserProfileEntity
import com.vitorsousa.stallfit.data.repository.NutritionRepository
import com.vitorsousa.stallfit.data.repository.ProfileRepository
import com.vitorsousa.stallfit.domain.model.MetabolicCalculator
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val nutritionRepository: NutritionRepository
) : ViewModel() {

    val uiState: StateFlow<ProfileUiState> = profileRepository.profile
        .map { profile ->
            ProfileUiState(
                profile = profile,
                result = profile?.let {
                    MetabolicCalculator.calculate(
                        ageYears = it.ageYears,
                        weightKg = it.weightKg,
                        heightCm = it.heightCm,
                        sex = it.sex,
                        activityLevel = it.activityLevel,
                        goal = it.goal
                    )
                },
                isLoading = false
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ProfileUiState())

    fun saveProfile(
        ageYears: Int,
        weightKg: Double,
        heightCm: Double,
        sex: BiologicalSex,
        activityLevel: ActivityLevel,
        goal: NutritionGoal,
        armCm: Double?,
        chestCm: Double?,
        hipCm: Double?,
        thighCm: Double?,
        calfCm: Double?,
        onSaved: () -> Unit
    ) {
        viewModelScope.launch {
            val profile = UserProfileEntity(
                ageYears = ageYears,
                weightKg = weightKg,
                heightCm = heightCm,
                sex = sex,
                activityLevel = activityLevel,
                goal = goal,
                armCm = armCm,
                chestCm = chestCm,
                hipCm = hipCm,
                thighCm = thighCm,
                calfCm = calfCm
            )
            profileRepository.saveProfile(profile)

            val result = MetabolicCalculator.calculate(
                ageYears = ageYears,
                weightKg = weightKg,
                heightCm = heightCm,
                sex = sex,
                activityLevel = activityLevel,
                goal = goal
            )
            // Confirmed with product owner: saving the profile always overwrites the stored
            // Goals macros — Goals stays a manual-override screen on top of this calculation.
            nutritionRepository.setMacroGoal(
                MacroGoalEntity(
                    calorieGoal = result.calorieTarget,
                    proteinGoal = result.proteinGramsGoal,
                    carbGoal = result.carbGramsGoal,
                    fatGoal = result.fatGramsGoal
                )
            )
            onSaved()
        }
    }
}

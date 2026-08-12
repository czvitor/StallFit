package com.vitorsousa.stallfit.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitorsousa.stallfit.data.local.entity.ActivityLevel
import com.vitorsousa.stallfit.data.local.entity.BiologicalSex
import com.vitorsousa.stallfit.data.local.entity.BodyMeasurementEntity
import com.vitorsousa.stallfit.data.local.entity.MacroGoalEntity
import com.vitorsousa.stallfit.data.local.entity.NutritionGoal
import com.vitorsousa.stallfit.data.local.entity.UserProfileEntity
import com.vitorsousa.stallfit.data.repository.NutritionRepository
import com.vitorsousa.stallfit.data.repository.ProfileRepository
import com.vitorsousa.stallfit.domain.model.MetabolicCalculator
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val nutritionRepository: NutritionRepository
) : ViewModel() {

    val uiState: StateFlow<ProfileUiState> = combine(
        profileRepository.profile,
        profileRepository.latestMeasurement,
        profileRepository.measurementHistory
    ) { profile, latestMeasurement, history ->
        ProfileUiState(
            profile = profile,
            latestMeasurement = latestMeasurement,
            measurementHistory = history,
            result = if (profile != null && latestMeasurement != null) {
                MetabolicCalculator.calculate(
                    ageYears = profile.ageYears,
                    weightKg = latestMeasurement.weightKg,
                    heightCm = latestMeasurement.heightCm,
                    sex = profile.sex,
                    activityLevel = profile.activityLevel,
                    goal = profile.goal
                )
            } else null,
            isLoading = false
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ProfileUiState())

    fun saveProfile(
        name: String,
        ageYears: Int,
        sex: BiologicalSex,
        activityLevel: ActivityLevel,
        goal: NutritionGoal
    ) {
        viewModelScope.launch {
            profileRepository.saveProfile(
                UserProfileEntity(
                    name = name,
                    ageYears = ageYears,
                    sex = sex,
                    activityLevel = activityLevel,
                    goal = goal
                )
            )
            recalculateAndPushGoal()
        }
    }

    fun addMeasurement(measurement: BodyMeasurementEntity, onSaved: () -> Unit = {}) {
        viewModelScope.launch {
            profileRepository.addMeasurement(measurement)
            recalculateAndPushGoal()
            onSaved()
        }
    }

    fun deleteMeasurement(measurement: BodyMeasurementEntity, onDeleted: () -> Unit = {}) {
        viewModelScope.launch {
            profileRepository.deleteMeasurement(measurement)
            recalculateAndPushGoal()
            onDeleted()
        }
    }

    // Confirmed with product owner: saving the profile or a new measurement always overwrites
    // the stored Goals macros — Goals stays a manual-override screen on top of this calculation.
    // Weight/height now come from the most recent Evolução Física record, not the profile itself.
    private suspend fun recalculateAndPushGoal() {
        val profile = profileRepository.profile.first() ?: return
        val latestMeasurement = profileRepository.getLatestMeasurementOnce() ?: return
        val result = MetabolicCalculator.calculate(
            ageYears = profile.ageYears,
            weightKg = latestMeasurement.weightKg,
            heightCm = latestMeasurement.heightCm,
            sex = profile.sex,
            activityLevel = profile.activityLevel,
            goal = profile.goal
        )
        nutritionRepository.setMacroGoal(
            MacroGoalEntity(
                calorieGoal = result.calorieTarget,
                proteinGoal = result.proteinGramsGoal,
                carbGoal = result.carbGramsGoal,
                fatGoal = result.fatGramsGoal
            )
        )
    }
}

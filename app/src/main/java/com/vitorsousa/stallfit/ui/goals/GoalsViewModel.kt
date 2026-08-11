package com.vitorsousa.stallfit.ui.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitorsousa.stallfit.data.local.entity.MacroGoalEntity
import com.vitorsousa.stallfit.data.repository.NutritionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GoalsViewModel(
    private val nutritionRepository: NutritionRepository
) : ViewModel() {

    val macroGoal: StateFlow<MacroGoalEntity?> = nutritionRepository.macroGoal
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun saveGoal(calorieGoal: Int, proteinGoal: Int, carbGoal: Int, fatGoal: Int, onSaved: () -> Unit) {
        viewModelScope.launch {
            nutritionRepository.setMacroGoal(
                MacroGoalEntity(
                    calorieGoal = calorieGoal,
                    proteinGoal = proteinGoal,
                    carbGoal = carbGoal,
                    fatGoal = fatGoal
                )
            )
            onSaved()
        }
    }
}

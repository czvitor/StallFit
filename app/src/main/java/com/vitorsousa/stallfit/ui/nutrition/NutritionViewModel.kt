package com.vitorsousa.stallfit.ui.nutrition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitorsousa.stallfit.data.repository.NutritionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class NutritionViewModel(
    nutritionRepository: NutritionRepository
) : ViewModel() {

    val uiState: StateFlow<NutritionUiState> = nutritionRepository.mealsWithTotals
        .map { meals -> NutritionUiState(mealsByType = meals.groupBy { it.mealType }, isLoading = false) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), NutritionUiState())
}

package com.vitorsousa.stallfit.ui.nutrition

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitorsousa.stallfit.data.repository.NutritionRepository
import com.vitorsousa.stallfit.navigation.Destination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MealDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val nutritionRepository: NutritionRepository
) : ViewModel() {

    private val mealId: Long = checkNotNull(savedStateHandle[Destination.MealDetail.ARG_MEAL_ID])

    val uiState: StateFlow<MealDetailUiState> = combine(
        nutritionRepository.getMealFlow(mealId),
        nutritionRepository.getItemsForMeal(mealId)
    ) { meal, items -> MealDetailUiState(meal = meal, items = items, isLoading = false) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), MealDetailUiState())

    fun deleteMeal(onDeleted: () -> Unit) {
        viewModelScope.launch {
            nutritionRepository.deleteMeal(mealId)
            onDeleted()
        }
    }
}

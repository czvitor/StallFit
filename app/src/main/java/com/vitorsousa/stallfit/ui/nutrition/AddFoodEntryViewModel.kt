package com.vitorsousa.stallfit.ui.nutrition

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitorsousa.stallfit.data.local.entity.FoodEntity
import com.vitorsousa.stallfit.data.local.entity.MealType
import com.vitorsousa.stallfit.data.repository.NutritionRepository
import com.vitorsousa.stallfit.navigation.Destination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AddFoodEntryViewModel(
    savedStateHandle: SavedStateHandle,
    private val nutritionRepository: NutritionRepository
) : ViewModel() {

    val mealType: MealType = MealType.valueOf(
        checkNotNull(savedStateHandle[Destination.AddFoodEntry.ARG_MEAL_TYPE])
    )
    val dateEpochDay: Long = checkNotNull(savedStateHandle[Destination.AddFoodEntry.ARG_DATE_EPOCH_DAY])

    val foods: StateFlow<List<FoodEntity>> = nutritionRepository.allFoods
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun logFood(foodId: Long, grams: Double, onDone: () -> Unit) {
        viewModelScope.launch {
            nutritionRepository.logMeal(foodId, mealType, grams, dateEpochDay)
            onDone()
        }
    }

    fun addCustomFoodAndLog(
        name: String,
        caloriesPer100g: Double,
        proteinPer100g: Double,
        carbsPer100g: Double,
        fatPer100g: Double,
        gramsToLog: Double,
        onDone: () -> Unit
    ) {
        viewModelScope.launch {
            val foodId = nutritionRepository.addCustomFood(
                name = name,
                caloriesPer100g = caloriesPer100g,
                proteinPer100g = proteinPer100g,
                carbsPer100g = carbsPer100g,
                fatPer100g = fatPer100g
            )
            nutritionRepository.logMeal(foodId, mealType, gramsToLog, dateEpochDay)
            onDone()
        }
    }
}

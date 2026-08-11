package com.vitorsousa.stallfit.ui.nutrition

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitorsousa.stallfit.data.local.entity.FoodEntity
import com.vitorsousa.stallfit.data.local.entity.MealFoodItemEntity
import com.vitorsousa.stallfit.data.local.entity.MealType
import com.vitorsousa.stallfit.data.repository.NutritionRepository
import com.vitorsousa.stallfit.navigation.Destination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CreateMealViewModel(
    savedStateHandle: SavedStateHandle,
    private val nutritionRepository: NutritionRepository
) : ViewModel() {

    val mealType: MealType = MealType.valueOf(
        checkNotNull(savedStateHandle[Destination.CreateMeal.ARG_MEAL_TYPE])
    )

    private val draftItems = MutableStateFlow<List<DraftMealItem>>(emptyList())

    val uiState: StateFlow<CreateMealUiState> = combine(
        nutritionRepository.allFoods,
        draftItems
    ) { foods, items -> CreateMealUiState(foods = foods, draftItems = items) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CreateMealUiState())

    fun addFoodItem(food: FoodEntity, grams: Double) {
        draftItems.value = draftItems.value + DraftMealItem(
            foodId = food.id,
            foodName = food.name,
            grams = grams,
            caloriesPer100g = food.caloriesPer100g,
            proteinPer100g = food.proteinPer100g,
            carbsPer100g = food.carbsPer100g,
            fatPer100g = food.fatPer100g
        )
    }

    fun removeItem(index: Int) {
        draftItems.value = draftItems.value.toMutableList().apply { removeAt(index) }
    }

    fun addCustomFoodAndAppend(
        name: String,
        caloriesPer100g: Double,
        proteinPer100g: Double,
        carbsPer100g: Double,
        fatPer100g: Double,
        grams: Double
    ) {
        viewModelScope.launch {
            val foodId = nutritionRepository.addCustomFood(
                name = name,
                caloriesPer100g = caloriesPer100g,
                proteinPer100g = proteinPer100g,
                carbsPer100g = carbsPer100g,
                fatPer100g = fatPer100g
            )
            draftItems.value = draftItems.value + DraftMealItem(
                foodId = foodId,
                foodName = name,
                grams = grams,
                caloriesPer100g = caloriesPer100g,
                proteinPer100g = proteinPer100g,
                carbsPer100g = carbsPer100g,
                fatPer100g = fatPer100g
            )
        }
    }

    fun saveMeal(name: String, onSaved: () -> Unit) {
        val items = draftItems.value
        if (name.isBlank() || items.isEmpty()) return
        viewModelScope.launch {
            nutritionRepository.createMeal(
                name = name.trim(),
                mealType = mealType,
                items = items.map { MealFoodItemEntity(mealId = 0, foodId = it.foodId, grams = it.grams) }
            )
            onSaved()
        }
    }
}

package com.vitorsousa.stallfit.ui.nutrition

import com.vitorsousa.stallfit.data.local.entity.FoodEntity

data class DraftMealItem(
    val foodId: Long,
    val foodName: String,
    val grams: Double,
    val caloriesPer100g: Double,
    val proteinPer100g: Double,
    val carbsPer100g: Double,
    val fatPer100g: Double
) {
    val calories: Double get() = caloriesPer100g * grams / 100.0
    val protein: Double get() = proteinPer100g * grams / 100.0
    val carbs: Double get() = carbsPer100g * grams / 100.0
    val fat: Double get() = fatPer100g * grams / 100.0
}

data class CreateMealUiState(
    val foods: List<FoodEntity> = emptyList(),
    val draftItems: List<DraftMealItem> = emptyList()
) {
    val totalCalories: Double get() = draftItems.sumOf { it.calories }
    val totalProtein: Double get() = draftItems.sumOf { it.protein }
    val totalCarbs: Double get() = draftItems.sumOf { it.carbs }
    val totalFat: Double get() = draftItems.sumOf { it.fat }
}

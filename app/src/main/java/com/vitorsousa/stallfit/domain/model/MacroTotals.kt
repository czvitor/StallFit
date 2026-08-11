package com.vitorsousa.stallfit.domain.model

import com.vitorsousa.stallfit.data.local.relation.MealFoodItemWithFood
import kotlin.math.roundToInt

data class MacroTotals(
    val calories: Double = 0.0,
    val protein: Double = 0.0,
    val carbs: Double = 0.0,
    val fat: Double = 0.0
) {
    val caloriesRounded: Int get() = calories.roundToInt()
    val proteinRounded: Int get() = protein.roundToInt()
    val carbsRounded: Int get() = carbs.roundToInt()
    val fatRounded: Int get() = fat.roundToInt()
}

fun List<MealFoodItemWithFood>.toMacroTotals(): MacroTotals = MacroTotals(
    calories = sumOf { it.calories },
    protein = sumOf { it.protein },
    carbs = sumOf { it.carbs },
    fat = sumOf { it.fat }
)

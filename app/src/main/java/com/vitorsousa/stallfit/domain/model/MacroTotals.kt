package com.vitorsousa.stallfit.domain.model

import com.vitorsousa.stallfit.data.local.relation.MealEntryWithFood
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

fun List<MealEntryWithFood>.toMacroTotals(): MacroTotals = MacroTotals(
    calories = sumOf { it.calories },
    protein = sumOf { it.protein },
    carbs = sumOf { it.carbs },
    fat = sumOf { it.fat }
)

/** Guards every progress bar / ring in the UI against div-by-zero when a goal is unset. */
fun Double.progressTowards(goal: Int): Float =
    if (goal <= 0) 0f else (this / goal).toFloat().coerceIn(0f, 1f)

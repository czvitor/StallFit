package com.vitorsousa.stallfit.data.local.relation

import androidx.room.Embedded
import com.vitorsousa.stallfit.data.local.entity.MealEntryEntity

/** Result of joining `meal_entries` with `foods` — carries the per-100g values needed to compute
 *  this entry's actual macro contribution (grams / 100 * perHundredG) without a second query. */
data class MealEntryWithFood(
    @Embedded val mealEntry: MealEntryEntity,
    val foodName: String,
    val caloriesPer100g: Double,
    val proteinPer100g: Double,
    val carbsPer100g: Double,
    val fatPer100g: Double
) {
    val calories: Double get() = caloriesPer100g * mealEntry.grams / 100.0
    val protein: Double get() = proteinPer100g * mealEntry.grams / 100.0
    val carbs: Double get() = carbsPer100g * mealEntry.grams / 100.0
    val fat: Double get() = fatPer100g * mealEntry.grams / 100.0
}

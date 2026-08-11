package com.vitorsousa.stallfit.data.local.relation

import androidx.room.Embedded
import com.vitorsousa.stallfit.data.local.entity.MealFoodItemEntity

/** Result of joining `meal_food_items` with `foods` — carries the per-100g values needed to compute
 *  this item's actual macro contribution (grams / 100 * perHundredG) without a second query. */
data class MealFoodItemWithFood(
    @Embedded val item: MealFoodItemEntity,
    val foodName: String,
    val caloriesPer100g: Double,
    val proteinPer100g: Double,
    val carbsPer100g: Double,
    val fatPer100g: Double
) {
    val calories: Double get() = caloriesPer100g * item.grams / 100.0
    val protein: Double get() = proteinPer100g * item.grams / 100.0
    val carbs: Double get() = carbsPer100g * item.grams / 100.0
    val fat: Double get() = fatPer100g * item.grams / 100.0
}

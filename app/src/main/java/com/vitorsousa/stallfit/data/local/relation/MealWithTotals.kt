package com.vitorsousa.stallfit.data.local.relation

import com.vitorsousa.stallfit.data.local.entity.MealType

/** A saved meal plus its macro totals, computed in SQL from `meal_food_items` × `foods`. */
data class MealWithTotals(
    val mealId: Long,
    val mealName: String,
    val mealType: MealType,
    val createdAt: Long,
    val totalCalories: Double,
    val totalProtein: Double,
    val totalCarbs: Double,
    val totalFat: Double
)

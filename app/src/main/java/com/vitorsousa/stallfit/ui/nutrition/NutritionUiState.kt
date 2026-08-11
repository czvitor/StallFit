package com.vitorsousa.stallfit.ui.nutrition

import com.vitorsousa.stallfit.data.local.entity.MealType
import com.vitorsousa.stallfit.data.local.relation.MealWithTotals

data class NutritionUiState(
    val mealsByType: Map<MealType, List<MealWithTotals>> = emptyMap(),
    val isLoading: Boolean = true
)

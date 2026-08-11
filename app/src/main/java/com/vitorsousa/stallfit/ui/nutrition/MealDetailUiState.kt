package com.vitorsousa.stallfit.ui.nutrition

import com.vitorsousa.stallfit.data.local.entity.MealEntity
import com.vitorsousa.stallfit.data.local.relation.MealFoodItemWithFood
import com.vitorsousa.stallfit.domain.model.MacroTotals
import com.vitorsousa.stallfit.domain.model.toMacroTotals

data class MealDetailUiState(
    val meal: MealEntity? = null,
    val items: List<MealFoodItemWithFood> = emptyList(),
    val isLoading: Boolean = true
) {
    val totals: MacroTotals get() = items.toMacroTotals()
}

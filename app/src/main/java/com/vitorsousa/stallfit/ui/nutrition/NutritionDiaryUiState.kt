package com.vitorsousa.stallfit.ui.nutrition

import com.vitorsousa.stallfit.core.util.DateUtils
import com.vitorsousa.stallfit.data.local.entity.MacroGoalEntity
import com.vitorsousa.stallfit.data.local.relation.MealEntryWithFood
import com.vitorsousa.stallfit.domain.model.MacroTotals

data class NutritionDiaryUiState(
    val dateEpochDay: Long = DateUtils.todayEpochDay(),
    val entries: List<MealEntryWithFood> = emptyList(),
    val macroGoal: MacroGoalEntity? = null,
    val totals: MacroTotals = MacroTotals()
) {
    val isToday: Boolean get() = dateEpochDay == DateUtils.todayEpochDay()
}

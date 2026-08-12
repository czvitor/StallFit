package com.vitorsousa.stallfit.ui.nutrition

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vitorsousa.stallfit.data.local.entity.MealType
import com.vitorsousa.stallfit.data.local.relation.MealWithTotals
import com.vitorsousa.stallfit.di.AppViewModelProvider
import com.vitorsousa.stallfit.ui.components.AppCard
import com.vitorsousa.stallfit.ui.components.EmptyState
import com.vitorsousa.stallfit.ui.components.SectionHeader
import kotlin.math.roundToInt

@Composable
fun NutritionScreen(
    onCreateMeal: (MealType) -> Unit,
    onOpenMeal: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NutritionViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        MealType.entries.forEach { mealType ->
            item {
                SectionHeader(
                    title = mealType.label,
                    actionLabel = "Criar",
                    onActionClick = { onCreateMeal(mealType) }
                )
            }

            val meals = uiState.mealsByType[mealType].orEmpty()
            if (meals.isEmpty()) {
                item { EmptyState(message = "Nenhuma refeição cadastrada nesta categoria.") }
            } else {
                items(meals, key = { it.mealId }) { meal ->
                    MealCard(meal = meal, onClick = { onOpenMeal(meal.mealId) })
                }
            }
        }
    }
}

@Composable
private fun MealCard(meal: MealWithTotals, onClick: () -> Unit) {
    AppCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = meal.mealName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${meal.totalCalories.roundToInt()} kcal · P ${meal.totalProtein.roundToInt()}g · C ${meal.totalCarbs.roundToInt()}g · G ${meal.totalFat.roundToInt()}g",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

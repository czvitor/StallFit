package com.vitorsousa.stallfit.ui.nutrition

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vitorsousa.stallfit.core.util.DateUtils
import com.vitorsousa.stallfit.data.local.entity.MealType
import com.vitorsousa.stallfit.data.local.relation.MealEntryWithFood
import com.vitorsousa.stallfit.di.AppViewModelProvider
import com.vitorsousa.stallfit.ui.components.CalorieRing
import com.vitorsousa.stallfit.ui.components.EmptyState
import com.vitorsousa.stallfit.ui.components.MacroProgressBar
import com.vitorsousa.stallfit.ui.components.SectionHeader
import com.vitorsousa.stallfit.ui.theme.CarbColor
import com.vitorsousa.stallfit.ui.theme.FatColor
import com.vitorsousa.stallfit.ui.theme.ProteinColor
import kotlin.math.roundToInt

@Composable
fun NutritionDiaryScreen(
    onAddFood: (mealType: MealType, dateEpochDay: Long) -> Unit,
    onOpenGoals: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NutritionDiaryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val entriesByMeal = uiState.entries.groupBy { it.mealEntry.mealType }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Diário",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                IconButton(onClick = onOpenGoals) {
                    Icon(
                        imageVector = Icons.Filled.Tune,
                        contentDescription = "Metas de macros",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.previousDay() }) {
                    Icon(
                        imageVector = Icons.Filled.ChevronLeft,
                        contentDescription = "Dia anterior",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    text = DateUtils.formatDayLabel(uiState.dateEpochDay),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                IconButton(onClick = { viewModel.nextDay() }, enabled = !uiState.isToday) {
                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = "Próximo dia",
                        tint = if (uiState.isToday) {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        } else {
                            MaterialTheme.colorScheme.onBackground
                        }
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    CalorieRing(
                        consumed = uiState.totals.calories,
                        goal = uiState.macroGoal?.calorieGoal ?: 0
                    )
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        MacroProgressBar(
                            name = "Proteína",
                            consumed = uiState.totals.protein,
                            goal = uiState.macroGoal?.proteinGoal ?: 0,
                            unit = "g",
                            color = ProteinColor
                        )
                        MacroProgressBar(
                            name = "Carboidratos",
                            consumed = uiState.totals.carbs,
                            goal = uiState.macroGoal?.carbGoal ?: 0,
                            unit = "g",
                            color = CarbColor
                        )
                        MacroProgressBar(
                            name = "Gorduras",
                            consumed = uiState.totals.fat,
                            goal = uiState.macroGoal?.fatGoal ?: 0,
                            unit = "g",
                            color = FatColor
                        )
                    }
                }
            }
        }

        MealType.entries.forEach { mealType ->
            item {
                SectionHeader(
                    title = mealType.label,
                    actionLabel = "Adicionar",
                    onActionClick = { onAddFood(mealType, uiState.dateEpochDay) }
                )
            }

            val mealEntries = entriesByMeal[mealType].orEmpty()
            if (mealEntries.isEmpty()) {
                item { EmptyState(message = "Nenhum alimento registrado nesta refeição.") }
            } else {
                items(mealEntries, key = { it.mealEntry.id }) { entry ->
                    MealEntryRow(entry = entry, onDelete = { viewModel.deleteEntry(entry.mealEntry) })
                }
            }
        }
    }
}

@Composable
private fun MealEntryRow(entry: MealEntryWithFood, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.foodName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${entry.mealEntry.grams.roundToInt()} g · ${entry.calories.roundToInt()} kcal",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Filled.DeleteOutline,
                    contentDescription = "Remover",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

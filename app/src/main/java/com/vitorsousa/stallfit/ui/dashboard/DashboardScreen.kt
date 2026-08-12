package com.vitorsousa.stallfit.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vitorsousa.stallfit.di.AppViewModelProvider
import com.vitorsousa.stallfit.ui.components.AppCard
import com.vitorsousa.stallfit.ui.components.SectionHeader
import com.vitorsousa.stallfit.ui.components.StatCard
import kotlin.math.roundToInt

@Composable
fun DashboardScreen(
    onStartOrResumeWorkout: (sessionId: Long?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            AppCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Metas do dia",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    val goal = uiState.macroGoal
                    if (goal != null) {
                        Text(
                            text = "${goal.calorieGoal} kcal",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Proteína ${goal.proteinGoal}g · Carboidratos ${goal.carbGoal}g · Gorduras ${goal.fatGoal}g",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Text(
                            text = "Defina seu perfil ou suas metas para ver os alvos diários aqui.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        val metabolicResult = uiState.metabolicResult
        if (metabolicResult != null) {
            item {
                EqualHeightStatGrid(modifier = Modifier.fillMaxWidth()) {
                    StatCard(
                        label = "TMB",
                        value = "${metabolicResult.bmr} kcal",
                        caption = "Taxa metabólica basal"
                    )
                    StatCard(
                        label = "Água",
                        value = "${metabolicResult.waterGoalMl} ml",
                        caption = "Meta diária de hidratação"
                    )
                    StatCard(
                        label = "Volume hoje",
                        value = "${uiState.volumeToday.roundToInt()} kg",
                        caption = "Tonelagem levantada"
                    )
                    StatCard(
                        label = "GET",
                        value = "${metabolicResult.tdee} kcal",
                        caption = "Gasto energético total"
                    )
                }
            }
        } else {
            item {
                StatCard(
                    label = "Volume hoje",
                    value = "${uiState.volumeToday.roundToInt()} kg",
                    caption = "Tonelagem levantada",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        item {
            SectionHeader(title = "Treino")
        }

        item {
            Button(
                onClick = { onStartOrResumeWorkout(uiState.activeSession?.id) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = null)
                Text(
                    text = if (uiState.activeSession != null) "Continuar treino em andamento" else "Iniciar novo treino",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

/**
 * Lays out its children in a 2-column grid where every cell shares the height of the tallest
 * child — e.g. TMB/Água/Volume hoje/GET, whose captions wrap to different line counts and would
 * otherwise end up with mismatched card heights.
 */
@Composable
private fun EqualHeightStatGrid(
    modifier: Modifier = Modifier,
    horizontalSpacing: Dp = 14.dp,
    verticalSpacing: Dp = 14.dp,
    content: @Composable () -> Unit
) {
    Layout(content = content, modifier = modifier) { measurables, constraints ->
        val columns = 2
        val hSpacingPx = horizontalSpacing.roundToPx()
        val vSpacingPx = verticalSpacing.roundToPx()
        val columnWidth = (constraints.maxWidth - hSpacingPx * (columns - 1)) / columns
        val maxHeight = measurables.maxOf { it.maxIntrinsicHeight(columnWidth) }
        val cellConstraints = Constraints(
            minWidth = columnWidth,
            maxWidth = columnWidth,
            minHeight = maxHeight,
            maxHeight = maxHeight
        )
        val placeables = measurables.map { it.measure(cellConstraints) }
        val rows = (placeables.size + columns - 1) / columns
        val totalHeight = maxHeight * rows + vSpacingPx * (rows - 1)

        layout(constraints.maxWidth, totalHeight) {
            placeables.forEachIndexed { index, placeable ->
                val row = index / columns
                val col = index % columns
                val x = col * (columnWidth + hSpacingPx)
                val y = row * (maxHeight + vSpacingPx)
                placeable.placeRelative(x, y)
            }
        }
    }
}

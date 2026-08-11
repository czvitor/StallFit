package com.vitorsousa.stallfit.ui.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.vitorsousa.stallfit.di.AppViewModelProvider
import com.vitorsousa.stallfit.ui.components.CalorieRing
import com.vitorsousa.stallfit.ui.components.MacroProgressBar
import com.vitorsousa.stallfit.ui.components.SectionHeader
import com.vitorsousa.stallfit.ui.components.StatCard
import com.vitorsousa.stallfit.ui.theme.CarbColor
import com.vitorsousa.stallfit.ui.theme.FatColor
import com.vitorsousa.stallfit.ui.theme.ProteinColor
import kotlin.math.roundToInt

@Composable
fun DashboardScreen(
    onStartOrResumeWorkout: (sessionId: Long?) -> Unit,
    onOpenNutrition: () -> Unit,
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
            Column {
                Text(
                    text = "StällFit",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Resumo de hoje",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
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
                        consumed = uiState.macroTotals.calories,
                        goal = uiState.macroGoal?.calorieGoal ?: 0
                    )
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        MacroProgressBar(
                            name = "Proteína",
                            consumed = uiState.macroTotals.protein,
                            goal = uiState.macroGoal?.proteinGoal ?: 0,
                            unit = "g",
                            color = ProteinColor
                        )
                        MacroProgressBar(
                            name = "Carboidratos",
                            consumed = uiState.macroTotals.carbs,
                            goal = uiState.macroGoal?.carbGoal ?: 0,
                            unit = "g",
                            color = CarbColor
                        )
                        MacroProgressBar(
                            name = "Gorduras",
                            consumed = uiState.macroTotals.fat,
                            goal = uiState.macroGoal?.fatGoal ?: 0,
                            unit = "g",
                            color = FatColor
                        )
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                StatCard(
                    label = "Volume hoje",
                    value = "${uiState.volumeToday.roundToInt()} kg",
                    caption = "Tonelagem levantada",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Refeições",
                    value = "${uiState.macroTotals.caloriesRounded}",
                    caption = "kcal registradas hoje",
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onOpenNutrition)
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

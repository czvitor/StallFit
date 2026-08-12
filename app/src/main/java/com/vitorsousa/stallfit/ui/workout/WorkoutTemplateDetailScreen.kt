package com.vitorsousa.stallfit.ui.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vitorsousa.stallfit.data.local.relation.TemplateExerciseWithExercise
import com.vitorsousa.stallfit.di.AppViewModelProvider
import com.vitorsousa.stallfit.ui.components.EmptyState

@Composable
fun WorkoutTemplateDetailScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WorkoutTemplateDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val isFinished = uiState.session?.finishedAt != null

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
            }
            Text(
                text = uiState.template?.title ?: "Ficha de treino",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (uiState.exercises.isEmpty()) {
                item { EmptyState(message = "Esta ficha ainda não tem exercícios.") }
            } else {
                items(uiState.exercises, key = { it.templateExercise.id }) { exercise ->
                    TemplateExerciseRow(
                        exercise = exercise,
                        enabled = !isFinished,
                        onSaveRegistro = { weight -> viewModel.saveRegistro(exercise, weight) }
                    )
                }
            }

            if (!isFinished) {
                item {
                    Button(
                        onClick = {
                            viewModel.finishWorkout(onBack)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Concluir treino")
                    }
                }
            }
        }
    }
}

@Composable
private fun TemplateExerciseRow(
    exercise: TemplateExerciseWithExercise,
    enabled: Boolean,
    onSaveRegistro: (weightKg: Double) -> Unit
) {
    var weightInput by rememberSaveable(exercise.templateExercise.id) { mutableStateOf("") }
    val weight = weightInput.replace(',', '.').toDoubleOrNull()

    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = exercise.exerciseName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${exercise.templateExercise.sets}x ${exercise.templateExercise.repRangeMin}-${exercise.templateExercise.repRangeMax} reps · " +
                    "${exercise.templateExercise.intensity.label} · Descanso ${exercise.templateExercise.restSeconds}s",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (enabled) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = weightInput,
                        onValueChange = { input ->
                            weightInput = input.filter { c -> c.isDigit() || c == '.' || c == ',' }
                        },
                        label = { Text("Carga (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = {
                            weight?.let {
                                onSaveRegistro(it)
                                weightInput = ""
                            }
                        },
                        enabled = weight != null && weight >= 0
                    ) {
                        Text("Salvar Registro")
                    }
                }
            }
        }
    }
}

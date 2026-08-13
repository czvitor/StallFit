package com.vitorsousa.stallfit.ui.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.vitorsousa.stallfit.di.AppViewModelProvider
import com.vitorsousa.stallfit.ui.components.EmptyState
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveWorkoutScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ActiveWorkoutViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val exercises by viewModel.exercises.collectAsState()
    val selectedExerciseId by viewModel.selectedExerciseId.collectAsState()
    val lastSet by viewModel.lastSetForSelected.collectAsState()
    val bestSet by viewModel.bestSetForSelected.collectAsState()

    var exerciseMenuExpanded by rememberSaveable { mutableStateOf(false) }
    var showAddExerciseDialog by rememberSaveable { mutableStateOf(false) }
    var repsInput by rememberSaveable { mutableStateOf("") }
    var weightInput by rememberSaveable { mutableStateOf("") }

    val selectedExercise = exercises.firstOrNull { it.id == selectedExerciseId }

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
            Column(modifier = Modifier.padding(start = 4.dp)) {
                Text(
                    text = uiState.session?.name ?: "Treino",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = if (uiState.isFinished) "Finalizado · ${uiState.volume.roundToInt()} kg" else "Em andamento · ${uiState.volume.roundToInt()} kg",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (!uiState.isFinished) {
                item {
                    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                ExposedDropdownMenuBox(
                                    expanded = exerciseMenuExpanded,
                                    onExpandedChange = { exerciseMenuExpanded = it },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    OutlinedTextField(
                                        value = selectedExercise?.name ?: "Selecionar exercício",
                                        onValueChange = {},
                                        readOnly = true,
                                        label = { Text("Exercício") },
                                        trailingIcon = {
                                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = exerciseMenuExpanded)
                                        },
                                        modifier = Modifier
                                            .menuAnchor()
                                            .fillMaxWidth()
                                    )
                                    ExposedDropdownMenu(
                                        expanded = exerciseMenuExpanded,
                                        onDismissRequest = { exerciseMenuExpanded = false }
                                    ) {
                                        exercises.forEach { exercise ->
                                            DropdownMenuItem(
                                                text = { Text("${exercise.name} · ${exercise.muscleGroup}") },
                                                onClick = {
                                                    viewModel.selectExercise(exercise.id)
                                                    exerciseMenuExpanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                                IconButton(onClick = { showAddExerciseDialog = true }) {
                                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Novo exercício")
                                }
                            }

                            if (selectedExercise != null) {
                                Text(
                                    text = if (lastSet != null) {
                                        "Último registro: ${formatWeight(lastSet!!.weightKg)} kg × ${lastSet!!.reps} reps"
                                    } else {
                                        "Nenhum histórico ainda para este exercício"
                                    },
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                if (bestSet != null) {
                                    Text(
                                        text = "Recorde pessoal: ${formatWeight(bestSet!!.weightKg)} kg × ${bestSet!!.reps} reps",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.tertiary
                                    )
                                }
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = repsInput,
                                    onValueChange = { repsInput = it.filter { c -> c.isDigit() } },
                                    label = { Text("Reps") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = weightInput,
                                    onValueChange = { input ->
                                        weightInput = input.filter { c -> c.isDigit() || c == '.' || c == ',' }
                                    },
                                    label = { Text("Carga (kg)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Button(
                                onClick = {
                                    val reps = repsInput.toIntOrNull()
                                    val weight = weightInput.replace(',', '.').toDoubleOrNull()
                                    if (reps != null && reps > 0 && weight != null && weight >= 0 && selectedExerciseId != null) {
                                        viewModel.logSet(reps, weight)
                                        repsInput = ""
                                        weightInput = ""
                                    }
                                },
                                enabled = selectedExerciseId != null && repsInput.isNotBlank() && weightInput.isNotBlank(),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                                Text(text = "Registrar série", modifier = Modifier.padding(start = 8.dp))
                            }
                        }
                    }
                }

                item { RestTimerCard() }
            }

            item {
                Text(
                    text = "Séries registradas",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            if (uiState.sets.isEmpty()) {
                item { EmptyState(message = "Nenhuma série registrada ainda.") }
            } else {
                items(uiState.sets.sortedByDescending { it.setEntry.loggedAt }, key = { it.setEntry.id }) { entry ->
                    SetEntryRow(
                        exerciseName = entry.exerciseName,
                        setNumber = entry.setEntry.setNumber,
                        reps = entry.setEntry.reps,
                        weightKg = entry.setEntry.weightKg,
                        onDelete = if (!uiState.isFinished) {
                            { viewModel.deleteSet(entry.setEntry) }
                        } else null
                    )
                }
            }

            if (!uiState.isFinished) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = {
                                viewModel.finishSession()
                                onBack()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Finalizar treino")
                        }
                        OutlinedButton(
                            onClick = {
                                viewModel.discardSession()
                                onBack()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Descartar treino")
                        }
                    }
                }
            }
        }
    }

    if (showAddExerciseDialog) {
        AddExerciseDialog(
            onDismiss = { showAddExerciseDialog = false },
            onConfirm = { name, muscleGroup ->
                viewModel.addCustomExercise(name, muscleGroup)
                showAddExerciseDialog = false
            }
        )
    }
}

@Composable
private fun SetEntryRow(
    exerciseName: String,
    setNumber: Int,
    reps: Int,
    weightKg: Double,
    onDelete: (() -> Unit)?
) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = exerciseName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Série $setNumber · ${formatWeight(weightKg)} kg × $reps reps",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (onDelete != null) {
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Excluir série",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun AddExerciseDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, muscleGroup: String) -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var muscleGroup by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Novo exercício") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome do exercício") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = muscleGroup,
                    onValueChange = { muscleGroup = it },
                    label = { Text("Grupo muscular") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(name.trim(), muscleGroup.trim().ifBlank { "Outro" }) },
                enabled = name.isNotBlank()
            ) {
                Text("Adicionar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                Text(text = "Cancelar", modifier = Modifier.padding(start = 4.dp))
            }
        }
    )
}

private fun formatWeight(weightKg: Double): String =
    if (weightKg == weightKg.roundToInt().toDouble()) {
        weightKg.roundToInt().toString()
    } else {
        "%.1f".format(weightKg)
    }

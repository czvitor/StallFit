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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.vitorsousa.stallfit.data.local.entity.Equipment
import com.vitorsousa.stallfit.data.local.entity.Intensity
import com.vitorsousa.stallfit.di.AppViewModelProvider
import com.vitorsousa.stallfit.ui.components.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateWorkoutScreen(
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateWorkoutViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    var title by rememberSaveable { mutableStateOf("") }
    var exerciseMenuExpanded by rememberSaveable { mutableStateOf(false) }
    var selectedExerciseId by rememberSaveable { mutableStateOf<Long?>(null) }
    var showCustomExerciseDialog by rememberSaveable { mutableStateOf(false) }

    val selectedExercise = uiState.exercises.firstOrNull { it.id == selectedExerciseId }
    val isValid = title.isNotBlank() && uiState.draftRows.isNotEmpty()

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onDone) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Voltar")
            }
            Text(
                text = "Criar treino",
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
            item {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Nome do treino") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
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
                            uiState.exercises.forEach { exercise ->
                                DropdownMenuItem(
                                    text = { Text("${exercise.name} · ${exercise.muscleGroup}") },
                                    onClick = {
                                        selectedExerciseId = exercise.id
                                        exerciseMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    IconButton(onClick = { showCustomExerciseDialog = true }) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Novo exercício")
                    }
                }
            }

            item {
                Button(
                    onClick = {
                        selectedExercise?.let { viewModel.addExerciseRow(it) }
                        selectedExerciseId = null
                    },
                    enabled = selectedExercise != null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Adicionar exercício à ficha")
                }
            }

            item {
                Text(
                    text = "Exercícios da ficha",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            if (uiState.draftRows.isEmpty()) {
                item { EmptyState(message = "Adicione exercícios para montar a ficha.") }
            } else {
                items(uiState.draftRows.size) { index ->
                    DraftExerciseCard(
                        row = uiState.draftRows[index],
                        onChange = { updated -> viewModel.updateRow(index, updated) },
                        onDelete = { viewModel.removeExerciseRow(index) }
                    )
                }
            }

            item {
                Button(
                    onClick = { viewModel.saveTemplate(title, onDone) },
                    enabled = isValid,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Salvar ficha")
                }
            }
        }
    }

    if (showCustomExerciseDialog) {
        CustomExerciseDialog(
            onDismiss = { showCustomExerciseDialog = false },
            onConfirm = { name, muscleGroup, equipment ->
                viewModel.addCustomExercise(name, muscleGroup, equipment) { created ->
                    selectedExerciseId = created.id
                }
                showCustomExerciseDialog = false
            }
        )
    }
}

@Composable
private fun DraftExerciseCard(
    row: DraftExerciseRow,
    onChange: (DraftExerciseRow) -> Unit,
    onDelete: () -> Unit
) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = row.exerciseName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Remover",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                NumberField(
                    value = row.sets,
                    label = "Séries",
                    onChange = { onChange(row.copy(sets = it)) },
                    modifier = Modifier.weight(1f)
                )
                NumberField(
                    value = row.repRangeMin,
                    label = "Reps mín.",
                    onChange = { onChange(row.copy(repRangeMin = it)) },
                    modifier = Modifier.weight(1f)
                )
                NumberField(
                    value = row.repRangeMax,
                    label = "Reps máx.",
                    onChange = { onChange(row.copy(repRangeMax = it)) },
                    modifier = Modifier.weight(1f)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                NumberField(
                    value = row.restSeconds,
                    label = "Descanso (s)",
                    onChange = { onChange(row.copy(restSeconds = it)) },
                    modifier = Modifier.weight(1f)
                )
                IntensityDropdown(
                    selected = row.intensity,
                    onSelected = { onChange(row.copy(intensity = it)) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun NumberField(
    value: Int,
    label: String,
    onChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var text by rememberSaveable(value) { mutableStateOf(value.toString()) }
    OutlinedTextField(
        value = text,
        onValueChange = { input ->
            text = input.filter { it.isDigit() }
            text.toIntOrNull()?.let(onChange)
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IntensityDropdown(
    selected: Intensity,
    onSelected: (Intensity) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selected.label,
            onValueChange = {},
            readOnly = true,
            label = { Text("Intensidade") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            Intensity.entries.forEach { intensity ->
                DropdownMenuItem(
                    text = { Text(intensity.label) },
                    onClick = {
                        onSelected(intensity)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun CustomExerciseDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, muscleGroup: String, equipment: Equipment) -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var muscleGroup by rememberSaveable { mutableStateOf("") }
    var equipment by rememberSaveable { mutableStateOf(Equipment.NONE) }
    var equipmentMenuExpanded by rememberSaveable { mutableStateOf(false) }

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
                EquipmentDropdown(
                    expanded = equipmentMenuExpanded,
                    onExpandedChange = { equipmentMenuExpanded = it },
                    selected = equipment,
                    onSelected = { equipment = it }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(name.trim(), muscleGroup.trim().ifBlank { "Outro" }, equipment) },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EquipmentDropdown(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    selected: Equipment,
    onSelected: (Equipment) -> Unit
) {
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = onExpandedChange) {
        OutlinedTextField(
            value = selected.label,
            onValueChange = {},
            readOnly = true,
            label = { Text("Equipamento") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { onExpandedChange(false) }) {
            Equipment.entries.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.label) },
                    onClick = {
                        onSelected(option)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}

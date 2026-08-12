package com.vitorsousa.stallfit.ui.workout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vitorsousa.stallfit.data.local.entity.Equipment
import com.vitorsousa.stallfit.data.local.entity.ExerciseEntity
import com.vitorsousa.stallfit.data.local.entity.Intensity
import com.vitorsousa.stallfit.di.AppViewModelProvider
import com.vitorsousa.stallfit.ui.components.EmptyState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CreateWorkoutScreen(
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateWorkoutViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    var title by rememberSaveable { mutableStateOf("") }
    var query by rememberSaveable { mutableStateOf("") }
    var showCustomExerciseDialog by rememberSaveable { mutableStateOf(false) }

    val filteredExercises = if (query.isBlank()) {
        uiState.exercises
    } else {
        uiState.exercises.filter { it.name.contains(query, ignoreCase = true) }
    }
    val addedExerciseIds = uiState.draftRows.map { it.exerciseId }.toSet()
    val isValid = title.isNotBlank() && uiState.draftRows.isNotEmpty()

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    // A focused field (e.g. "Nome do treino") keeps asking the list to scroll itself back into
    // view above the keyboard, which fights the user's manual scroll and breaks the sticky
    // search header. Dismissing focus as soon as a drag starts removes that fight.
    LaunchedEffect(listState.isScrollInProgress) {
        if (listState.isScrollInProgress) {
            focusManager.clearFocus()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
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
                state = listState,
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

                item {
                    Text(
                        text = "Adicionar exercícios",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                stickyHeader {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(bottom = 12.dp)
                    ) {
                        OutlinedTextField(
                            value = query,
                            onValueChange = { query = it },
                            label = { Text("Buscar exercício") },
                            leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = null) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                item {
                    OutlinedButton(
                        onClick = { showCustomExerciseDialog = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                        Text(text = "Criar exercício personalizado", modifier = Modifier.padding(start = 8.dp))
                    }
                }

                if (filteredExercises.isEmpty()) {
                    item { EmptyState(message = "Nenhum exercício encontrado.") }
                } else {
                    items(filteredExercises, key = { it.id }) { exercise ->
                        ExerciseRow(
                            exercise = exercise,
                            isSelected = exercise.id in addedExerciseIds,
                            onClick = { viewModel.addExerciseRow(exercise) }
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = listState.canScrollBackward,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            FloatingActionButton(
                onClick = { coroutineScope.launch { listState.animateScrollToItem(0) } }
            ) {
                Icon(imageVector = Icons.Filled.KeyboardArrowUp, contentDescription = "Voltar ao topo")
            }
        }
    }

    if (showCustomExerciseDialog) {
        CustomExerciseDialog(
            onDismiss = { showCustomExerciseDialog = false },
            onConfirm = { name, muscleGroup, equipment ->
                viewModel.addCustomExercise(name, muscleGroup, equipment) { created ->
                    viewModel.addExerciseRow(created)
                }
                showCustomExerciseDialog = false
            }
        )
    }
}

@Composable
private fun ExerciseRow(exercise: ExerciseEntity, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        border = if (isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${exercise.muscleGroup} · ${exercise.equipment.label}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (isSelected) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Adicionado",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
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

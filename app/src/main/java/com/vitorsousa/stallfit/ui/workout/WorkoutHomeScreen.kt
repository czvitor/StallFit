package com.vitorsousa.stallfit.ui.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vitorsousa.stallfit.core.util.DateUtils
import com.vitorsousa.stallfit.data.local.entity.WorkoutSessionEntity
import com.vitorsousa.stallfit.data.local.relation.TemplateWithExercises
import com.vitorsousa.stallfit.di.AppViewModelProvider
import com.vitorsousa.stallfit.ui.components.AppCard
import com.vitorsousa.stallfit.ui.components.EmptyState
import com.vitorsousa.stallfit.ui.components.SectionHeader
import com.vitorsousa.stallfit.ui.components.StatCard
import kotlin.math.roundToInt

@Composable
fun WorkoutHomeScreen(
    onOpenSession: (Long) -> Unit,
    onOpenTemplate: (Long) -> Unit,
    onCreateWorkout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WorkoutHomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    var showStartChoiceDialog by rememberSaveable { mutableStateOf(false) }
    var showTemplatePickerDialog by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            StatCard(
                label = "Volume da semana",
                value = "${uiState.volumeThisWeek.roundToInt()} kg",
                caption = "Tonelagem total desde segunda-feira"
            )
        }

        item {
            if (uiState.activeSession != null) {
                Button(
                    onClick = { onOpenSession(uiState.activeSession!!.id) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Filled.FitnessCenter, contentDescription = null)
                    Text(text = "Continuar treino em andamento", modifier = Modifier.padding(start = 8.dp))
                }
            } else {
                Button(
                    onClick = { showStartChoiceDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                    Text(text = "Iniciar treino", modifier = Modifier.padding(start = 8.dp))
                }
            }
        }

        item {
            SectionHeader(title = "Meus treinos", actionLabel = "Criar treino", onActionClick = onCreateWorkout)
        }

        if (uiState.templates.isEmpty()) {
            item { EmptyState(message = "Crie uma ficha de treino reutilizável.") }
        } else {
            items(uiState.templates, key = { it.template.id }) { template ->
                TemplateRow(template = template, onClick = { onOpenTemplate(template.template.id) })
            }
        }

        item {
            SectionHeader(title = "Histórico")
        }

        if (uiState.completedSessions.isEmpty()) {
            item { EmptyState(message = "Seus treinos concluídos vão aparecer aqui.") }
        } else {
            items(uiState.completedSessions, key = { it.id }) { session ->
                SessionHistoryRow(session = session, onClick = { onOpenSession(session.id) })
            }
        }
    }

    if (showStartChoiceDialog) {
        StartWorkoutChoiceDialog(
            hasTemplates = uiState.templates.isNotEmpty(),
            onDismiss = { showStartChoiceDialog = false },
            onFreeform = {
                showStartChoiceDialog = false
                viewModel.startNewSession(onOpenSession)
            },
            onFromTemplate = {
                showStartChoiceDialog = false
                if (uiState.templates.isEmpty()) onCreateWorkout() else showTemplatePickerDialog = true
            }
        )
    }

    if (showTemplatePickerDialog) {
        TemplatePickerDialog(
            templates = uiState.templates,
            onDismiss = { showTemplatePickerDialog = false },
            onTemplateSelected = { templateId ->
                showTemplatePickerDialog = false
                onOpenTemplate(templateId)
            }
        )
    }
}

@Composable
private fun StartWorkoutChoiceDialog(
    hasTemplates: Boolean,
    onDismiss: () -> Unit,
    onFreeform: () -> Unit,
    onFromTemplate: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Como você quer treinar?") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                StartOptionRow(
                    icon = Icons.Filled.Bolt,
                    title = "Treino livre",
                    description = "Escolha exercícios e registre séries na hora, sem estrutura fixa.",
                    onClick = onFreeform
                )
                StartOptionRow(
                    icon = Icons.AutoMirrored.Filled.Assignment,
                    title = "A partir de uma ficha",
                    description = if (hasTemplates) {
                        "Use uma ficha pré-montada com exercícios e séries definidos."
                    } else {
                        "Crie sua primeira ficha para treinar com uma estrutura fixa."
                    },
                    onClick = onFromTemplate
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
private fun StartOptionRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    AppCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun TemplatePickerDialog(
    templates: List<TemplateWithExercises>,
    onDismiss: () -> Unit,
    onTemplateSelected: (Long) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Escolha uma ficha") },
        text = {
            Column(
                modifier = Modifier
                    .heightIn(max = 400.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                templates.forEach { template ->
                    TemplateRow(
                        template = template,
                        onClick = { onTemplateSelected(template.template.id) }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
private fun TemplateRow(template: TemplateWithExercises, onClick: () -> Unit) {
    AppCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp)
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
                    text = template.template.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${template.exercises.size} exercícios",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SessionHistoryRow(session: WorkoutSessionEntity, onClick: () -> Unit) {
    AppCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp)
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
                    text = session.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = DateUtils.formatTime(session.startedAt),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

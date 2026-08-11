package com.vitorsousa.stallfit.ui.workout

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FitnessCenter
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
import com.vitorsousa.stallfit.core.util.DateUtils
import com.vitorsousa.stallfit.data.local.entity.WorkoutSessionEntity
import com.vitorsousa.stallfit.data.local.relation.TemplateWithExercises
import com.vitorsousa.stallfit.di.AppViewModelProvider
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

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Treino",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

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
                    onClick = { viewModel.startNewSession(onOpenSession) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                    Text(text = "Iniciar novo treino", modifier = Modifier.padding(start = 8.dp))
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
}

@Composable
private fun TemplateRow(template: TemplateWithExercises, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
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

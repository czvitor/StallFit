package com.vitorsousa.stallfit.ui.workout

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vitorsousa.stallfit.data.local.relation.TemplateExerciseWithExercise
import com.vitorsousa.stallfit.data.pdf.WorkoutPdfGenerator
import com.vitorsousa.stallfit.di.AppViewModelProvider
import com.vitorsousa.stallfit.ui.components.EmptyState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun WorkoutTemplateDetailScreen(
    onBack: () -> Unit,
    onStartSession: (sessionId: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WorkoutTemplateDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var showDeleteDialog by remember { mutableStateOf(false) }

    val pdfExportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf")
    ) { uri ->
        val template = uiState.template
        if (uri != null && template != null) {
            coroutineScope.launch {
                val writeResult = runCatching {
                    withContext(Dispatchers.IO) {
                        val bytes = WorkoutPdfGenerator.generate(
                            context = context,
                            template = template,
                            exercises = uiState.exercises,
                            studentName = uiState.studentName
                        )
                        context.contentResolver.openOutputStream(uri)?.use { stream ->
                            stream.write(bytes)
                        } != null
                    }
                }
                writeResult.onSuccess { wrote ->
                    if (wrote) {
                        Toast.makeText(context, "PDF exportado com sucesso.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Falha ao exportar PDF: não foi possível abrir o arquivo.", Toast.LENGTH_LONG).show()
                    }
                }.onFailure { error ->
                    Toast.makeText(context, "Falha ao exportar PDF: ${error.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

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
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { pdfExportLauncher.launch(pdfFileName(uiState.template?.title)) }) {
                Icon(imageVector = Icons.Filled.PictureAsPdf, contentDescription = "Baixar treino em PDF")
            }
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Excluir treino",
                    tint = MaterialTheme.colorScheme.error
                )
            }
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
                        onSaveReferenceWeight = { weight ->
                            viewModel.updateReferenceWeight(exercise.templateExercise.id, weight)
                        }
                    )
                }
            }

            item {
                NotesCard(
                    notes = uiState.template?.notes.orEmpty(),
                    onSaveNotes = { notes -> viewModel.updateNotes(notes) }
                )
            }

            item {
                Button(
                    onClick = { viewModel.startSession(onStarted = onStartSession) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = null)
                    Text(text = " Iniciar treino a partir desta ficha")
                }
            }
        }
    }

    if (showDeleteDialog) {
        DeleteTemplateConfirmationDialog(
            templateName = uiState.template?.title.orEmpty(),
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                showDeleteDialog = false
                viewModel.deleteTemplate(onDeleted = onBack)
            }
        )
    }
}

private fun pdfFileName(title: String?): String {
    val slug = title.orEmpty()
        .lowercase()
        .replace(Regex("[^a-z0-9]+"), "-")
        .trim('-')
        .ifBlank { "ficha" }
    return "ficha-$slug.pdf"
}

@Composable
private fun DeleteTemplateConfirmationDialog(
    templateName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Excluir treino?") },
        text = {
            Text("Tem certeza que deseja excluir o treino \"$templateName\"? Esta ação não poderá ser desfeita.")
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Confirmar Exclusão", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun TemplateExerciseRow(
    exercise: TemplateExerciseWithExercise,
    onSaveReferenceWeight: (weightKg: Double?) -> Unit
) {
    var isEditing by rememberSaveable(exercise.templateExercise.id) { mutableStateOf(false) }
    var weightInput by rememberSaveable(exercise.templateExercise.id) {
        mutableStateOf(exercise.templateExercise.referenceWeightKg?.let(::formatWeight).orEmpty())
    }

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

            if (isEditing) {
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
                        label = { Text("Carga de referência (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = {
                            val weight = weightInput.replace(',', '.').toDoubleOrNull()
                            onSaveReferenceWeight(weight)
                            isEditing = false
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.Check, contentDescription = "Salvar carga")
                    }
                    IconButton(
                        onClick = {
                            weightInput = exercise.templateExercise.referenceWeightKg?.let(::formatWeight).orEmpty()
                            isEditing = false
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Cancelar edição")
                    }
                }
            } else {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val referenceWeight = exercise.templateExercise.referenceWeightKg
                    Text(
                        text = if (referenceWeight != null) {
                            "Carga de referência: ${formatWeight(referenceWeight)} kg"
                        } else {
                            "Carga de referência: não definida"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { isEditing = true }) {
                        Icon(imageVector = Icons.Filled.Edit, contentDescription = "Editar carga")
                    }
                }
            }
        }
    }
}

@Composable
private fun NotesCard(
    notes: String,
    onSaveNotes: (String) -> Unit
) {
    var isEditing by rememberSaveable { mutableStateOf(false) }
    var notesInput by rememberSaveable(notes) { mutableStateOf(notes) }

    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Observações",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                if (!isEditing) {
                    IconButton(onClick = { isEditing = true }) {
                        Icon(imageVector = Icons.Filled.Edit, contentDescription = "Editar observações")
                    }
                }
            }

            if (isEditing) {
                OutlinedTextField(
                    value = notesInput,
                    onValueChange = { notesInput = it },
                    placeholder = { Text("Observações sobre esta ficha (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(onClick = { onSaveNotes(notesInput); isEditing = false }) {
                        Text("Salvar")
                    }
                    TextButton(onClick = { notesInput = notes; isEditing = false }) {
                        Text("Cancelar")
                    }
                }
            } else {
                Text(
                    text = notes.ifBlank { "Nenhuma observação registrada." },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun formatWeight(value: Double): String =
    if (value == value.toLong().toDouble()) value.toLong().toString() else value.toString()

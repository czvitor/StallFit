package com.vitorsousa.stallfit.ui.profile

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vitorsousa.stallfit.data.backup.BackupEnvelope
import com.vitorsousa.stallfit.data.backup.BackupModule
import com.vitorsousa.stallfit.data.backup.backupFileName
import com.vitorsousa.stallfit.data.local.entity.ActivityLevel
import com.vitorsousa.stallfit.data.local.entity.BiologicalSex
import com.vitorsousa.stallfit.data.local.entity.BodyMeasurementEntity
import com.vitorsousa.stallfit.data.local.entity.NutritionGoal
import com.vitorsousa.stallfit.di.AppViewModelProvider
import com.vitorsousa.stallfit.ui.components.AppCard
import com.vitorsousa.stallfit.ui.components.EmptyState
import com.vitorsousa.stallfit.ui.components.LineChart
import com.vitorsousa.stallfit.ui.components.LineChartPoint
import com.vitorsousa.stallfit.ui.components.StatCard
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private fun decimal(value: String) = value.replace(',', '.').toDoubleOrNull()

private fun formatNumber(value: Double): String =
    if (value == value.toLong().toDouble()) value.toLong().toString() else "%.1f".format(value)

private fun formatDate(epochMillis: Long): String =
    Instant.ofEpochMilli(epochMillis)
        .atZone(ZoneId.systemDefault())
        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var showExportDialog by remember { mutableStateOf(false) }
    var pendingExportModules by remember { mutableStateOf(setOf<BackupModule>()) }

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        if (uri != null) {
            viewModel.exportBackup(pendingExportModules) { result ->
                result.onSuccess { jsonString ->
                    coroutineScope.launch {
                        val writeResult = runCatching {
                            withContext(Dispatchers.IO) {
                                context.contentResolver.openOutputStream(uri)?.use { stream ->
                                    stream.write(jsonString.toByteArray())
                                } != null
                            }
                        }
                        writeResult.onSuccess { wrote ->
                            if (wrote) {
                                Toast.makeText(context, "Backup exportado com sucesso.", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Falha ao exportar backup: não foi possível abrir o arquivo.", Toast.LENGTH_LONG).show()
                            }
                        }.onFailure { error ->
                            Toast.makeText(context, "Falha ao exportar backup: ${error.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }.onFailure { error ->
                    Toast.makeText(context, "Falha ao exportar backup: ${error.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    var pendingImportEnvelope by remember { mutableStateOf<BackupEnvelope?>(null) }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            coroutineScope.launch {
                val readResult = runCatching {
                    withContext(Dispatchers.IO) {
                        context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { reader -> reader.readText() }
                    }
                }
                readResult.onSuccess { jsonString ->
                    if (jsonString != null) {
                        viewModel.inspectBackup(jsonString) { result ->
                            result.onSuccess { envelope ->
                                pendingImportEnvelope = envelope
                            }.onFailure { error ->
                                Toast.makeText(context, "Arquivo de backup inválido: ${error.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Falha ao importar backup: não foi possível ler o arquivo.", Toast.LENGTH_LONG).show()
                    }
                }.onFailure { error ->
                    Toast.makeText(context, "Falha ao importar backup: ${error.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    var nameInput by rememberSaveable { mutableStateOf("") }
    var ageInput by rememberSaveable { mutableStateOf("") }
    var sex by rememberSaveable { mutableStateOf(BiologicalSex.MALE) }
    var activityLevel by rememberSaveable { mutableStateOf(ActivityLevel.SEDENTARY) }
    var goal by rememberSaveable { mutableStateOf(NutritionGoal.MAINTENANCE) }
    var initialized by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(uiState.profile) {
        val current = uiState.profile
        if (!initialized && current != null) {
            nameInput = current.name
            ageInput = current.ageYears.toString()
            sex = current.sex
            activityLevel = current.activityLevel
            goal = current.goal
            initialized = true
        }
    }

    var showMeasurementDialog by rememberSaveable { mutableStateOf(false) }
    var selectedMeasurementId by rememberSaveable { mutableStateOf<Long?>(null) }

    val ageValue = ageInput.toIntOrNull()
    val isProfileValid = ageValue != null && ageValue > 0

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Dados gerais do perfil. Peso, altura e demais medidas ficam no histórico de Evolução Física abaixo, e a avaliação mais recente alimenta automaticamente o cálculo da sua TMB, GET e macros.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
            value = nameInput,
            onValueChange = { nameInput = it },
            label = { Text("Nome") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = ageInput,
            onValueChange = { input -> ageInput = input.filter { it.isDigit() } },
            label = { Text("Idade") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        EnumDropdown(
            label = "Sexo biológico",
            selected = sex,
            options = BiologicalSex.entries,
            optionLabel = { it.label },
            onSelected = { sex = it }
        )

        EnumDropdown(
            label = "Nível de atividade",
            selected = activityLevel,
            options = ActivityLevel.entries,
            optionLabel = { it.label },
            onSelected = { activityLevel = it }
        )

        EnumDropdown(
            label = "Objetivo",
            selected = goal,
            options = NutritionGoal.entries,
            optionLabel = { it.label },
            onSelected = { goal = it }
        )

        Button(
            onClick = {
                if (isProfileValid) {
                    viewModel.saveProfile(
                        name = nameInput.trim(),
                        ageYears = ageValue!!,
                        sex = sex,
                        activityLevel = activityLevel,
                        goal = goal
                    )
                }
            },
            enabled = isProfileValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar perfil")
        }

        Text(
            text = "Evolução Física",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Button(
            onClick = { showMeasurementDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Adicionar Medidas / Avaliação Física")
        }

        if (uiState.measurementHistory.isEmpty()) {
            EmptyState(message = "Nenhuma avaliação registrada ainda. Adicione a primeira para começar a acompanhar sua evolução.")
        } else {
            if (uiState.measurementHistory.size >= 2) {
                AppCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Evolução do peso",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        LineChart(
                            points = uiState.measurementHistory.reversed().map { measurement ->
                                LineChartPoint(
                                    value = measurement.weightKg.toFloat(),
                                    label = formatDate(measurement.createdAt).substring(0, 5)
                                )
                            },
                            valueFormatter = { value -> "${formatNumber(value.toDouble())} kg" },
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                uiState.measurementHistory.forEach { measurement ->
                    MeasurementHistoryCard(
                        measurement = measurement,
                        onClick = { selectedMeasurementId = measurement.id }
                    )
                }
            }
        }

        val result = uiState.result
        if (result != null) {
            Text(
                text = "Resultado (com base na última avaliação)",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                StatCard(
                    label = "TMB",
                    value = "${result.bmr} kcal",
                    caption = "Taxa metabólica basal",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "GET",
                    value = "${result.tdee} kcal",
                    caption = "Gasto energético total",
                    modifier = Modifier.weight(1f)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                StatCard(
                    label = "Meta calórica",
                    value = "${result.calorieTarget} kcal",
                    caption = "Ajustada para o objetivo",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Água",
                    value = "${result.waterGoalMl} ml",
                    caption = "Meta diária de hidratação",
                    modifier = Modifier.weight(1f)
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Distribuição de macros",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Proteína: ${result.proteinGramsGoal} g",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Carboidratos: ${result.carbGramsGoal} g",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Gorduras: ${result.fatGramsGoal} g",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else if (uiState.measurementHistory.isEmpty()) {
            Text(
                text = "Adicione uma avaliação física para calcular sua TMB, GET e macros.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = "Backup e Restauração",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Escolha quais categorias exportar ou restaurar — tudo de uma vez ou por partes — em um arquivo .json.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { showExportDialog = true },
                modifier = Modifier.weight(1f)
            ) {
                Icon(imageVector = Icons.Filled.Download, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Exportar")
            }
            Button(
                onClick = { importLauncher.launch(arrayOf("application/json")) },
                modifier = Modifier.weight(1f)
            ) {
                Icon(imageVector = Icons.Filled.Upload, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Importar")
            }
        }
    }

    if (showMeasurementDialog) {
        AddMeasurementDialog(
            lastHeightCm = uiState.latestMeasurement?.heightCm,
            onDismiss = { showMeasurementDialog = false },
            onSave = { measurement ->
                viewModel.addMeasurement(measurement) { showMeasurementDialog = false }
            }
        )
    }

    val selectedMeasurement = uiState.measurementHistory.firstOrNull { it.id == selectedMeasurementId }
    if (selectedMeasurement != null) {
        MeasurementDetailDialog(
            measurement = selectedMeasurement,
            onDismiss = { selectedMeasurementId = null },
            onDelete = {
                viewModel.deleteMeasurement(selectedMeasurement) {
                    selectedMeasurementId = null
                }
            }
        )
    }

    if (showExportDialog) {
        ExportBackupDialog(
            onDismiss = { showExportDialog = false },
            onConfirm = { modules ->
                pendingExportModules = modules
                showExportDialog = false
                exportLauncher.launch(backupFileName(modules))
            }
        )
    }

    val envelopeToImport = pendingImportEnvelope
    if (envelopeToImport != null) {
        ImportBackupDialog(
            envelope = envelopeToImport,
            onDismiss = { pendingImportEnvelope = null },
            onConfirm = { selectedModules, strategy ->
                pendingImportEnvelope = null
                viewModel.importBackup(envelopeToImport, selectedModules, strategy) { result ->
                    result.onSuccess {
                        Toast.makeText(context, "Backup restaurado com sucesso.", Toast.LENGTH_SHORT).show()
                    }.onFailure { error ->
                        Toast.makeText(context, "Falha ao importar backup: ${error.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        )
    }
}

@Composable
private fun MeasurementHistoryCard(
    measurement: BodyMeasurementEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppCard(modifier = modifier.fillMaxWidth(), onClick = onClick) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = formatDate(measurement.createdAt),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "${formatNumber(measurement.weightKg)} kg",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${formatNumber(measurement.heightCm)} cm",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                measurement.bodyFatPercent?.let {
                    Text(
                        text = "${formatNumber(it)}% gordura",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun RowScope.MeasurementField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = { input -> onValueChange(input.filter { c -> c.isDigit() || c == '.' || c == ',' }) },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        singleLine = true,
        modifier = Modifier.weight(1f)
    )
}

@Composable
private fun CategoryLabel(text: String) {
    Text(text = text, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
}

@Composable
private fun SubgroupLabel(text: String) {
    Text(text = text, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddMeasurementDialog(
    lastHeightCm: Double?,
    onDismiss: () -> Unit,
    onSave: (BodyMeasurementEntity) -> Unit
) {
    var selectedDateMillis by rememberSaveable {
        mutableStateOf(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli())
    }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    var weightInput by rememberSaveable { mutableStateOf("") }
    var heightInput by rememberSaveable { mutableStateOf(lastHeightCm?.let { formatNumber(it) }.orEmpty()) }

    var chestInput by rememberSaveable { mutableStateOf("") }
    var waistInput by rememberSaveable { mutableStateOf("") }
    var abdomenInput by rememberSaveable { mutableStateOf("") }
    var hipInput by rememberSaveable { mutableStateOf("") }

    var armLeftRelaxedInput by rememberSaveable { mutableStateOf("") }
    var armLeftFlexedInput by rememberSaveable { mutableStateOf("") }
    var armRightRelaxedInput by rememberSaveable { mutableStateOf("") }
    var armRightFlexedInput by rememberSaveable { mutableStateOf("") }
    var forearmLeftInput by rememberSaveable { mutableStateOf("") }
    var forearmRightInput by rememberSaveable { mutableStateOf("") }
    var thighLeftInput by rememberSaveable { mutableStateOf("") }
    var thighRightInput by rememberSaveable { mutableStateOf("") }
    var calfLeftInput by rememberSaveable { mutableStateOf("") }
    var calfRightInput by rememberSaveable { mutableStateOf("") }

    var bodyFatInput by rememberSaveable { mutableStateOf("") }
    var leanMassInput by rememberSaveable { mutableStateOf("") }
    var fatMassInput by rememberSaveable { mutableStateOf("") }
    var bodyWaterInput by rememberSaveable { mutableStateOf("") }

    var tricepsFoldInput by rememberSaveable { mutableStateOf("") }
    var subscapularFoldInput by rememberSaveable { mutableStateOf("") }
    var suprailiacFoldInput by rememberSaveable { mutableStateOf("") }
    var abdominalFoldInput by rememberSaveable { mutableStateOf("") }
    var thighFoldInput by rememberSaveable { mutableStateOf("") }
    var chestFoldInput by rememberSaveable { mutableStateOf("") }
    var midaxillaryFoldInput by rememberSaveable { mutableStateOf("") }

    val weightValue = decimal(weightInput)
    val heightValue = decimal(heightInput)
    val isValid = weightValue != null && weightValue > 0 && heightValue != null && heightValue > 0

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Nova avaliação física", style = MaterialTheme.typography.titleLarge)
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Fechar")
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    CategoryLabel("Dados gerais e antropometria básica")

                    OutlinedTextField(
                        value = formatDate(selectedDateMillis),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Data da avaliação") },
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(imageVector = Icons.Filled.CalendarMonth, contentDescription = "Escolher data")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        MeasurementField(weightInput, { weightInput = it }, "Peso (kg) *")
                        MeasurementField(heightInput, { heightInput = it }, "Altura (cm) *")
                    }

                    CategoryLabel("Perímetros / circunferências (cm)")

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        MeasurementField(chestInput, { chestInput = it }, "Tórax / Peitoral")
                        MeasurementField(waistInput, { waistInput = it }, "Cintura")
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        MeasurementField(abdomenInput, { abdomenInput = it }, "Abdômen")
                        MeasurementField(hipInput, { hipInput = it }, "Quadril")
                    }

                    SubgroupLabel("Braço")
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        MeasurementField(armLeftRelaxedInput, { armLeftRelaxedInput = it }, "Esq. relaxado")
                        MeasurementField(armLeftFlexedInput, { armLeftFlexedInput = it }, "Esq. contraído")
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        MeasurementField(armRightRelaxedInput, { armRightRelaxedInput = it }, "Dir. relaxado")
                        MeasurementField(armRightFlexedInput, { armRightFlexedInput = it }, "Dir. contraído")
                    }

                    SubgroupLabel("Antebraço")
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        MeasurementField(forearmLeftInput, { forearmLeftInput = it }, "Esquerdo")
                        MeasurementField(forearmRightInput, { forearmRightInput = it }, "Direito")
                    }

                    SubgroupLabel("Coxa")
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        MeasurementField(thighLeftInput, { thighLeftInput = it }, "Esquerda")
                        MeasurementField(thighRightInput, { thighRightInput = it }, "Direita")
                    }

                    SubgroupLabel("Panturrilha")
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        MeasurementField(calfLeftInput, { calfLeftInput = it }, "Esquerda")
                        MeasurementField(calfRightInput, { calfRightInput = it }, "Direita")
                    }

                    CategoryLabel("Bioimpedância e composição corporal")
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        MeasurementField(bodyFatInput, { bodyFatInput = it }, "Gordura (%)")
                        MeasurementField(leanMassInput, { leanMassInput = it }, "Massa magra (kg)")
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        MeasurementField(fatMassInput, { fatMassInput = it }, "Massa gorda (kg)")
                        MeasurementField(bodyWaterInput, { bodyWaterInput = it }, "Água corporal (%)")
                    }

                    SubgroupLabel("Dobras cutâneas (mm) — opcional")
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        MeasurementField(tricepsFoldInput, { tricepsFoldInput = it }, "Tríceps")
                        MeasurementField(subscapularFoldInput, { subscapularFoldInput = it }, "Subescapular")
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        MeasurementField(suprailiacFoldInput, { suprailiacFoldInput = it }, "Suprailíaca")
                        MeasurementField(abdominalFoldInput, { abdominalFoldInput = it }, "Abdominal")
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        MeasurementField(thighFoldInput, { thighFoldInput = it }, "Coxa")
                        MeasurementField(chestFoldInput, { chestFoldInput = it }, "Peitoral")
                    }
                    OutlinedTextField(
                        value = midaxillaryFoldInput,
                        onValueChange = { input -> midaxillaryFoldInput = input.filter { c -> c.isDigit() || c == '.' || c == ',' } },
                        label = { Text("Axilar média") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                }

                Button(
                    onClick = {
                        if (isValid) {
                            onSave(
                                BodyMeasurementEntity(
                                    createdAt = selectedDateMillis,
                                    weightKg = weightValue!!,
                                    heightCm = heightValue!!,
                                    chestCm = decimal(chestInput),
                                    waistCm = decimal(waistInput),
                                    abdomenCm = decimal(abdomenInput),
                                    hipCm = decimal(hipInput),
                                    armLeftRelaxedCm = decimal(armLeftRelaxedInput),
                                    armLeftFlexedCm = decimal(armLeftFlexedInput),
                                    armRightRelaxedCm = decimal(armRightRelaxedInput),
                                    armRightFlexedCm = decimal(armRightFlexedInput),
                                    forearmLeftCm = decimal(forearmLeftInput),
                                    forearmRightCm = decimal(forearmRightInput),
                                    thighLeftCm = decimal(thighLeftInput),
                                    thighRightCm = decimal(thighRightInput),
                                    calfLeftCm = decimal(calfLeftInput),
                                    calfRightCm = decimal(calfRightInput),
                                    bodyFatPercent = decimal(bodyFatInput),
                                    leanMassKg = decimal(leanMassInput),
                                    fatMassKg = decimal(fatMassInput),
                                    bodyWaterPercent = decimal(bodyWaterInput),
                                    tricepsFoldMm = decimal(tricepsFoldInput),
                                    subscapularFoldMm = decimal(subscapularFoldInput),
                                    suprailiacFoldMm = decimal(suprailiacFoldInput),
                                    abdominalFoldMm = decimal(abdominalFoldInput),
                                    thighFoldMm = decimal(thighFoldInput),
                                    chestFoldMm = decimal(chestFoldInput),
                                    midaxillaryFoldMm = decimal(midaxillaryFoldInput)
                                )
                            )
                        }
                    },
                    enabled = isValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text("Salvar avaliação")
                }
            }

            if (showDatePicker) {
                val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDateMillis)
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { utcMillis ->
                                val localDate = Instant.ofEpochMilli(utcMillis).atZone(ZoneOffset.UTC).toLocalDate()
                                selectedDateMillis = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                            }
                            showDatePicker = false
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Cancelar")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: Double?, unit: String) {
    if (value == null) return
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "${formatNumber(value)} $unit",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun MeasurementDetailDialog(
    measurement: BodyMeasurementEntity,
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteConfirmation by rememberSaveable { mutableStateOf(false) }

    val hasPerimeterData = listOfNotNull(
        measurement.chestCm, measurement.waistCm, measurement.abdomenCm, measurement.hipCm,
        measurement.armLeftRelaxedCm, measurement.armLeftFlexedCm, measurement.armRightRelaxedCm, measurement.armRightFlexedCm,
        measurement.forearmLeftCm, measurement.forearmRightCm, measurement.thighLeftCm, measurement.thighRightCm,
        measurement.calfLeftCm, measurement.calfRightCm
    ).isNotEmpty()
    val hasCompositionData = listOfNotNull(
        measurement.bodyFatPercent, measurement.leanMassKg, measurement.fatMassKg, measurement.bodyWaterPercent
    ).isNotEmpty()
    val hasFoldData = listOfNotNull(
        measurement.tricepsFoldMm, measurement.subscapularFoldMm, measurement.suprailiacFoldMm,
        measurement.abdominalFoldMm, measurement.thighFoldMm, measurement.chestFoldMm, measurement.midaxillaryFoldMm
    ).isNotEmpty()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Avaliação de ${formatDate(measurement.createdAt)}",
                        style = MaterialTheme.typography.titleLarge
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Fechar")
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CategoryLabel("Dados gerais")
                    DetailRow("Peso", measurement.weightKg, "kg")
                    DetailRow("Altura", measurement.heightCm, "cm")

                    if (hasPerimeterData) {
                        CategoryLabel("Perímetros / circunferências")
                        DetailRow("Tórax / Peitoral", measurement.chestCm, "cm")
                        DetailRow("Cintura", measurement.waistCm, "cm")
                        DetailRow("Abdômen", measurement.abdomenCm, "cm")
                        DetailRow("Quadril", measurement.hipCm, "cm")
                        DetailRow("Braço esq. relaxado", measurement.armLeftRelaxedCm, "cm")
                        DetailRow("Braço esq. contraído", measurement.armLeftFlexedCm, "cm")
                        DetailRow("Braço dir. relaxado", measurement.armRightRelaxedCm, "cm")
                        DetailRow("Braço dir. contraído", measurement.armRightFlexedCm, "cm")
                        DetailRow("Antebraço esquerdo", measurement.forearmLeftCm, "cm")
                        DetailRow("Antebraço direito", measurement.forearmRightCm, "cm")
                        DetailRow("Coxa esquerda", measurement.thighLeftCm, "cm")
                        DetailRow("Coxa direita", measurement.thighRightCm, "cm")
                        DetailRow("Panturrilha esquerda", measurement.calfLeftCm, "cm")
                        DetailRow("Panturrilha direita", measurement.calfRightCm, "cm")
                    }

                    if (hasCompositionData) {
                        CategoryLabel("Bioimpedância e composição corporal")
                        DetailRow("Gordura corporal", measurement.bodyFatPercent, "%")
                        DetailRow("Massa magra", measurement.leanMassKg, "kg")
                        DetailRow("Massa gorda", measurement.fatMassKg, "kg")
                        DetailRow("Água corporal", measurement.bodyWaterPercent, "%")
                    }

                    if (hasFoldData) {
                        CategoryLabel("Dobras cutâneas")
                        DetailRow("Tríceps", measurement.tricepsFoldMm, "mm")
                        DetailRow("Subescapular", measurement.subscapularFoldMm, "mm")
                        DetailRow("Suprailíaca", measurement.suprailiacFoldMm, "mm")
                        DetailRow("Abdominal", measurement.abdominalFoldMm, "mm")
                        DetailRow("Coxa", measurement.thighFoldMm, "mm")
                        DetailRow("Peitoral", measurement.chestFoldMm, "mm")
                        DetailRow("Axilar média", measurement.midaxillaryFoldMm, "mm")
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                }

                Button(
                    onClick = { showDeleteConfirmation = true },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Excluir avaliação")
                }
            }

            if (showDeleteConfirmation) {
                AlertDialog(
                    onDismissRequest = { showDeleteConfirmation = false },
                    title = { Text("Excluir avaliação?") },
                    text = {
                        Text(
                            "A avaliação de ${formatDate(measurement.createdAt)} será removida " +
                                "permanentemente do histórico. Essa ação não pode ser desfeita."
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            showDeleteConfirmation = false
                            onDelete()
                        }) {
                            Text("Excluir", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteConfirmation = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> EnumDropdown(
    label: String,
    selected: T,
    options: List<T>,
    optionLabel: (T) -> String,
    onSelected: (T) -> Unit
) {
    var expanded by rememberSaveable(selected) { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = optionLabel(selected),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(optionLabel(option)) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

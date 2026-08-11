package com.vitorsousa.stallfit.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.vitorsousa.stallfit.data.local.entity.ActivityLevel
import com.vitorsousa.stallfit.data.local.entity.BiologicalSex
import com.vitorsousa.stallfit.data.local.entity.NutritionGoal
import com.vitorsousa.stallfit.di.AppViewModelProvider
import com.vitorsousa.stallfit.ui.components.StatCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    var ageInput by rememberSaveable { mutableStateOf("") }
    var weightInput by rememberSaveable { mutableStateOf("") }
    var heightInput by rememberSaveable { mutableStateOf("") }
    var armInput by rememberSaveable { mutableStateOf("") }
    var chestInput by rememberSaveable { mutableStateOf("") }
    var hipInput by rememberSaveable { mutableStateOf("") }
    var thighInput by rememberSaveable { mutableStateOf("") }
    var calfInput by rememberSaveable { mutableStateOf("") }

    var sex by rememberSaveable { mutableStateOf(BiologicalSex.MALE) }
    var activityLevel by rememberSaveable { mutableStateOf(ActivityLevel.SEDENTARY) }
    var goal by rememberSaveable { mutableStateOf(NutritionGoal.MAINTENANCE) }

    var initialized by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(uiState.profile) {
        val current = uiState.profile
        if (!initialized && current != null) {
            ageInput = current.ageYears.toString()
            weightInput = current.weightKg.toString()
            heightInput = current.heightCm.toString()
            armInput = current.armCm?.toString().orEmpty()
            chestInput = current.chestCm?.toString().orEmpty()
            hipInput = current.hipCm?.toString().orEmpty()
            thighInput = current.thighCm?.toString().orEmpty()
            calfInput = current.calfCm?.toString().orEmpty()
            sex = current.sex
            activityLevel = current.activityLevel
            goal = current.goal
            initialized = true
        }
    }

    fun decimal(value: String) = value.replace(',', '.').toDoubleOrNull()

    val ageValue = ageInput.toIntOrNull()
    val weightValue = decimal(weightInput)
    val heightValue = decimal(heightInput)
    val isValid = ageValue != null && ageValue > 0 &&
        weightValue != null && weightValue > 0 &&
        heightValue != null && heightValue > 0

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Voltar")
            }
            Text(
                text = "Perfil",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Preencha seus dados para calcular sua Taxa Metabólica Basal (TMB), gasto calórico total (GET), meta de água e distribuição de macros.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = ageInput,
                    onValueChange = { input -> ageInput = input.filter { it.isDigit() } },
                    label = { Text("Idade") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = weightInput,
                    onValueChange = { input -> weightInput = input.filter { c -> c.isDigit() || c == '.' || c == ',' } },
                    label = { Text("Peso (kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }

            OutlinedTextField(
                value = heightInput,
                onValueChange = { input -> heightInput = input.filter { c -> c.isDigit() || c == '.' || c == ',' } },
                label = { Text("Altura (cm)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
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

            Text(
                text = "Medidas corporais (opcional)",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = armInput,
                    onValueChange = { input -> armInput = input.filter { c -> c.isDigit() || c == '.' || c == ',' } },
                    label = { Text("Braço (cm)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = chestInput,
                    onValueChange = { input -> chestInput = input.filter { c -> c.isDigit() || c == '.' || c == ',' } },
                    label = { Text("Peitoral (cm)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = hipInput,
                    onValueChange = { input -> hipInput = input.filter { c -> c.isDigit() || c == '.' || c == ',' } },
                    label = { Text("Quadril (cm)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = thighInput,
                    onValueChange = { input -> thighInput = input.filter { c -> c.isDigit() || c == '.' || c == ',' } },
                    label = { Text("Coxa (cm)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }
            OutlinedTextField(
                value = calfInput,
                onValueChange = { input -> calfInput = input.filter { c -> c.isDigit() || c == '.' || c == ',' } },
                label = { Text("Panturrilha (cm)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (isValid) {
                        viewModel.saveProfile(
                            ageYears = ageValue!!,
                            weightKg = weightValue!!,
                            heightCm = heightValue!!,
                            sex = sex,
                            activityLevel = activityLevel,
                            goal = goal,
                            armCm = decimal(armInput),
                            chestCm = decimal(chestInput),
                            hipCm = decimal(hipInput),
                            thighCm = decimal(thighInput),
                            calfCm = decimal(calfInput),
                            onSaved = {}
                        )
                    }
                },
                enabled = isValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Calcular e salvar")
            }

            val result = uiState.result
            if (result != null) {
                Text(
                    text = "Resultado",
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

package com.vitorsousa.stallfit.ui.goals

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
import com.vitorsousa.stallfit.di.AppViewModelProvider

@Composable
fun GoalsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GoalsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val goal by viewModel.macroGoal.collectAsState()

    var calorieInput by rememberSaveable { mutableStateOf("") }
    var proteinInput by rememberSaveable { mutableStateOf("") }
    var carbInput by rememberSaveable { mutableStateOf("") }
    var fatInput by rememberSaveable { mutableStateOf("") }
    var initialized by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(goal) {
        val current = goal
        if (!initialized && current != null) {
            calorieInput = current.calorieGoal.toString()
            proteinInput = current.proteinGoal.toString()
            carbInput = current.carbGoal.toString()
            fatInput = current.fatGoal.toString()
            initialized = true
        }
    }

    val calorieValue = calorieInput.toIntOrNull()
    val proteinValue = proteinInput.toIntOrNull()
    val carbValue = carbInput.toIntOrNull()
    val fatValue = fatInput.toIntOrNull()
    val isValid = calorieValue != null && calorieValue > 0 &&
        proteinValue != null && proteinValue >= 0 &&
        carbValue != null && carbValue >= 0 &&
        fatValue != null && fatValue >= 0

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
                text = "Metas de macros",
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
                text = "Defina suas metas diárias. Elas aparecem no Dashboard e podem ser recalculadas automaticamente a partir do seu Perfil.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = calorieInput,
                onValueChange = { input -> calorieInput = input.filter { it.isDigit() } },
                label = { Text("Meta de calorias (kcal)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = proteinInput,
                onValueChange = { input -> proteinInput = input.filter { it.isDigit() } },
                label = { Text("Meta de proteína (g)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = carbInput,
                onValueChange = { input -> carbInput = input.filter { it.isDigit() } },
                label = { Text("Meta de carboidratos (g)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = fatInput,
                onValueChange = { input -> fatInput = input.filter { it.isDigit() } },
                label = { Text("Meta de gorduras (g)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (isValid) {
                        viewModel.saveGoal(calorieValue!!, proteinValue!!, carbValue!!, fatValue!!, onBack)
                    }
                },
                enabled = isValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar metas")
            }
        }
    }
}

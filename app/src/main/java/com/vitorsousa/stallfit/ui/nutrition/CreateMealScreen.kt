package com.vitorsousa.stallfit.ui.nutrition

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.vitorsousa.stallfit.data.local.entity.FoodEntity
import com.vitorsousa.stallfit.di.AppViewModelProvider
import com.vitorsousa.stallfit.ui.components.EmptyState
import kotlin.math.roundToInt
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreateMealScreen(
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateMealViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    var mealName by rememberSaveable { mutableStateOf("") }
    var query by rememberSaveable { mutableStateOf("") }
    var foodForGramsDialog by rememberSaveable { mutableStateOf<Long?>(null) }
    var showCustomFoodDialog by rememberSaveable { mutableStateOf(false) }

    val filteredFoods = if (query.isBlank()) {
        uiState.foods
    } else {
        uiState.foods.filter { it.name.contains(query, ignoreCase = true) }
    }
    val addedFoodIds = uiState.draftItems.map { it.foodId }.toSet()
    val selectedFood = uiState.foods.firstOrNull { it.id == foodForGramsDialog }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    // Focusing a field triggers Compose's automatic "scroll into view above the keyboard", which
    // briefly flips isScrollInProgress too — watching that flag here would clear focus (and close
    // the keyboard) the instant it opens, causing an open/close loop. Watching drag interactions
    // instead only reacts to the user actually dragging the list with a finger.
    LaunchedEffect(listState.interactionSource) {
        listState.interactionSource.interactions.collect { interaction ->
            if (interaction is DragInteraction.Start) {
                focusManager.clearFocus()
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDone) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                    Text(
                        text = "Nova refeição em ${viewModel.mealType.label}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            item {
                OutlinedTextField(
                    value = mealName,
                    onValueChange = { mealName = it },
                    label = { Text("Nome da refeição") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (uiState.draftItems.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Total: ${uiState.totalCalories.roundToInt()} kcal · P ${uiState.totalProtein.roundToInt()}g · C ${uiState.totalCarbs.roundToInt()}g · G ${uiState.totalFat.roundToInt()}g",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                items(uiState.draftItems.size) { index ->
                    val item = uiState.draftItems[index]
                    Card(
                        modifier = Modifier.fillMaxWidth(),
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
                                    text = item.foodName,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${item.grams.roundToInt()} g · ${item.calories.roundToInt()} kcal",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            IconButton(onClick = { viewModel.removeItem(index) }) {
                                Icon(
                                    imageVector = Icons.Filled.DeleteOutline,
                                    contentDescription = "Remover",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            item {
                Button(
                    onClick = { viewModel.saveMeal(mealName, onDone) },
                    enabled = mealName.isNotBlank() && uiState.draftItems.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Salvar refeição")
                }
            }

            item {
                Text(
                    text = "Adicionar alimentos",
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
                        label = { Text("Buscar alimento") },
                        leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            item {
                OutlinedButton(
                    onClick = { showCustomFoodDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                    Text(text = "Criar alimento personalizado", modifier = Modifier.padding(start = 8.dp))
                }
            }

            if (filteredFoods.isEmpty()) {
                item { EmptyState(message = "Nenhum alimento encontrado.") }
            } else {
                items(filteredFoods, key = { it.id }) { food ->
                    FoodRow(
                        food = food,
                        isSelected = food.id in addedFoodIds,
                        onClick = { foodForGramsDialog = food.id }
                    )
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

    if (selectedFood != null) {
        GramsDialog(
            food = selectedFood,
            onDismiss = { foodForGramsDialog = null },
            onConfirm = { grams ->
                viewModel.addFoodItem(selectedFood, grams)
                foodForGramsDialog = null
            }
        )
    }

    if (showCustomFoodDialog) {
        CustomFoodDialog(
            onDismiss = { showCustomFoodDialog = false },
            onConfirm = { name, calories, protein, carbs, fat, grams ->
                viewModel.addCustomFoodAndAppend(
                    name = name,
                    caloriesPer100g = calories,
                    proteinPer100g = protein,
                    carbsPer100g = carbs,
                    fatPer100g = fat,
                    grams = grams
                )
                showCustomFoodDialog = false
            }
        )
    }
}

@Composable
private fun FoodRow(food: FoodEntity, isSelected: Boolean, onClick: () -> Unit) {
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
                    text = food.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${food.caloriesPer100g.roundToInt()} kcal / 100g · P ${food.proteinPer100g.roundToInt()}g · C ${food.carbsPer100g.roundToInt()}g · G ${food.fatPer100g.roundToInt()}g",
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
private fun GramsDialog(
    food: FoodEntity,
    onDismiss: () -> Unit,
    onConfirm: (grams: Double) -> Unit
) {
    var gramsInput by rememberSaveable { mutableStateOf("100") }
    val grams = gramsInput.replace(',', '.').toDoubleOrNull()
    val previewCalories = grams?.let { (food.caloriesPer100g * it / 100.0).roundToInt() }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(food.name) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = gramsInput,
                    onValueChange = { input -> gramsInput = input.filter { c -> c.isDigit() || c == '.' || c == ',' } },
                    label = { Text("Quantidade (g)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                if (previewCalories != null) {
                    Text(
                        text = "≈ $previewCalories kcal",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { grams?.let(onConfirm) },
                enabled = grams != null && grams > 0
            ) {
                Text("Adicionar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = null, modifier = Modifier.padding(end = 4.dp))
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun CustomFoodDialog(
    onDismiss: () -> Unit,
    onConfirm: (
        name: String,
        caloriesPer100g: Double,
        proteinPer100g: Double,
        carbsPer100g: Double,
        fatPer100g: Double,
        grams: Double
    ) -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var calories by rememberSaveable { mutableStateOf("") }
    var protein by rememberSaveable { mutableStateOf("") }
    var carbs by rememberSaveable { mutableStateOf("") }
    var fat by rememberSaveable { mutableStateOf("") }
    var grams by rememberSaveable { mutableStateOf("100") }

    fun decimal(value: String) = value.replace(',', '.').toDoubleOrNull()

    val caloriesValue = decimal(calories)
    val proteinValue = decimal(protein)
    val carbsValue = decimal(carbs)
    val fatValue = decimal(fat)
    val gramsValue = decimal(grams)

    val isValid = name.isNotBlank() &&
        caloriesValue != null && caloriesValue >= 0 &&
        proteinValue != null && proteinValue >= 0 &&
        carbsValue != null && carbsValue >= 0 &&
        fatValue != null && fatValue >= 0 &&
        gramsValue != null && gramsValue > 0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Alimento personalizado") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = calories,
                        onValueChange = { input -> calories = input.filter { c -> c.isDigit() || c == '.' || c == ',' } },
                        label = { Text("Kcal/100g") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = protein,
                        onValueChange = { input -> protein = input.filter { c -> c.isDigit() || c == '.' || c == ',' } },
                        label = { Text("Prot/100g") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = carbs,
                        onValueChange = { input -> carbs = input.filter { c -> c.isDigit() || c == '.' || c == ',' } },
                        label = { Text("Carb/100g") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = fat,
                        onValueChange = { input -> fat = input.filter { c -> c.isDigit() || c == '.' || c == ',' } },
                        label = { Text("Gord/100g") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value = grams,
                    onValueChange = { input -> grams = input.filter { c -> c.isDigit() || c == '.' || c == ',' } },
                    label = { Text("Quantidade a adicionar (g)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(name.trim(), caloriesValue!!, proteinValue!!, carbsValue!!, fatValue!!, gramsValue!!)
                },
                enabled = isValid
            ) {
                Text("Salvar e adicionar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = null, modifier = Modifier.padding(end = 4.dp))
                Text("Cancelar")
            }
        }
    )
}

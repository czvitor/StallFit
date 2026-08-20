package com.vitorsousa.stallfit.wear.ui

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Stepper
import androidx.wear.compose.material3.Text
import com.vitorsousa.stallfit.wear.data.WearExerciseDto

@Composable
fun ActiveWorkoutScreen(
    sessionName: String,
    exercises: List<WearExerciseDto>,
    actionState: ActionState,
    lastLoggedSetNumber: Int?,
    liveHeartRateBpm: Int?,
    onLogSet: (exerciseId: Long, reps: Int, weightKg: Double) -> Unit,
    onFinishSession: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedExerciseId by remember(exercises) { mutableStateOf(exercises.firstOrNull()?.id) }
    var reps by remember { mutableIntStateOf(10) }
    var weightKg by remember { mutableIntStateOf(20) }

    ScreenScaffold(modifier = modifier) {
        ScalingLazyColumn(modifier = Modifier.fillMaxSize()) {
            item { Text(text = sessionName, style = MaterialTheme.typography.titleMedium) }

            if (liveHeartRateBpm != null) {
                item { Text(text = "♥ $liveHeartRateBpm bpm") }
            }

            item {
                val selectedName = exercises.firstOrNull { it.id == selectedExerciseId }?.name
                Text(text = selectedName ?: "Selecione um exercício")
            }

            items(exercises) { exercise: WearExerciseDto ->
                Button(onClick = { selectedExerciseId = exercise.id }) { Text(exercise.name) }
            }

            item {
                Stepper(
                    value = reps,
                    onValueChange = { reps = it },
                    valueProgression = 1..30,
                    decreaseIcon = { Text("−") },
                    increaseIcon = { Text("+") },
                    modifier = Modifier.height(120.dp)
                ) {
                    Text("Reps: $reps")
                }
            }

            item {
                Stepper(
                    value = weightKg,
                    onValueChange = { weightKg = it },
                    valueProgression = 0..300 step 5,
                    decreaseIcon = { Text("−") },
                    increaseIcon = { Text("+") },
                    modifier = Modifier.height(120.dp)
                ) {
                    Text("Carga: $weightKg kg")
                }
            }

            item {
                Button(
                    onClick = { selectedExerciseId?.let { onLogSet(it, reps, weightKg.toDouble()) } }
                ) {
                    Text(if (actionState is ActionState.Sending) "Enviando..." else "Registrar série")
                }
            }

            if (lastLoggedSetNumber != null) {
                item { Text(text = "Série $lastLoggedSetNumber registrada!") }
            }

            if (actionState is ActionState.Error) {
                item { Text(text = actionState.message) }
            }

            item { Button(onClick = onFinishSession) { Text("Finalizar treino") } }
        }
    }
}

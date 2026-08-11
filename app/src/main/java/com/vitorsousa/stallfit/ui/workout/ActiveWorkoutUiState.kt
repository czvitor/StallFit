package com.vitorsousa.stallfit.ui.workout

import com.vitorsousa.stallfit.data.local.entity.WorkoutSessionEntity
import com.vitorsousa.stallfit.data.local.relation.SetEntryWithExercise

data class ActiveWorkoutUiState(
    val session: WorkoutSessionEntity? = null,
    val sets: List<SetEntryWithExercise> = emptyList(),
    val volume: Double = 0.0
) {
    val isFinished: Boolean get() = session?.finishedAt != null
}

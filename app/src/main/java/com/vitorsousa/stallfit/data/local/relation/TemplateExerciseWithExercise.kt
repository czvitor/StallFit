package com.vitorsousa.stallfit.data.local.relation

import androidx.room.Embedded
import com.vitorsousa.stallfit.data.local.entity.Equipment
import com.vitorsousa.stallfit.data.local.entity.TemplateExerciseEntity

/** Result of joining `template_exercises` with `exercises` — avoids a second query just to show a name. */
data class TemplateExerciseWithExercise(
    @Embedded val templateExercise: TemplateExerciseEntity,
    val exerciseName: String,
    val muscleGroup: String,
    val equipment: Equipment
)

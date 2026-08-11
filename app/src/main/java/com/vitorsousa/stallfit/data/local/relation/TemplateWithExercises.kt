package com.vitorsousa.stallfit.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.vitorsousa.stallfit.data.local.entity.TemplateExerciseEntity
import com.vitorsousa.stallfit.data.local.entity.WorkoutTemplateEntity

/** A template plus every exercise configured on it — used for the "Meus treinos" summary list. */
data class TemplateWithExercises(
    @Embedded val template: WorkoutTemplateEntity,
    @Relation(parentColumn = "id", entityColumn = "templateId")
    val exercises: List<TemplateExerciseEntity>
)

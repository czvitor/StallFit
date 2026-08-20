package com.vitorsousa.stallfit.data.repository

import com.vitorsousa.stallfit.data.local.entity.ExerciseEntity
import com.vitorsousa.stallfit.data.local.entity.Intensity
import com.vitorsousa.stallfit.data.local.entity.TemplateExerciseEntity
import com.vitorsousa.stallfit.data.local.entity.WorkoutTemplateEntity
import com.vitorsousa.stallfit.data.local.relation.TemplateWithExercises
import org.junit.Assert.assertEquals
import org.junit.Test

class WearRepositoryTest {

    private val squat = ExerciseEntity(id = 1, name = "Agachamento", muscleGroup = "Pernas")
    private val bench = ExerciseEntity(id = 2, name = "Supino", muscleGroup = "Peito")

    private fun templateExercise(templateId: Long, exerciseId: Long, orderIndex: Int) = TemplateExerciseEntity(
        id = orderIndex.toLong(),
        templateId = templateId,
        exerciseId = exerciseId,
        orderIndex = orderIndex,
        sets = 3,
        repRangeMin = 8,
        repRangeMax = 12,
        restSeconds = 90,
        intensity = Intensity.MODERADA
    )

    @Test
    fun `orders a template's exercises by orderIndex, not insertion order`() {
        val template = TemplateWithExercises(
            template = WorkoutTemplateEntity(id = 10, title = "Treino A", createdAt = 0L),
            exercises = listOf(
                templateExercise(templateId = 10, exerciseId = bench.id, orderIndex = 1),
                templateExercise(templateId = 10, exerciseId = squat.id, orderIndex = 0)
            )
        )

        val catalog = buildCatalogDto(templates = listOf(template), exercises = listOf(squat, bench))

        assertEquals(listOf("Agachamento", "Supino"), catalog.templates.single().exercises.map { it.name })
    }

    @Test
    fun `drops a template exercise whose exercise row no longer exists`() {
        val template = TemplateWithExercises(
            template = WorkoutTemplateEntity(id = 10, title = "Treino A", createdAt = 0L),
            exercises = listOf(templateExercise(templateId = 10, exerciseId = 999L, orderIndex = 0))
        )

        val catalog = buildCatalogDto(templates = listOf(template), exercises = listOf(squat))

        assertEquals(emptyList<Any>(), catalog.templates.single().exercises)
    }

    @Test
    fun `allExercises carries every exercise regardless of template membership`() {
        val catalog = buildCatalogDto(templates = emptyList(), exercises = listOf(squat, bench))

        assertEquals(setOf("Agachamento", "Supino"), catalog.allExercises.map { it.name }.toSet())
    }
}

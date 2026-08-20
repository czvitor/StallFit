package com.vitorsousa.stallfit.data.repository

import com.vitorsousa.stallfit.core.util.DateUtils
import com.vitorsousa.stallfit.data.local.entity.ExerciseEntity
import com.vitorsousa.stallfit.data.local.relation.TemplateWithExercises
import com.vitorsousa.stallfit.data.wear.WearCatalogDto
import com.vitorsousa.stallfit.data.wear.WearExerciseDto
import com.vitorsousa.stallfit.data.wear.WearFinishSessionRequest
import com.vitorsousa.stallfit.data.wear.WearLogSetRequest
import com.vitorsousa.stallfit.data.wear.WearSessionStartedDto
import com.vitorsousa.stallfit.data.wear.WearSetLoggedDto
import com.vitorsousa.stallfit.data.wear.WearTemplateDto
import kotlinx.coroutines.flow.first

/** Translates the watch<->phone message protocol (see `data/wear/`) into calls against the existing workout repositories. */
class WearRepository(
    private val workoutRepository: WorkoutRepository,
    private val workoutTemplateRepository: WorkoutTemplateRepository
) {
    suspend fun buildCatalog(): WearCatalogDto {
        val templates = workoutTemplateRepository.allTemplatesWithExercises.first()
        val exercises = workoutRepository.allExercises.first()
        return buildCatalogDto(templates, exercises)
    }

    suspend fun startSession(templateId: Long?): WearSessionStartedDto {
        val sessionId = if (templateId != null) {
            workoutTemplateRepository.getOrStartSessionForTemplate(templateId)
        } else {
            workoutRepository.startSession(DateUtils.todaySessionLabel())
        }
        val session = checkNotNull(workoutRepository.getSessionFlow(sessionId).first()) {
            "Session $sessionId was just created but can't be found"
        }
        return WearSessionStartedDto(sessionId = sessionId, sessionName = session.name)
    }

    suspend fun logSet(request: WearLogSetRequest): WearSetLoggedDto {
        val currentSets = workoutRepository.getSetsForSession(request.sessionId).first()
        val nextSetNumber = currentSets.count { it.setEntry.exerciseId == request.exerciseId } + 1
        workoutRepository.logSet(
            sessionId = request.sessionId,
            exerciseId = request.exerciseId,
            setNumber = nextSetNumber,
            reps = request.reps,
            weightKg = request.weightKg
        )
        return WearSetLoggedDto(setNumber = nextSetNumber)
    }

    suspend fun finishSession(request: WearFinishSessionRequest) {
        val session = workoutRepository.getSessionFlow(request.sessionId).first() ?: return
        workoutRepository.finishSession(session, request.avgHeartRateBpm, request.totalCalories)
    }
}

/** Pure join extracted out of [WearRepository.buildCatalog] so it's unit-testable without touching Room. */
fun buildCatalogDto(templates: List<TemplateWithExercises>, exercises: List<ExerciseEntity>): WearCatalogDto {
    val exerciseById = exercises.associateBy { it.id }
    val templateDtos = templates.map { template ->
        val exerciseDtos = template.exercises
            .sortedBy { it.orderIndex }
            .mapNotNull { templateExercise -> exerciseById[templateExercise.exerciseId] }
            .map { WearExerciseDto(id = it.id, name = it.name) }
        WearTemplateDto(id = template.template.id, title = template.template.title, exercises = exerciseDtos)
    }
    val allExerciseDtos = exercises.map { WearExerciseDto(id = it.id, name = it.name) }
    return WearCatalogDto(templates = templateDtos, allExercises = allExerciseDtos)
}

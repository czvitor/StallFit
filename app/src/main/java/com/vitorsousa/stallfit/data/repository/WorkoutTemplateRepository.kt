package com.vitorsousa.stallfit.data.repository

import com.vitorsousa.stallfit.core.util.DateUtils
import com.vitorsousa.stallfit.data.local.dao.WorkoutSessionDao
import com.vitorsousa.stallfit.data.local.dao.WorkoutTemplateDao
import com.vitorsousa.stallfit.data.local.entity.TemplateExerciseEntity
import com.vitorsousa.stallfit.data.local.entity.WorkoutSessionEntity
import com.vitorsousa.stallfit.data.local.entity.WorkoutTemplateEntity
import com.vitorsousa.stallfit.data.local.relation.TemplateExerciseWithExercise
import com.vitorsousa.stallfit.data.local.relation.TemplateWithExercises
import kotlinx.coroutines.flow.Flow

/** Single entry point for every read/write the UI needs from the workout template ("ficha") tables. */
class WorkoutTemplateRepository(
    private val templateDao: WorkoutTemplateDao,
    private val sessionDao: WorkoutSessionDao
) {
    val allTemplatesWithExercises: Flow<List<TemplateWithExercises>> = templateDao.getAllTemplatesWithExercises()

    fun getTemplateFlow(templateId: Long): Flow<WorkoutTemplateEntity?> =
        templateDao.getTemplateFlow(templateId)

    fun getExercisesForTemplate(templateId: Long): Flow<List<TemplateExerciseWithExercise>> =
        templateDao.getExercisesForTemplate(templateId)

    suspend fun createTemplate(title: String, exercises: List<TemplateExerciseEntity>): Long =
        templateDao.insertTemplateWithExercises(
            WorkoutTemplateEntity(title = title, createdAt = System.currentTimeMillis()),
            exercises
        )

    suspend fun deleteTemplate(templateId: Long) = templateDao.deleteTemplate(templateId)

    /** Reuses today's still-open session started for this template, or starts a fresh one. */
    suspend fun getOrStartSessionForTemplate(templateId: Long): Long {
        val existing = sessionDao.getOpenSessionForTemplateToday(
            templateId = templateId,
            startMillis = DateUtils.startOfDayMillis(),
            endMillis = DateUtils.endOfDayMillis()
        )
        if (existing != null) return existing.id

        val template = templateDao.getTemplateOnce(templateId)
        return sessionDao.insert(
            WorkoutSessionEntity(
                name = template?.title ?: "Treino",
                startedAt = System.currentTimeMillis(),
                templateId = templateId
            )
        )
    }
}

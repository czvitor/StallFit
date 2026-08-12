package com.vitorsousa.stallfit.data.repository

import com.vitorsousa.stallfit.core.util.DateUtils
import com.vitorsousa.stallfit.core.util.normalizedKey
import com.vitorsousa.stallfit.data.local.dao.ExerciseDao
import com.vitorsousa.stallfit.data.local.dao.SetEntryDao
import com.vitorsousa.stallfit.data.local.dao.WorkoutSessionDao
import com.vitorsousa.stallfit.data.local.entity.Equipment
import com.vitorsousa.stallfit.data.local.entity.ExerciseEntity
import com.vitorsousa.stallfit.data.local.entity.SetEntryEntity
import com.vitorsousa.stallfit.data.local.entity.WorkoutSessionEntity
import com.vitorsousa.stallfit.data.local.relation.SetEntryWithExercise
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** Single entry point for every read/write the UI needs from the workout tables. */
class WorkoutRepository(
    private val exerciseDao: ExerciseDao,
    private val sessionDao: WorkoutSessionDao,
    private val setEntryDao: SetEntryDao
) {
    // distinctBy on the normalized name self-heals any duplicate/near-duplicate rows (accent or
    // casing variations) that may already exist, on top of the insert-time guard below.
    val allExercises: Flow<List<ExerciseEntity>> = exerciseDao.getAll()
        .map { exercises -> exercises.distinctBy { it.name.normalizedKey() } }
    val activeSession: Flow<WorkoutSessionEntity?> = sessionDao.getActiveSession()
    val completedSessions: Flow<List<WorkoutSessionEntity>> = sessionDao.getCompletedSessions()

    fun getSessionFlow(sessionId: Long): Flow<WorkoutSessionEntity?> =
        sessionDao.getById(sessionId)

    fun getSetsForSession(sessionId: Long): Flow<List<SetEntryWithExercise>> =
        setEntryDao.getSetsForSession(sessionId)

    fun getVolumeForSession(sessionId: Long): Flow<Double> =
        setEntryDao.getVolumeForSession(sessionId)

    fun getVolumeToday(): Flow<Double> =
        setEntryDao.getVolumeBetween(DateUtils.startOfDayMillis(), DateUtils.endOfDayMillis())

    fun getVolumeThisWeek(): Flow<Double> =
        setEntryDao.getVolumeBetween(DateUtils.startOfWeekMillis(), DateUtils.endOfDayMillis())

    suspend fun startSession(name: String): Long =
        sessionDao.insert(WorkoutSessionEntity(name = name, startedAt = System.currentTimeMillis()))

    suspend fun finishSession(session: WorkoutSessionEntity) {
        sessionDao.update(session.copy(finishedAt = System.currentTimeMillis()))
    }

    suspend fun discardSession(sessionId: Long) {
        sessionDao.delete(sessionId)
    }

    suspend fun logSet(sessionId: Long, exerciseId: Long, setNumber: Int, reps: Int, weightKg: Double) {
        setEntryDao.insert(
            SetEntryEntity(
                sessionId = sessionId,
                exerciseId = exerciseId,
                setNumber = setNumber,
                reps = reps,
                weightKg = weightKg,
                loggedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun deleteSet(setEntry: SetEntryEntity) = setEntryDao.delete(setEntry)

    suspend fun getLastSetForExercise(exerciseId: Long): SetEntryEntity? =
        setEntryDao.getLastSetForExercise(exerciseId)

    suspend fun addCustomExercise(name: String, muscleGroup: String, equipment: Equipment = Equipment.NONE): Long {
        val key = name.normalizedKey()
        val existing = exerciseDao.getAllOnce().firstOrNull { it.name.normalizedKey() == key }
        if (existing != null) return existing.id
        return exerciseDao.insert(ExerciseEntity(name = name, muscleGroup = muscleGroup, equipment = equipment, isCustom = true))
    }
}

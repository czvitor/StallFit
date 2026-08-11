package com.vitorsousa.stallfit.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.vitorsousa.stallfit.data.local.entity.WorkoutSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutSessionDao {
    @Insert
    suspend fun insert(session: WorkoutSessionEntity): Long

    @Update
    suspend fun update(session: WorkoutSessionEntity)

    @Query("DELETE FROM workout_sessions WHERE id = :sessionId")
    suspend fun delete(sessionId: Long)

    @Query("SELECT * FROM workout_sessions WHERE id = :sessionId")
    fun getById(sessionId: Long): Flow<WorkoutSessionEntity?>

    @Query("SELECT * FROM workout_sessions WHERE finishedAt IS NULL ORDER BY startedAt DESC LIMIT 1")
    fun getActiveSession(): Flow<WorkoutSessionEntity?>

    @Query("SELECT * FROM workout_sessions WHERE finishedAt IS NOT NULL ORDER BY startedAt DESC")
    fun getCompletedSessions(): Flow<List<WorkoutSessionEntity>>

    @Query("SELECT * FROM workout_sessions WHERE startedAt BETWEEN :startMillis AND :endMillis ORDER BY startedAt DESC")
    fun getSessionsBetween(startMillis: Long, endMillis: Long): Flow<List<WorkoutSessionEntity>>
}

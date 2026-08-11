package com.vitorsousa.stallfit.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.vitorsousa.stallfit.data.local.entity.SetEntryEntity
import com.vitorsousa.stallfit.data.local.relation.SetEntryWithExercise
import kotlinx.coroutines.flow.Flow

@Dao
interface SetEntryDao {
    @Insert
    suspend fun insert(setEntry: SetEntryEntity): Long

    @Delete
    suspend fun delete(setEntry: SetEntryEntity)

    @Query(
        """
        SELECT set_entries.*, exercises.name AS exerciseName
        FROM set_entries
        JOIN exercises ON exercises.id = set_entries.exerciseId
        WHERE set_entries.sessionId = :sessionId
        ORDER BY set_entries.loggedAt ASC
        """
    )
    fun getSetsForSession(sessionId: Long): Flow<List<SetEntryWithExercise>>

    @Query(
        """
        SELECT * FROM set_entries
        WHERE exerciseId = :exerciseId
        ORDER BY loggedAt DESC
        LIMIT 1
        """
    )
    suspend fun getLastSetForExercise(exerciseId: Long): SetEntryEntity?

    @Query(
        """
        SELECT COALESCE(SUM(reps * weightKg), 0.0)
        FROM set_entries
        WHERE sessionId = :sessionId
        """
    )
    fun getVolumeForSession(sessionId: Long): Flow<Double>

    @Query(
        """
        SELECT COALESCE(SUM(set_entries.reps * set_entries.weightKg), 0.0)
        FROM set_entries
        JOIN workout_sessions ON workout_sessions.id = set_entries.sessionId
        WHERE workout_sessions.startedAt BETWEEN :startMillis AND :endMillis
        """
    )
    fun getVolumeBetween(startMillis: Long, endMillis: Long): Flow<Double>
}

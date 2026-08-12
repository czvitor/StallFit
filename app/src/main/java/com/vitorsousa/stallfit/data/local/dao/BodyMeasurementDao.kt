package com.vitorsousa.stallfit.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.vitorsousa.stallfit.data.local.entity.BodyMeasurementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BodyMeasurementDao {
    @Query("SELECT * FROM body_measurements ORDER BY createdAt DESC")
    fun getAll(): Flow<List<BodyMeasurementEntity>>

    @Query("SELECT * FROM body_measurements ORDER BY createdAt DESC LIMIT 1")
    fun getLatest(): Flow<BodyMeasurementEntity?>

    @Query("SELECT * FROM body_measurements ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLatestOnce(): BodyMeasurementEntity?

    @Insert
    suspend fun insert(measurement: BodyMeasurementEntity): Long

    @Delete
    suspend fun delete(measurement: BodyMeasurementEntity)
}

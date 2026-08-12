package com.vitorsousa.stallfit.data.repository

import com.vitorsousa.stallfit.data.local.dao.BodyMeasurementDao
import com.vitorsousa.stallfit.data.local.dao.UserProfileDao
import com.vitorsousa.stallfit.data.local.entity.BodyMeasurementEntity
import com.vitorsousa.stallfit.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

/** Single entry point for every read/write the UI needs from the profile and body-measurement tables. */
class ProfileRepository(
    private val userProfileDao: UserProfileDao,
    private val bodyMeasurementDao: BodyMeasurementDao
) {
    val profile: Flow<UserProfileEntity?> = userProfileDao.getProfile()
    val measurementHistory: Flow<List<BodyMeasurementEntity>> = bodyMeasurementDao.getAll()
    val latestMeasurement: Flow<BodyMeasurementEntity?> = bodyMeasurementDao.getLatest()

    suspend fun saveProfile(profile: UserProfileEntity) = userProfileDao.upsert(profile)

    suspend fun addMeasurement(measurement: BodyMeasurementEntity): Long =
        bodyMeasurementDao.insert(measurement)

    suspend fun deleteMeasurement(measurement: BodyMeasurementEntity) =
        bodyMeasurementDao.delete(measurement)

    suspend fun getLatestMeasurementOnce(): BodyMeasurementEntity? = bodyMeasurementDao.getLatestOnce()
}

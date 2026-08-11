package com.vitorsousa.stallfit.data.repository

import com.vitorsousa.stallfit.data.local.dao.UserProfileDao
import com.vitorsousa.stallfit.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

/** Single entry point for every read/write the UI needs from the user profile table. */
class ProfileRepository(
    private val userProfileDao: UserProfileDao
) {
    val profile: Flow<UserProfileEntity?> = userProfileDao.getProfile()

    suspend fun saveProfile(profile: UserProfileEntity) = userProfileDao.upsert(profile)
}

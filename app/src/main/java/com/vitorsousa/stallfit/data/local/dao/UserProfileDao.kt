package com.vitorsousa.stallfit.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vitorsousa.stallfit.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = ${UserProfileEntity.SINGLETON_ID}")
    fun getProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(profile: UserProfileEntity)
}

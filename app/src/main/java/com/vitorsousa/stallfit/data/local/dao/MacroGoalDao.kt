package com.vitorsousa.stallfit.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vitorsousa.stallfit.data.local.entity.MacroGoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MacroGoalDao {
    @Query("SELECT * FROM macro_goal WHERE id = ${MacroGoalEntity.SINGLETON_ID}")
    fun getGoal(): Flow<MacroGoalEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(goal: MacroGoalEntity)
}

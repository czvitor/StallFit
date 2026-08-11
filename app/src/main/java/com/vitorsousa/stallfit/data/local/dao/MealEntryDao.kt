package com.vitorsousa.stallfit.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.vitorsousa.stallfit.data.local.entity.MealEntryEntity
import com.vitorsousa.stallfit.data.local.relation.MealEntryWithFood
import kotlinx.coroutines.flow.Flow

@Dao
interface MealEntryDao {
    @Insert
    suspend fun insert(mealEntry: MealEntryEntity): Long

    @Delete
    suspend fun delete(mealEntry: MealEntryEntity)

    @Query(
        """
        SELECT meal_entries.*, foods.name AS foodName, foods.caloriesPer100g,
               foods.proteinPer100g, foods.carbsPer100g, foods.fatPer100g
        FROM meal_entries
        JOIN foods ON foods.id = meal_entries.foodId
        WHERE meal_entries.dateEpochDay = :dateEpochDay
        ORDER BY meal_entries.loggedAt ASC
        """
    )
    fun getEntriesForDate(dateEpochDay: Long): Flow<List<MealEntryWithFood>>
}

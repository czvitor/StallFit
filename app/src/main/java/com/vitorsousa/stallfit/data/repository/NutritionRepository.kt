package com.vitorsousa.stallfit.data.repository

import com.vitorsousa.stallfit.data.local.dao.FoodDao
import com.vitorsousa.stallfit.data.local.dao.MacroGoalDao
import com.vitorsousa.stallfit.data.local.dao.MealEntryDao
import com.vitorsousa.stallfit.data.local.entity.FoodEntity
import com.vitorsousa.stallfit.data.local.entity.MacroGoalEntity
import com.vitorsousa.stallfit.data.local.entity.MealEntryEntity
import com.vitorsousa.stallfit.data.local.entity.MealType
import com.vitorsousa.stallfit.data.local.relation.MealEntryWithFood
import kotlinx.coroutines.flow.Flow

/** Single entry point for every read/write the UI needs from the nutrition tables. */
class NutritionRepository(
    private val foodDao: FoodDao,
    private val mealEntryDao: MealEntryDao,
    private val macroGoalDao: MacroGoalDao
) {
    val allFoods: Flow<List<FoodEntity>> = foodDao.getAll()
    val macroGoal: Flow<MacroGoalEntity?> = macroGoalDao.getGoal()

    fun getEntriesForDate(dateEpochDay: Long): Flow<List<MealEntryWithFood>> =
        mealEntryDao.getEntriesForDate(dateEpochDay)

    suspend fun logMeal(foodId: Long, mealType: MealType, grams: Double, dateEpochDay: Long) {
        mealEntryDao.insert(
            MealEntryEntity(
                foodId = foodId,
                mealType = mealType,
                grams = grams,
                dateEpochDay = dateEpochDay,
                loggedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun deleteMealEntry(entry: MealEntryEntity) = mealEntryDao.delete(entry)

    suspend fun addCustomFood(
        name: String,
        caloriesPer100g: Double,
        proteinPer100g: Double,
        carbsPer100g: Double,
        fatPer100g: Double
    ): Long = foodDao.insert(
        FoodEntity(
            name = name,
            caloriesPer100g = caloriesPer100g,
            proteinPer100g = proteinPer100g,
            carbsPer100g = carbsPer100g,
            fatPer100g = fatPer100g,
            isCustom = true
        )
    )

    suspend fun setMacroGoal(goal: MacroGoalEntity) = macroGoalDao.upsert(goal)
}

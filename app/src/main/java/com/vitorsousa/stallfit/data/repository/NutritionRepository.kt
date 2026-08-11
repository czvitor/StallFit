package com.vitorsousa.stallfit.data.repository

import com.vitorsousa.stallfit.data.local.dao.FoodDao
import com.vitorsousa.stallfit.data.local.dao.MacroGoalDao
import com.vitorsousa.stallfit.data.local.dao.MealDao
import com.vitorsousa.stallfit.data.local.entity.FoodEntity
import com.vitorsousa.stallfit.data.local.entity.MacroGoalEntity
import com.vitorsousa.stallfit.data.local.entity.MealEntity
import com.vitorsousa.stallfit.data.local.entity.MealFoodItemEntity
import com.vitorsousa.stallfit.data.local.entity.MealType
import com.vitorsousa.stallfit.data.local.relation.MealFoodItemWithFood
import com.vitorsousa.stallfit.data.local.relation.MealWithTotals
import kotlinx.coroutines.flow.Flow

/** Single entry point for every read/write the UI needs from the nutrition tables. */
class NutritionRepository(
    private val foodDao: FoodDao,
    private val mealDao: MealDao,
    private val macroGoalDao: MacroGoalDao
) {
    val allFoods: Flow<List<FoodEntity>> = foodDao.getAll()
    val macroGoal: Flow<MacroGoalEntity?> = macroGoalDao.getGoal()
    val mealsWithTotals: Flow<List<MealWithTotals>> = mealDao.getMealsWithTotals()

    fun getMealFlow(mealId: Long): Flow<MealEntity?> = mealDao.getMealFlow(mealId)

    fun getItemsForMeal(mealId: Long): Flow<List<MealFoodItemWithFood>> = mealDao.getItemsForMeal(mealId)

    suspend fun createMeal(name: String, mealType: MealType, items: List<MealFoodItemEntity>): Long =
        mealDao.insertMealWithItems(
            MealEntity(name = name, mealType = mealType, createdAt = System.currentTimeMillis()),
            items
        )

    suspend fun deleteMeal(mealId: Long) = mealDao.deleteMeal(mealId)

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

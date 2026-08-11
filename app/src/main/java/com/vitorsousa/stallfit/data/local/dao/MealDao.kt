package com.vitorsousa.stallfit.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.vitorsousa.stallfit.data.local.entity.MealEntity
import com.vitorsousa.stallfit.data.local.entity.MealFoodItemEntity
import com.vitorsousa.stallfit.data.local.relation.MealFoodItemWithFood
import com.vitorsousa.stallfit.data.local.relation.MealWithTotals
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Insert
    suspend fun insertMeal(meal: MealEntity): Long

    @Insert
    suspend fun insertItems(items: List<MealFoodItemEntity>)

    @Transaction
    suspend fun insertMealWithItems(meal: MealEntity, items: List<MealFoodItemEntity>): Long {
        val mealId = insertMeal(meal)
        insertItems(items.map { it.copy(mealId = mealId) })
        return mealId
    }

    @Query("DELETE FROM meals WHERE id = :mealId")
    suspend fun deleteMeal(mealId: Long)

    @Query("SELECT * FROM meals WHERE id = :mealId")
    fun getMealFlow(mealId: Long): Flow<MealEntity?>

    @Query(
        """
        SELECT meals.id AS mealId, meals.name AS mealName, meals.mealType AS mealType, meals.createdAt AS createdAt,
               COALESCE(SUM(foods.caloriesPer100g * meal_food_items.grams / 100.0), 0.0) AS totalCalories,
               COALESCE(SUM(foods.proteinPer100g * meal_food_items.grams / 100.0), 0.0) AS totalProtein,
               COALESCE(SUM(foods.carbsPer100g * meal_food_items.grams / 100.0), 0.0) AS totalCarbs,
               COALESCE(SUM(foods.fatPer100g * meal_food_items.grams / 100.0), 0.0) AS totalFat
        FROM meals
        LEFT JOIN meal_food_items ON meal_food_items.mealId = meals.id
        LEFT JOIN foods ON foods.id = meal_food_items.foodId
        GROUP BY meals.id
        ORDER BY meals.createdAt DESC
        """
    )
    fun getMealsWithTotals(): Flow<List<MealWithTotals>>

    @Query(
        """
        SELECT meal_food_items.*, foods.name AS foodName, foods.caloriesPer100g,
               foods.proteinPer100g, foods.carbsPer100g, foods.fatPer100g
        FROM meal_food_items
        JOIN foods ON foods.id = meal_food_items.foodId
        WHERE meal_food_items.mealId = :mealId
        """
    )
    fun getItemsForMeal(mealId: Long): Flow<List<MealFoodItemWithFood>>
}

package com.vitorsousa.stallfit.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.vitorsousa.stallfit.data.backup.MealsBackupData
import com.vitorsousa.stallfit.data.backup.ProfileBackupData
import com.vitorsousa.stallfit.data.backup.WorkoutsBackupData
import com.vitorsousa.stallfit.data.local.entity.BodyMeasurementEntity
import com.vitorsousa.stallfit.data.local.entity.ExerciseEntity
import com.vitorsousa.stallfit.data.local.entity.FoodEntity
import com.vitorsousa.stallfit.data.local.entity.MacroGoalEntity
import com.vitorsousa.stallfit.data.local.entity.MealEntity
import com.vitorsousa.stallfit.data.local.entity.MealFoodItemEntity
import com.vitorsousa.stallfit.data.local.entity.SetEntryEntity
import com.vitorsousa.stallfit.data.local.entity.TemplateExerciseEntity
import com.vitorsousa.stallfit.data.local.entity.UserProfileEntity
import com.vitorsousa.stallfit.data.local.entity.WorkoutSessionEntity
import com.vitorsousa.stallfit.data.local.entity.WorkoutTemplateEntity

/**
 * Dedicated DAO for the modular JSON backup feature — isolated from the per-feature DAOs above,
 * most of which don't expose a raw "every row" query. Delete/insert ordering inside the
 * `overwrite*` transactions always goes children-before-parents / parents-before-children, so
 * foreign keys never point at a row that doesn't exist yet (Room enforces FK constraints at the
 * SQLite level). Merge-mode row remapping (dedup by name, fresh autoGenerate ids) is orchestrated
 * in [com.vitorsousa.stallfit.data.repository.BackupRepository] instead of here, since it needs
 * per-row Kotlin logic that doesn't fit a single SQL statement.
 */
@Dao
interface BackupDao {
    @Query("SELECT * FROM exercises")
    suspend fun getAllExercises(): List<ExerciseEntity>

    @Query("SELECT * FROM foods")
    suspend fun getAllFoods(): List<FoodEntity>

    @Query("SELECT * FROM workout_templates")
    suspend fun getAllWorkoutTemplates(): List<WorkoutTemplateEntity>

    @Query("SELECT * FROM meals")
    suspend fun getAllMeals(): List<MealEntity>

    @Query("SELECT * FROM workout_sessions")
    suspend fun getAllWorkoutSessions(): List<WorkoutSessionEntity>

    @Query("SELECT * FROM template_exercises")
    suspend fun getAllTemplateExercises(): List<TemplateExerciseEntity>

    @Query("SELECT * FROM set_entries")
    suspend fun getAllSetEntries(): List<SetEntryEntity>

    @Query("SELECT * FROM meal_food_items")
    suspend fun getAllMealFoodItems(): List<MealFoodItemEntity>

    @Query("SELECT * FROM macro_goal")
    suspend fun getAllMacroGoals(): List<MacroGoalEntity>

    @Query("SELECT * FROM user_profile")
    suspend fun getAllUserProfiles(): List<UserProfileEntity>

    @Query("SELECT * FROM body_measurements")
    suspend fun getAllBodyMeasurements(): List<BodyMeasurementEntity>

    @Query("SELECT * FROM user_profile WHERE id = ${UserProfileEntity.SINGLETON_ID}")
    suspend fun getUserProfileOnce(): UserProfileEntity?

    @Query("SELECT * FROM macro_goal WHERE id = ${MacroGoalEntity.SINGLETON_ID}")
    suspend fun getMacroGoalOnce(): MacroGoalEntity?

    // Bulk inserts (overwrite mode) — os ids originais do backup são seguros de manter, já que a
    // tabela correspondente é sempre limpa antes, na mesma transação.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(items: List<ExerciseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoods(items: List<FoodEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutTemplates(items: List<WorkoutTemplateEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeals(items: List<MealEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutSessions(items: List<WorkoutSessionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemplateExercises(items: List<TemplateExerciseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetEntries(items: List<SetEntryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealFoodItems(items: List<MealFoodItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMacroGoals(items: List<MacroGoalEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfiles(items: List<UserProfileEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBodyMeasurements(items: List<BodyMeasurementEntity>)

    // Inserts de uma linha (merge mode) — chamados com `id = 0` para o Room deixar o SQLite gerar
    // um id novo, garantindo que a linha acrescentada nunca colida com uma linha local existente.
    @Insert
    suspend fun insertExercise(item: ExerciseEntity): Long

    @Insert
    suspend fun insertFood(item: FoodEntity): Long

    @Insert
    suspend fun insertWorkoutTemplate(item: WorkoutTemplateEntity): Long

    @Insert
    suspend fun insertWorkoutSession(item: WorkoutSessionEntity): Long

    @Insert
    suspend fun insertMeal(item: MealEntity): Long

    @Insert
    suspend fun insertBodyMeasurement(item: BodyMeasurementEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUserProfile(item: UserProfileEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMacroGoal(item: MacroGoalEntity)

    @Query("DELETE FROM set_entries")
    suspend fun clearSetEntries()

    @Query("DELETE FROM template_exercises")
    suspend fun clearTemplateExercises()

    @Query("DELETE FROM meal_food_items")
    suspend fun clearMealFoodItems()

    @Query("DELETE FROM workout_sessions")
    suspend fun clearWorkoutSessions()

    @Query("DELETE FROM meals")
    suspend fun clearMeals()

    @Query("DELETE FROM workout_templates")
    suspend fun clearWorkoutTemplates()

    @Query("DELETE FROM exercises")
    suspend fun clearExercises()

    @Query("DELETE FROM foods")
    suspend fun clearFoods()

    @Query("DELETE FROM macro_goal")
    suspend fun clearMacroGoals()

    @Query("DELETE FROM user_profile")
    suspend fun clearUserProfiles()

    @Query("DELETE FROM body_measurements")
    suspend fun clearBodyMeasurements()

    @Transaction
    suspend fun overwriteWorkouts(data: WorkoutsBackupData) {
        clearSetEntries()
        clearTemplateExercises()
        clearWorkoutSessions()
        clearWorkoutTemplates()
        clearExercises()
        insertExercises(data.exercises)
        insertWorkoutTemplates(data.workoutTemplates)
        insertTemplateExercises(data.templateExercises)
        insertWorkoutSessions(data.workoutSessions)
        insertSetEntries(data.setEntries)
    }

    @Transaction
    suspend fun overwriteMeals(data: MealsBackupData) {
        clearMealFoodItems()
        clearMeals()
        clearFoods()
        insertFoods(data.foods)
        insertMeals(data.meals)
        insertMealFoodItems(data.mealFoodItems)
    }

    @Transaction
    suspend fun overwriteProfile(data: ProfileBackupData) {
        clearBodyMeasurements()
        clearUserProfiles()
        clearMacroGoals()
        insertUserProfiles(data.userProfiles)
        insertMacroGoals(data.macroGoals)
        insertBodyMeasurements(data.bodyMeasurements)
    }
}

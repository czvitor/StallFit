package com.vitorsousa.stallfit.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vitorsousa.stallfit.data.local.dao.ExerciseDao
import com.vitorsousa.stallfit.data.local.dao.FoodDao
import com.vitorsousa.stallfit.data.local.dao.MacroGoalDao
import com.vitorsousa.stallfit.data.local.dao.MealEntryDao
import com.vitorsousa.stallfit.data.local.dao.SetEntryDao
import com.vitorsousa.stallfit.data.local.dao.WorkoutSessionDao
import com.vitorsousa.stallfit.data.local.entity.ExerciseEntity
import com.vitorsousa.stallfit.data.local.entity.FoodEntity
import com.vitorsousa.stallfit.data.local.entity.MacroGoalEntity
import com.vitorsousa.stallfit.data.local.entity.MealEntryEntity
import com.vitorsousa.stallfit.data.local.entity.SetEntryEntity
import com.vitorsousa.stallfit.data.local.entity.WorkoutSessionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Database(
    entities = [
        ExerciseEntity::class,
        WorkoutSessionEntity::class,
        SetEntryEntity::class,
        FoodEntity::class,
        MealEntryEntity::class,
        MacroGoalEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class StallFitDatabase : RoomDatabase() {

    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutSessionDao(): WorkoutSessionDao
    abstract fun setEntryDao(): SetEntryDao
    abstract fun foodDao(): FoodDao
    abstract fun mealEntryDao(): MealEntryDao
    abstract fun macroGoalDao(): MacroGoalDao

    companion object {
        @Volatile
        private var INSTANCE: StallFitDatabase? = null

        fun getInstance(context: Context): StallFitDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    StallFitDatabase::class.java,
                    "stallfit.db"
                )
                    .addCallback(seedCallback)
                    .build()
                    .also { INSTANCE = it }
            }
        }

        /**
         * Populates default exercises, a starter food list, and a default macro goal the first
         * time the database file is created. Runs on a background scope since Room callbacks
         * execute on the database's own thread and must not block it with a runBlocking call.
         */
        private val seedCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                val database = INSTANCE ?: return
                CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                    database.exerciseDao().insertAll(DefaultSeedData.exercises)
                    database.foodDao().insertAll(DefaultSeedData.foods)
                    database.macroGoalDao().upsert(DefaultSeedData.defaultMacroGoal)
                }
            }
        }
    }
}

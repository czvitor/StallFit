package com.vitorsousa.stallfit.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vitorsousa.stallfit.data.local.dao.BodyMeasurementDao
import com.vitorsousa.stallfit.data.local.dao.ExerciseDao
import com.vitorsousa.stallfit.data.local.dao.FoodDao
import com.vitorsousa.stallfit.data.local.dao.MacroGoalDao
import com.vitorsousa.stallfit.data.local.dao.MealDao
import com.vitorsousa.stallfit.data.local.dao.SetEntryDao
import com.vitorsousa.stallfit.data.local.dao.UserProfileDao
import com.vitorsousa.stallfit.data.local.dao.WorkoutSessionDao
import com.vitorsousa.stallfit.data.local.dao.WorkoutTemplateDao
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
        MacroGoalEntity::class,
        UserProfileEntity::class,
        WorkoutTemplateEntity::class,
        TemplateExerciseEntity::class,
        MealEntity::class,
        MealFoodItemEntity::class,
        BodyMeasurementEntity::class
    ],
    version = 3,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class StallFitDatabase : RoomDatabase() {

    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutSessionDao(): WorkoutSessionDao
    abstract fun setEntryDao(): SetEntryDao
    abstract fun foodDao(): FoodDao
    abstract fun macroGoalDao(): MacroGoalDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun workoutTemplateDao(): WorkoutTemplateDao
    abstract fun mealDao(): MealDao
    abstract fun bodyMeasurementDao(): BodyMeasurementDao

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
                    // Pre-release app with no real user data to preserve — a schema bump just
                    // wipes and reseeds rather than carrying hand-written Migration objects.
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }

        /**
         * Populates default exercises, a starter food list, and a default macro goal. Runs on
         * a background scope since Room callbacks execute on the database's own thread and must
         * not block it with a runBlocking call.
         *
         * Seeding is checked on every [onOpen], not just [onCreate]: `fallbackToDestructiveMigration()`
         * drops and recreates tables on a version bump for an already-installed app, but
         * SQLiteOpenHelper only invokes `onCreate` for a brand-new database file — never for that
         * upgrade path — so a schema bump alone would otherwise leave the tables permanently
         * empty. The `count() == 0` guard keeps this a no-op once real data exists.
         */
        private val seedCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                seedIfEmpty()
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                seedIfEmpty()
            }

            private fun seedIfEmpty() {
                val database = INSTANCE ?: return
                CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                    if (database.exerciseDao().count() == 0) {
                        database.exerciseDao().insertAll(DefaultSeedData.exercises)
                    }
                    if (database.foodDao().count() == 0) {
                        database.foodDao().insertAll(DefaultSeedData.foods)
                    }
                    if (database.macroGoalDao().getGoalOnce() == null) {
                        database.macroGoalDao().upsert(DefaultSeedData.defaultMacroGoal)
                    }
                }
            }
        }
    }
}

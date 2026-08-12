package com.vitorsousa.stallfit.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.vitorsousa.stallfit.data.local.dao.BodyMeasurementDao;
import com.vitorsousa.stallfit.data.local.dao.BodyMeasurementDao_Impl;
import com.vitorsousa.stallfit.data.local.dao.ExerciseDao;
import com.vitorsousa.stallfit.data.local.dao.ExerciseDao_Impl;
import com.vitorsousa.stallfit.data.local.dao.FoodDao;
import com.vitorsousa.stallfit.data.local.dao.FoodDao_Impl;
import com.vitorsousa.stallfit.data.local.dao.MacroGoalDao;
import com.vitorsousa.stallfit.data.local.dao.MacroGoalDao_Impl;
import com.vitorsousa.stallfit.data.local.dao.MealDao;
import com.vitorsousa.stallfit.data.local.dao.MealDao_Impl;
import com.vitorsousa.stallfit.data.local.dao.SetEntryDao;
import com.vitorsousa.stallfit.data.local.dao.SetEntryDao_Impl;
import com.vitorsousa.stallfit.data.local.dao.UserProfileDao;
import com.vitorsousa.stallfit.data.local.dao.UserProfileDao_Impl;
import com.vitorsousa.stallfit.data.local.dao.WorkoutSessionDao;
import com.vitorsousa.stallfit.data.local.dao.WorkoutSessionDao_Impl;
import com.vitorsousa.stallfit.data.local.dao.WorkoutTemplateDao;
import com.vitorsousa.stallfit.data.local.dao.WorkoutTemplateDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class StallFitDatabase_Impl extends StallFitDatabase {
  private volatile ExerciseDao _exerciseDao;

  private volatile WorkoutSessionDao _workoutSessionDao;

  private volatile SetEntryDao _setEntryDao;

  private volatile FoodDao _foodDao;

  private volatile MacroGoalDao _macroGoalDao;

  private volatile UserProfileDao _userProfileDao;

  private volatile WorkoutTemplateDao _workoutTemplateDao;

  private volatile MealDao _mealDao;

  private volatile BodyMeasurementDao _bodyMeasurementDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `exercises` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `muscleGroup` TEXT NOT NULL, `equipment` TEXT NOT NULL, `isCustom` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `workout_sessions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `startedAt` INTEGER NOT NULL, `finishedAt` INTEGER, `templateId` INTEGER, FOREIGN KEY(`templateId`) REFERENCES `workout_templates`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_workout_sessions_templateId` ON `workout_sessions` (`templateId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `set_entries` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `sessionId` INTEGER NOT NULL, `exerciseId` INTEGER NOT NULL, `setNumber` INTEGER NOT NULL, `reps` INTEGER NOT NULL, `weightKg` REAL NOT NULL, `loggedAt` INTEGER NOT NULL, FOREIGN KEY(`sessionId`) REFERENCES `workout_sessions`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`exerciseId`) REFERENCES `exercises`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_set_entries_sessionId` ON `set_entries` (`sessionId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_set_entries_exerciseId` ON `set_entries` (`exerciseId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `foods` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `caloriesPer100g` REAL NOT NULL, `proteinPer100g` REAL NOT NULL, `carbsPer100g` REAL NOT NULL, `fatPer100g` REAL NOT NULL, `isCustom` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `macro_goal` (`id` INTEGER NOT NULL, `calorieGoal` INTEGER NOT NULL, `proteinGoal` INTEGER NOT NULL, `carbGoal` INTEGER NOT NULL, `fatGoal` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `user_profile` (`id` INTEGER NOT NULL, `name` TEXT NOT NULL, `ageYears` INTEGER NOT NULL, `sex` TEXT NOT NULL, `activityLevel` TEXT NOT NULL, `goal` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `workout_templates` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `template_exercises` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `templateId` INTEGER NOT NULL, `exerciseId` INTEGER NOT NULL, `orderIndex` INTEGER NOT NULL, `sets` INTEGER NOT NULL, `repRangeMin` INTEGER NOT NULL, `repRangeMax` INTEGER NOT NULL, `restSeconds` INTEGER NOT NULL, `intensity` TEXT NOT NULL, FOREIGN KEY(`templateId`) REFERENCES `workout_templates`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`exerciseId`) REFERENCES `exercises`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_template_exercises_templateId` ON `template_exercises` (`templateId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_template_exercises_exerciseId` ON `template_exercises` (`exerciseId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `meals` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `mealType` TEXT NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `meal_food_items` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `mealId` INTEGER NOT NULL, `foodId` INTEGER NOT NULL, `grams` REAL NOT NULL, FOREIGN KEY(`mealId`) REFERENCES `meals`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`foodId`) REFERENCES `foods`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_meal_food_items_mealId` ON `meal_food_items` (`mealId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_meal_food_items_foodId` ON `meal_food_items` (`foodId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `body_measurements` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `createdAt` INTEGER NOT NULL, `weightKg` REAL NOT NULL, `heightCm` REAL NOT NULL, `chestCm` REAL, `waistCm` REAL, `abdomenCm` REAL, `hipCm` REAL, `armLeftRelaxedCm` REAL, `armLeftFlexedCm` REAL, `armRightRelaxedCm` REAL, `armRightFlexedCm` REAL, `forearmLeftCm` REAL, `forearmRightCm` REAL, `thighLeftCm` REAL, `thighRightCm` REAL, `calfLeftCm` REAL, `calfRightCm` REAL, `bodyFatPercent` REAL, `leanMassKg` REAL, `fatMassKg` REAL, `bodyWaterPercent` REAL, `tricepsFoldMm` REAL, `subscapularFoldMm` REAL, `suprailiacFoldMm` REAL, `abdominalFoldMm` REAL, `thighFoldMm` REAL, `chestFoldMm` REAL, `midaxillaryFoldMm` REAL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'c4e9df99477f0ee911dbdf695204ea78')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `exercises`");
        db.execSQL("DROP TABLE IF EXISTS `workout_sessions`");
        db.execSQL("DROP TABLE IF EXISTS `set_entries`");
        db.execSQL("DROP TABLE IF EXISTS `foods`");
        db.execSQL("DROP TABLE IF EXISTS `macro_goal`");
        db.execSQL("DROP TABLE IF EXISTS `user_profile`");
        db.execSQL("DROP TABLE IF EXISTS `workout_templates`");
        db.execSQL("DROP TABLE IF EXISTS `template_exercises`");
        db.execSQL("DROP TABLE IF EXISTS `meals`");
        db.execSQL("DROP TABLE IF EXISTS `meal_food_items`");
        db.execSQL("DROP TABLE IF EXISTS `body_measurements`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsExercises = new HashMap<String, TableInfo.Column>(5);
        _columnsExercises.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("muscleGroup", new TableInfo.Column("muscleGroup", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("equipment", new TableInfo.Column("equipment", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("isCustom", new TableInfo.Column("isCustom", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysExercises = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesExercises = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoExercises = new TableInfo("exercises", _columnsExercises, _foreignKeysExercises, _indicesExercises);
        final TableInfo _existingExercises = TableInfo.read(db, "exercises");
        if (!_infoExercises.equals(_existingExercises)) {
          return new RoomOpenHelper.ValidationResult(false, "exercises(com.vitorsousa.stallfit.data.local.entity.ExerciseEntity).\n"
                  + " Expected:\n" + _infoExercises + "\n"
                  + " Found:\n" + _existingExercises);
        }
        final HashMap<String, TableInfo.Column> _columnsWorkoutSessions = new HashMap<String, TableInfo.Column>(5);
        _columnsWorkoutSessions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutSessions.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutSessions.put("startedAt", new TableInfo.Column("startedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutSessions.put("finishedAt", new TableInfo.Column("finishedAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutSessions.put("templateId", new TableInfo.Column("templateId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysWorkoutSessions = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysWorkoutSessions.add(new TableInfo.ForeignKey("workout_templates", "SET NULL", "NO ACTION", Arrays.asList("templateId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesWorkoutSessions = new HashSet<TableInfo.Index>(1);
        _indicesWorkoutSessions.add(new TableInfo.Index("index_workout_sessions_templateId", false, Arrays.asList("templateId"), Arrays.asList("ASC")));
        final TableInfo _infoWorkoutSessions = new TableInfo("workout_sessions", _columnsWorkoutSessions, _foreignKeysWorkoutSessions, _indicesWorkoutSessions);
        final TableInfo _existingWorkoutSessions = TableInfo.read(db, "workout_sessions");
        if (!_infoWorkoutSessions.equals(_existingWorkoutSessions)) {
          return new RoomOpenHelper.ValidationResult(false, "workout_sessions(com.vitorsousa.stallfit.data.local.entity.WorkoutSessionEntity).\n"
                  + " Expected:\n" + _infoWorkoutSessions + "\n"
                  + " Found:\n" + _existingWorkoutSessions);
        }
        final HashMap<String, TableInfo.Column> _columnsSetEntries = new HashMap<String, TableInfo.Column>(7);
        _columnsSetEntries.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSetEntries.put("sessionId", new TableInfo.Column("sessionId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSetEntries.put("exerciseId", new TableInfo.Column("exerciseId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSetEntries.put("setNumber", new TableInfo.Column("setNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSetEntries.put("reps", new TableInfo.Column("reps", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSetEntries.put("weightKg", new TableInfo.Column("weightKg", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSetEntries.put("loggedAt", new TableInfo.Column("loggedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSetEntries = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysSetEntries.add(new TableInfo.ForeignKey("workout_sessions", "CASCADE", "NO ACTION", Arrays.asList("sessionId"), Arrays.asList("id")));
        _foreignKeysSetEntries.add(new TableInfo.ForeignKey("exercises", "CASCADE", "NO ACTION", Arrays.asList("exerciseId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesSetEntries = new HashSet<TableInfo.Index>(2);
        _indicesSetEntries.add(new TableInfo.Index("index_set_entries_sessionId", false, Arrays.asList("sessionId"), Arrays.asList("ASC")));
        _indicesSetEntries.add(new TableInfo.Index("index_set_entries_exerciseId", false, Arrays.asList("exerciseId"), Arrays.asList("ASC")));
        final TableInfo _infoSetEntries = new TableInfo("set_entries", _columnsSetEntries, _foreignKeysSetEntries, _indicesSetEntries);
        final TableInfo _existingSetEntries = TableInfo.read(db, "set_entries");
        if (!_infoSetEntries.equals(_existingSetEntries)) {
          return new RoomOpenHelper.ValidationResult(false, "set_entries(com.vitorsousa.stallfit.data.local.entity.SetEntryEntity).\n"
                  + " Expected:\n" + _infoSetEntries + "\n"
                  + " Found:\n" + _existingSetEntries);
        }
        final HashMap<String, TableInfo.Column> _columnsFoods = new HashMap<String, TableInfo.Column>(7);
        _columnsFoods.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoods.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoods.put("caloriesPer100g", new TableInfo.Column("caloriesPer100g", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoods.put("proteinPer100g", new TableInfo.Column("proteinPer100g", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoods.put("carbsPer100g", new TableInfo.Column("carbsPer100g", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoods.put("fatPer100g", new TableInfo.Column("fatPer100g", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoods.put("isCustom", new TableInfo.Column("isCustom", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFoods = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFoods = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFoods = new TableInfo("foods", _columnsFoods, _foreignKeysFoods, _indicesFoods);
        final TableInfo _existingFoods = TableInfo.read(db, "foods");
        if (!_infoFoods.equals(_existingFoods)) {
          return new RoomOpenHelper.ValidationResult(false, "foods(com.vitorsousa.stallfit.data.local.entity.FoodEntity).\n"
                  + " Expected:\n" + _infoFoods + "\n"
                  + " Found:\n" + _existingFoods);
        }
        final HashMap<String, TableInfo.Column> _columnsMacroGoal = new HashMap<String, TableInfo.Column>(5);
        _columnsMacroGoal.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMacroGoal.put("calorieGoal", new TableInfo.Column("calorieGoal", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMacroGoal.put("proteinGoal", new TableInfo.Column("proteinGoal", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMacroGoal.put("carbGoal", new TableInfo.Column("carbGoal", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMacroGoal.put("fatGoal", new TableInfo.Column("fatGoal", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMacroGoal = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMacroGoal = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMacroGoal = new TableInfo("macro_goal", _columnsMacroGoal, _foreignKeysMacroGoal, _indicesMacroGoal);
        final TableInfo _existingMacroGoal = TableInfo.read(db, "macro_goal");
        if (!_infoMacroGoal.equals(_existingMacroGoal)) {
          return new RoomOpenHelper.ValidationResult(false, "macro_goal(com.vitorsousa.stallfit.data.local.entity.MacroGoalEntity).\n"
                  + " Expected:\n" + _infoMacroGoal + "\n"
                  + " Found:\n" + _existingMacroGoal);
        }
        final HashMap<String, TableInfo.Column> _columnsUserProfile = new HashMap<String, TableInfo.Column>(6);
        _columnsUserProfile.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("ageYears", new TableInfo.Column("ageYears", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("sex", new TableInfo.Column("sex", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("activityLevel", new TableInfo.Column("activityLevel", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("goal", new TableInfo.Column("goal", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUserProfile = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUserProfile = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUserProfile = new TableInfo("user_profile", _columnsUserProfile, _foreignKeysUserProfile, _indicesUserProfile);
        final TableInfo _existingUserProfile = TableInfo.read(db, "user_profile");
        if (!_infoUserProfile.equals(_existingUserProfile)) {
          return new RoomOpenHelper.ValidationResult(false, "user_profile(com.vitorsousa.stallfit.data.local.entity.UserProfileEntity).\n"
                  + " Expected:\n" + _infoUserProfile + "\n"
                  + " Found:\n" + _existingUserProfile);
        }
        final HashMap<String, TableInfo.Column> _columnsWorkoutTemplates = new HashMap<String, TableInfo.Column>(3);
        _columnsWorkoutTemplates.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutTemplates.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutTemplates.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysWorkoutTemplates = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesWorkoutTemplates = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoWorkoutTemplates = new TableInfo("workout_templates", _columnsWorkoutTemplates, _foreignKeysWorkoutTemplates, _indicesWorkoutTemplates);
        final TableInfo _existingWorkoutTemplates = TableInfo.read(db, "workout_templates");
        if (!_infoWorkoutTemplates.equals(_existingWorkoutTemplates)) {
          return new RoomOpenHelper.ValidationResult(false, "workout_templates(com.vitorsousa.stallfit.data.local.entity.WorkoutTemplateEntity).\n"
                  + " Expected:\n" + _infoWorkoutTemplates + "\n"
                  + " Found:\n" + _existingWorkoutTemplates);
        }
        final HashMap<String, TableInfo.Column> _columnsTemplateExercises = new HashMap<String, TableInfo.Column>(9);
        _columnsTemplateExercises.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTemplateExercises.put("templateId", new TableInfo.Column("templateId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTemplateExercises.put("exerciseId", new TableInfo.Column("exerciseId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTemplateExercises.put("orderIndex", new TableInfo.Column("orderIndex", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTemplateExercises.put("sets", new TableInfo.Column("sets", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTemplateExercises.put("repRangeMin", new TableInfo.Column("repRangeMin", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTemplateExercises.put("repRangeMax", new TableInfo.Column("repRangeMax", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTemplateExercises.put("restSeconds", new TableInfo.Column("restSeconds", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTemplateExercises.put("intensity", new TableInfo.Column("intensity", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTemplateExercises = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysTemplateExercises.add(new TableInfo.ForeignKey("workout_templates", "CASCADE", "NO ACTION", Arrays.asList("templateId"), Arrays.asList("id")));
        _foreignKeysTemplateExercises.add(new TableInfo.ForeignKey("exercises", "CASCADE", "NO ACTION", Arrays.asList("exerciseId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesTemplateExercises = new HashSet<TableInfo.Index>(2);
        _indicesTemplateExercises.add(new TableInfo.Index("index_template_exercises_templateId", false, Arrays.asList("templateId"), Arrays.asList("ASC")));
        _indicesTemplateExercises.add(new TableInfo.Index("index_template_exercises_exerciseId", false, Arrays.asList("exerciseId"), Arrays.asList("ASC")));
        final TableInfo _infoTemplateExercises = new TableInfo("template_exercises", _columnsTemplateExercises, _foreignKeysTemplateExercises, _indicesTemplateExercises);
        final TableInfo _existingTemplateExercises = TableInfo.read(db, "template_exercises");
        if (!_infoTemplateExercises.equals(_existingTemplateExercises)) {
          return new RoomOpenHelper.ValidationResult(false, "template_exercises(com.vitorsousa.stallfit.data.local.entity.TemplateExerciseEntity).\n"
                  + " Expected:\n" + _infoTemplateExercises + "\n"
                  + " Found:\n" + _existingTemplateExercises);
        }
        final HashMap<String, TableInfo.Column> _columnsMeals = new HashMap<String, TableInfo.Column>(4);
        _columnsMeals.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeals.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeals.put("mealType", new TableInfo.Column("mealType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeals.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMeals = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMeals = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMeals = new TableInfo("meals", _columnsMeals, _foreignKeysMeals, _indicesMeals);
        final TableInfo _existingMeals = TableInfo.read(db, "meals");
        if (!_infoMeals.equals(_existingMeals)) {
          return new RoomOpenHelper.ValidationResult(false, "meals(com.vitorsousa.stallfit.data.local.entity.MealEntity).\n"
                  + " Expected:\n" + _infoMeals + "\n"
                  + " Found:\n" + _existingMeals);
        }
        final HashMap<String, TableInfo.Column> _columnsMealFoodItems = new HashMap<String, TableInfo.Column>(4);
        _columnsMealFoodItems.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealFoodItems.put("mealId", new TableInfo.Column("mealId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealFoodItems.put("foodId", new TableInfo.Column("foodId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealFoodItems.put("grams", new TableInfo.Column("grams", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMealFoodItems = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysMealFoodItems.add(new TableInfo.ForeignKey("meals", "CASCADE", "NO ACTION", Arrays.asList("mealId"), Arrays.asList("id")));
        _foreignKeysMealFoodItems.add(new TableInfo.ForeignKey("foods", "CASCADE", "NO ACTION", Arrays.asList("foodId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesMealFoodItems = new HashSet<TableInfo.Index>(2);
        _indicesMealFoodItems.add(new TableInfo.Index("index_meal_food_items_mealId", false, Arrays.asList("mealId"), Arrays.asList("ASC")));
        _indicesMealFoodItems.add(new TableInfo.Index("index_meal_food_items_foodId", false, Arrays.asList("foodId"), Arrays.asList("ASC")));
        final TableInfo _infoMealFoodItems = new TableInfo("meal_food_items", _columnsMealFoodItems, _foreignKeysMealFoodItems, _indicesMealFoodItems);
        final TableInfo _existingMealFoodItems = TableInfo.read(db, "meal_food_items");
        if (!_infoMealFoodItems.equals(_existingMealFoodItems)) {
          return new RoomOpenHelper.ValidationResult(false, "meal_food_items(com.vitorsousa.stallfit.data.local.entity.MealFoodItemEntity).\n"
                  + " Expected:\n" + _infoMealFoodItems + "\n"
                  + " Found:\n" + _existingMealFoodItems);
        }
        final HashMap<String, TableInfo.Column> _columnsBodyMeasurements = new HashMap<String, TableInfo.Column>(29);
        _columnsBodyMeasurements.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("weightKg", new TableInfo.Column("weightKg", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("heightCm", new TableInfo.Column("heightCm", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("chestCm", new TableInfo.Column("chestCm", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("waistCm", new TableInfo.Column("waistCm", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("abdomenCm", new TableInfo.Column("abdomenCm", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("hipCm", new TableInfo.Column("hipCm", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("armLeftRelaxedCm", new TableInfo.Column("armLeftRelaxedCm", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("armLeftFlexedCm", new TableInfo.Column("armLeftFlexedCm", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("armRightRelaxedCm", new TableInfo.Column("armRightRelaxedCm", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("armRightFlexedCm", new TableInfo.Column("armRightFlexedCm", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("forearmLeftCm", new TableInfo.Column("forearmLeftCm", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("forearmRightCm", new TableInfo.Column("forearmRightCm", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("thighLeftCm", new TableInfo.Column("thighLeftCm", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("thighRightCm", new TableInfo.Column("thighRightCm", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("calfLeftCm", new TableInfo.Column("calfLeftCm", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("calfRightCm", new TableInfo.Column("calfRightCm", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("bodyFatPercent", new TableInfo.Column("bodyFatPercent", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("leanMassKg", new TableInfo.Column("leanMassKg", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("fatMassKg", new TableInfo.Column("fatMassKg", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("bodyWaterPercent", new TableInfo.Column("bodyWaterPercent", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("tricepsFoldMm", new TableInfo.Column("tricepsFoldMm", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("subscapularFoldMm", new TableInfo.Column("subscapularFoldMm", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("suprailiacFoldMm", new TableInfo.Column("suprailiacFoldMm", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("abdominalFoldMm", new TableInfo.Column("abdominalFoldMm", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("thighFoldMm", new TableInfo.Column("thighFoldMm", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("chestFoldMm", new TableInfo.Column("chestFoldMm", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodyMeasurements.put("midaxillaryFoldMm", new TableInfo.Column("midaxillaryFoldMm", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBodyMeasurements = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBodyMeasurements = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBodyMeasurements = new TableInfo("body_measurements", _columnsBodyMeasurements, _foreignKeysBodyMeasurements, _indicesBodyMeasurements);
        final TableInfo _existingBodyMeasurements = TableInfo.read(db, "body_measurements");
        if (!_infoBodyMeasurements.equals(_existingBodyMeasurements)) {
          return new RoomOpenHelper.ValidationResult(false, "body_measurements(com.vitorsousa.stallfit.data.local.entity.BodyMeasurementEntity).\n"
                  + " Expected:\n" + _infoBodyMeasurements + "\n"
                  + " Found:\n" + _existingBodyMeasurements);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "c4e9df99477f0ee911dbdf695204ea78", "08d770797c19b3d7e45f59ff7724a2f8");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "exercises","workout_sessions","set_entries","foods","macro_goal","user_profile","workout_templates","template_exercises","meals","meal_food_items","body_measurements");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `exercises`");
      _db.execSQL("DELETE FROM `workout_sessions`");
      _db.execSQL("DELETE FROM `set_entries`");
      _db.execSQL("DELETE FROM `foods`");
      _db.execSQL("DELETE FROM `macro_goal`");
      _db.execSQL("DELETE FROM `user_profile`");
      _db.execSQL("DELETE FROM `workout_templates`");
      _db.execSQL("DELETE FROM `template_exercises`");
      _db.execSQL("DELETE FROM `meals`");
      _db.execSQL("DELETE FROM `meal_food_items`");
      _db.execSQL("DELETE FROM `body_measurements`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(ExerciseDao.class, ExerciseDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(WorkoutSessionDao.class, WorkoutSessionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SetEntryDao.class, SetEntryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FoodDao.class, FoodDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MacroGoalDao.class, MacroGoalDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(UserProfileDao.class, UserProfileDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(WorkoutTemplateDao.class, WorkoutTemplateDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MealDao.class, MealDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BodyMeasurementDao.class, BodyMeasurementDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public ExerciseDao exerciseDao() {
    if (_exerciseDao != null) {
      return _exerciseDao;
    } else {
      synchronized(this) {
        if(_exerciseDao == null) {
          _exerciseDao = new ExerciseDao_Impl(this);
        }
        return _exerciseDao;
      }
    }
  }

  @Override
  public WorkoutSessionDao workoutSessionDao() {
    if (_workoutSessionDao != null) {
      return _workoutSessionDao;
    } else {
      synchronized(this) {
        if(_workoutSessionDao == null) {
          _workoutSessionDao = new WorkoutSessionDao_Impl(this);
        }
        return _workoutSessionDao;
      }
    }
  }

  @Override
  public SetEntryDao setEntryDao() {
    if (_setEntryDao != null) {
      return _setEntryDao;
    } else {
      synchronized(this) {
        if(_setEntryDao == null) {
          _setEntryDao = new SetEntryDao_Impl(this);
        }
        return _setEntryDao;
      }
    }
  }

  @Override
  public FoodDao foodDao() {
    if (_foodDao != null) {
      return _foodDao;
    } else {
      synchronized(this) {
        if(_foodDao == null) {
          _foodDao = new FoodDao_Impl(this);
        }
        return _foodDao;
      }
    }
  }

  @Override
  public MacroGoalDao macroGoalDao() {
    if (_macroGoalDao != null) {
      return _macroGoalDao;
    } else {
      synchronized(this) {
        if(_macroGoalDao == null) {
          _macroGoalDao = new MacroGoalDao_Impl(this);
        }
        return _macroGoalDao;
      }
    }
  }

  @Override
  public UserProfileDao userProfileDao() {
    if (_userProfileDao != null) {
      return _userProfileDao;
    } else {
      synchronized(this) {
        if(_userProfileDao == null) {
          _userProfileDao = new UserProfileDao_Impl(this);
        }
        return _userProfileDao;
      }
    }
  }

  @Override
  public WorkoutTemplateDao workoutTemplateDao() {
    if (_workoutTemplateDao != null) {
      return _workoutTemplateDao;
    } else {
      synchronized(this) {
        if(_workoutTemplateDao == null) {
          _workoutTemplateDao = new WorkoutTemplateDao_Impl(this);
        }
        return _workoutTemplateDao;
      }
    }
  }

  @Override
  public MealDao mealDao() {
    if (_mealDao != null) {
      return _mealDao;
    } else {
      synchronized(this) {
        if(_mealDao == null) {
          _mealDao = new MealDao_Impl(this);
        }
        return _mealDao;
      }
    }
  }

  @Override
  public BodyMeasurementDao bodyMeasurementDao() {
    if (_bodyMeasurementDao != null) {
      return _bodyMeasurementDao;
    } else {
      synchronized(this) {
        if(_bodyMeasurementDao == null) {
          _bodyMeasurementDao = new BodyMeasurementDao_Impl(this);
        }
        return _bodyMeasurementDao;
      }
    }
  }
}

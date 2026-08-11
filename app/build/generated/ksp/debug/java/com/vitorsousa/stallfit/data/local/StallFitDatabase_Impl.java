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
import com.vitorsousa.stallfit.data.local.dao.ExerciseDao;
import com.vitorsousa.stallfit.data.local.dao.ExerciseDao_Impl;
import com.vitorsousa.stallfit.data.local.dao.FoodDao;
import com.vitorsousa.stallfit.data.local.dao.FoodDao_Impl;
import com.vitorsousa.stallfit.data.local.dao.MacroGoalDao;
import com.vitorsousa.stallfit.data.local.dao.MacroGoalDao_Impl;
import com.vitorsousa.stallfit.data.local.dao.MealEntryDao;
import com.vitorsousa.stallfit.data.local.dao.MealEntryDao_Impl;
import com.vitorsousa.stallfit.data.local.dao.SetEntryDao;
import com.vitorsousa.stallfit.data.local.dao.SetEntryDao_Impl;
import com.vitorsousa.stallfit.data.local.dao.WorkoutSessionDao;
import com.vitorsousa.stallfit.data.local.dao.WorkoutSessionDao_Impl;
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

  private volatile MealEntryDao _mealEntryDao;

  private volatile MacroGoalDao _macroGoalDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `exercises` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `muscleGroup` TEXT NOT NULL, `isCustom` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `workout_sessions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `startedAt` INTEGER NOT NULL, `finishedAt` INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `set_entries` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `sessionId` INTEGER NOT NULL, `exerciseId` INTEGER NOT NULL, `setNumber` INTEGER NOT NULL, `reps` INTEGER NOT NULL, `weightKg` REAL NOT NULL, `loggedAt` INTEGER NOT NULL, FOREIGN KEY(`sessionId`) REFERENCES `workout_sessions`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`exerciseId`) REFERENCES `exercises`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_set_entries_sessionId` ON `set_entries` (`sessionId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_set_entries_exerciseId` ON `set_entries` (`exerciseId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `foods` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `caloriesPer100g` REAL NOT NULL, `proteinPer100g` REAL NOT NULL, `carbsPer100g` REAL NOT NULL, `fatPer100g` REAL NOT NULL, `isCustom` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `meal_entries` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `foodId` INTEGER NOT NULL, `mealType` TEXT NOT NULL, `grams` REAL NOT NULL, `dateEpochDay` INTEGER NOT NULL, `loggedAt` INTEGER NOT NULL, FOREIGN KEY(`foodId`) REFERENCES `foods`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_meal_entries_foodId` ON `meal_entries` (`foodId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_meal_entries_dateEpochDay` ON `meal_entries` (`dateEpochDay`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `macro_goal` (`id` INTEGER NOT NULL, `calorieGoal` INTEGER NOT NULL, `proteinGoal` INTEGER NOT NULL, `carbGoal` INTEGER NOT NULL, `fatGoal` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'db4c0c25ac048f21a726ce27548de1d3')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `exercises`");
        db.execSQL("DROP TABLE IF EXISTS `workout_sessions`");
        db.execSQL("DROP TABLE IF EXISTS `set_entries`");
        db.execSQL("DROP TABLE IF EXISTS `foods`");
        db.execSQL("DROP TABLE IF EXISTS `meal_entries`");
        db.execSQL("DROP TABLE IF EXISTS `macro_goal`");
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
        final HashMap<String, TableInfo.Column> _columnsExercises = new HashMap<String, TableInfo.Column>(4);
        _columnsExercises.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("muscleGroup", new TableInfo.Column("muscleGroup", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
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
        final HashMap<String, TableInfo.Column> _columnsWorkoutSessions = new HashMap<String, TableInfo.Column>(4);
        _columnsWorkoutSessions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutSessions.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutSessions.put("startedAt", new TableInfo.Column("startedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutSessions.put("finishedAt", new TableInfo.Column("finishedAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysWorkoutSessions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesWorkoutSessions = new HashSet<TableInfo.Index>(0);
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
        final HashMap<String, TableInfo.Column> _columnsMealEntries = new HashMap<String, TableInfo.Column>(6);
        _columnsMealEntries.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealEntries.put("foodId", new TableInfo.Column("foodId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealEntries.put("mealType", new TableInfo.Column("mealType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealEntries.put("grams", new TableInfo.Column("grams", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealEntries.put("dateEpochDay", new TableInfo.Column("dateEpochDay", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealEntries.put("loggedAt", new TableInfo.Column("loggedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMealEntries = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysMealEntries.add(new TableInfo.ForeignKey("foods", "CASCADE", "NO ACTION", Arrays.asList("foodId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesMealEntries = new HashSet<TableInfo.Index>(2);
        _indicesMealEntries.add(new TableInfo.Index("index_meal_entries_foodId", false, Arrays.asList("foodId"), Arrays.asList("ASC")));
        _indicesMealEntries.add(new TableInfo.Index("index_meal_entries_dateEpochDay", false, Arrays.asList("dateEpochDay"), Arrays.asList("ASC")));
        final TableInfo _infoMealEntries = new TableInfo("meal_entries", _columnsMealEntries, _foreignKeysMealEntries, _indicesMealEntries);
        final TableInfo _existingMealEntries = TableInfo.read(db, "meal_entries");
        if (!_infoMealEntries.equals(_existingMealEntries)) {
          return new RoomOpenHelper.ValidationResult(false, "meal_entries(com.vitorsousa.stallfit.data.local.entity.MealEntryEntity).\n"
                  + " Expected:\n" + _infoMealEntries + "\n"
                  + " Found:\n" + _existingMealEntries);
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
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "db4c0c25ac048f21a726ce27548de1d3", "3822ad734ac3cf04e3eb8c0dd8ad4358");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "exercises","workout_sessions","set_entries","foods","meal_entries","macro_goal");
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
      _db.execSQL("DELETE FROM `meal_entries`");
      _db.execSQL("DELETE FROM `macro_goal`");
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
    _typeConvertersMap.put(MealEntryDao.class, MealEntryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MacroGoalDao.class, MacroGoalDao_Impl.getRequiredConverters());
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
  public MealEntryDao mealEntryDao() {
    if (_mealEntryDao != null) {
      return _mealEntryDao;
    } else {
      synchronized(this) {
        if(_mealEntryDao == null) {
          _mealEntryDao = new MealEntryDao_Impl(this);
        }
        return _mealEntryDao;
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
}

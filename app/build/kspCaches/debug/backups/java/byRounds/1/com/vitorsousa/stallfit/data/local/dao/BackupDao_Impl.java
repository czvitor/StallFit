package com.vitorsousa.stallfit.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.vitorsousa.stallfit.data.local.Converters;
import com.vitorsousa.stallfit.data.local.entity.ActivityLevel;
import com.vitorsousa.stallfit.data.local.entity.BiologicalSex;
import com.vitorsousa.stallfit.data.local.entity.BodyMeasurementEntity;
import com.vitorsousa.stallfit.data.local.entity.Equipment;
import com.vitorsousa.stallfit.data.local.entity.ExerciseEntity;
import com.vitorsousa.stallfit.data.local.entity.FoodEntity;
import com.vitorsousa.stallfit.data.local.entity.Intensity;
import com.vitorsousa.stallfit.data.local.entity.MacroGoalEntity;
import com.vitorsousa.stallfit.data.local.entity.MealEntity;
import com.vitorsousa.stallfit.data.local.entity.MealFoodItemEntity;
import com.vitorsousa.stallfit.data.local.entity.MealType;
import com.vitorsousa.stallfit.data.local.entity.NutritionGoal;
import com.vitorsousa.stallfit.data.local.entity.SetEntryEntity;
import com.vitorsousa.stallfit.data.local.entity.TemplateExerciseEntity;
import com.vitorsousa.stallfit.data.local.entity.UserProfileEntity;
import com.vitorsousa.stallfit.data.local.entity.WorkoutSessionEntity;
import com.vitorsousa.stallfit.data.local.entity.WorkoutTemplateEntity;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class BackupDao_Impl implements BackupDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ExerciseEntity> __insertionAdapterOfExerciseEntity;

  private final Converters __converters = new Converters();

  private final EntityInsertionAdapter<FoodEntity> __insertionAdapterOfFoodEntity;

  private final EntityInsertionAdapter<WorkoutTemplateEntity> __insertionAdapterOfWorkoutTemplateEntity;

  private final EntityInsertionAdapter<MealEntity> __insertionAdapterOfMealEntity;

  private final EntityInsertionAdapter<WorkoutSessionEntity> __insertionAdapterOfWorkoutSessionEntity;

  private final EntityInsertionAdapter<TemplateExerciseEntity> __insertionAdapterOfTemplateExerciseEntity;

  private final EntityInsertionAdapter<SetEntryEntity> __insertionAdapterOfSetEntryEntity;

  private final EntityInsertionAdapter<MealFoodItemEntity> __insertionAdapterOfMealFoodItemEntity;

  private final EntityInsertionAdapter<MacroGoalEntity> __insertionAdapterOfMacroGoalEntity;

  private final EntityInsertionAdapter<UserProfileEntity> __insertionAdapterOfUserProfileEntity;

  private final EntityInsertionAdapter<BodyMeasurementEntity> __insertionAdapterOfBodyMeasurementEntity;

  private final SharedSQLiteStatement __preparedStmtOfClearSetEntries;

  private final SharedSQLiteStatement __preparedStmtOfClearTemplateExercises;

  private final SharedSQLiteStatement __preparedStmtOfClearMealFoodItems;

  private final SharedSQLiteStatement __preparedStmtOfClearWorkoutSessions;

  private final SharedSQLiteStatement __preparedStmtOfClearMeals;

  private final SharedSQLiteStatement __preparedStmtOfClearWorkoutTemplates;

  private final SharedSQLiteStatement __preparedStmtOfClearExercises;

  private final SharedSQLiteStatement __preparedStmtOfClearFoods;

  private final SharedSQLiteStatement __preparedStmtOfClearMacroGoals;

  private final SharedSQLiteStatement __preparedStmtOfClearUserProfiles;

  private final SharedSQLiteStatement __preparedStmtOfClearBodyMeasurements;

  public BackupDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfExerciseEntity = new EntityInsertionAdapter<ExerciseEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `exercises` (`id`,`name`,`muscleGroup`,`equipment`,`isCustom`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExerciseEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getMuscleGroup());
        final String _tmp = __converters.fromEquipment(entity.getEquipment());
        statement.bindString(4, _tmp);
        final int _tmp_1 = entity.isCustom() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
      }
    };
    this.__insertionAdapterOfFoodEntity = new EntityInsertionAdapter<FoodEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `foods` (`id`,`name`,`caloriesPer100g`,`proteinPer100g`,`carbsPer100g`,`fatPer100g`,`isCustom`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FoodEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindDouble(3, entity.getCaloriesPer100g());
        statement.bindDouble(4, entity.getProteinPer100g());
        statement.bindDouble(5, entity.getCarbsPer100g());
        statement.bindDouble(6, entity.getFatPer100g());
        final int _tmp = entity.isCustom() ? 1 : 0;
        statement.bindLong(7, _tmp);
      }
    };
    this.__insertionAdapterOfWorkoutTemplateEntity = new EntityInsertionAdapter<WorkoutTemplateEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `workout_templates` (`id`,`title`,`createdAt`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WorkoutTemplateEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindLong(3, entity.getCreatedAt());
      }
    };
    this.__insertionAdapterOfMealEntity = new EntityInsertionAdapter<MealEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `meals` (`id`,`name`,`mealType`,`createdAt`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MealEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        final String _tmp = __converters.fromMealType(entity.getMealType());
        statement.bindString(3, _tmp);
        statement.bindLong(4, entity.getCreatedAt());
      }
    };
    this.__insertionAdapterOfWorkoutSessionEntity = new EntityInsertionAdapter<WorkoutSessionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `workout_sessions` (`id`,`name`,`startedAt`,`finishedAt`,`templateId`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WorkoutSessionEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindLong(3, entity.getStartedAt());
        if (entity.getFinishedAt() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getFinishedAt());
        }
        if (entity.getTemplateId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getTemplateId());
        }
      }
    };
    this.__insertionAdapterOfTemplateExerciseEntity = new EntityInsertionAdapter<TemplateExerciseEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `template_exercises` (`id`,`templateId`,`exerciseId`,`orderIndex`,`sets`,`repRangeMin`,`repRangeMax`,`restSeconds`,`intensity`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TemplateExerciseEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getTemplateId());
        statement.bindLong(3, entity.getExerciseId());
        statement.bindLong(4, entity.getOrderIndex());
        statement.bindLong(5, entity.getSets());
        statement.bindLong(6, entity.getRepRangeMin());
        statement.bindLong(7, entity.getRepRangeMax());
        statement.bindLong(8, entity.getRestSeconds());
        final String _tmp = __converters.fromIntensity(entity.getIntensity());
        statement.bindString(9, _tmp);
      }
    };
    this.__insertionAdapterOfSetEntryEntity = new EntityInsertionAdapter<SetEntryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `set_entries` (`id`,`sessionId`,`exerciseId`,`setNumber`,`reps`,`weightKg`,`loggedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SetEntryEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSessionId());
        statement.bindLong(3, entity.getExerciseId());
        statement.bindLong(4, entity.getSetNumber());
        statement.bindLong(5, entity.getReps());
        statement.bindDouble(6, entity.getWeightKg());
        statement.bindLong(7, entity.getLoggedAt());
      }
    };
    this.__insertionAdapterOfMealFoodItemEntity = new EntityInsertionAdapter<MealFoodItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `meal_food_items` (`id`,`mealId`,`foodId`,`grams`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MealFoodItemEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getMealId());
        statement.bindLong(3, entity.getFoodId());
        statement.bindDouble(4, entity.getGrams());
      }
    };
    this.__insertionAdapterOfMacroGoalEntity = new EntityInsertionAdapter<MacroGoalEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `macro_goal` (`id`,`calorieGoal`,`proteinGoal`,`carbGoal`,`fatGoal`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MacroGoalEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getCalorieGoal());
        statement.bindLong(3, entity.getProteinGoal());
        statement.bindLong(4, entity.getCarbGoal());
        statement.bindLong(5, entity.getFatGoal());
      }
    };
    this.__insertionAdapterOfUserProfileEntity = new EntityInsertionAdapter<UserProfileEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `user_profile` (`id`,`name`,`ageYears`,`sex`,`activityLevel`,`goal`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserProfileEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindLong(3, entity.getAgeYears());
        final String _tmp = __converters.fromBiologicalSex(entity.getSex());
        statement.bindString(4, _tmp);
        final String _tmp_1 = __converters.fromActivityLevel(entity.getActivityLevel());
        statement.bindString(5, _tmp_1);
        final String _tmp_2 = __converters.fromNutritionGoal(entity.getGoal());
        statement.bindString(6, _tmp_2);
      }
    };
    this.__insertionAdapterOfBodyMeasurementEntity = new EntityInsertionAdapter<BodyMeasurementEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `body_measurements` (`id`,`createdAt`,`weightKg`,`heightCm`,`chestCm`,`waistCm`,`abdomenCm`,`hipCm`,`armLeftRelaxedCm`,`armLeftFlexedCm`,`armRightRelaxedCm`,`armRightFlexedCm`,`forearmLeftCm`,`forearmRightCm`,`thighLeftCm`,`thighRightCm`,`calfLeftCm`,`calfRightCm`,`bodyFatPercent`,`leanMassKg`,`fatMassKg`,`bodyWaterPercent`,`tricepsFoldMm`,`subscapularFoldMm`,`suprailiacFoldMm`,`abdominalFoldMm`,`thighFoldMm`,`chestFoldMm`,`midaxillaryFoldMm`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BodyMeasurementEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getCreatedAt());
        statement.bindDouble(3, entity.getWeightKg());
        statement.bindDouble(4, entity.getHeightCm());
        if (entity.getChestCm() == null) {
          statement.bindNull(5);
        } else {
          statement.bindDouble(5, entity.getChestCm());
        }
        if (entity.getWaistCm() == null) {
          statement.bindNull(6);
        } else {
          statement.bindDouble(6, entity.getWaistCm());
        }
        if (entity.getAbdomenCm() == null) {
          statement.bindNull(7);
        } else {
          statement.bindDouble(7, entity.getAbdomenCm());
        }
        if (entity.getHipCm() == null) {
          statement.bindNull(8);
        } else {
          statement.bindDouble(8, entity.getHipCm());
        }
        if (entity.getArmLeftRelaxedCm() == null) {
          statement.bindNull(9);
        } else {
          statement.bindDouble(9, entity.getArmLeftRelaxedCm());
        }
        if (entity.getArmLeftFlexedCm() == null) {
          statement.bindNull(10);
        } else {
          statement.bindDouble(10, entity.getArmLeftFlexedCm());
        }
        if (entity.getArmRightRelaxedCm() == null) {
          statement.bindNull(11);
        } else {
          statement.bindDouble(11, entity.getArmRightRelaxedCm());
        }
        if (entity.getArmRightFlexedCm() == null) {
          statement.bindNull(12);
        } else {
          statement.bindDouble(12, entity.getArmRightFlexedCm());
        }
        if (entity.getForearmLeftCm() == null) {
          statement.bindNull(13);
        } else {
          statement.bindDouble(13, entity.getForearmLeftCm());
        }
        if (entity.getForearmRightCm() == null) {
          statement.bindNull(14);
        } else {
          statement.bindDouble(14, entity.getForearmRightCm());
        }
        if (entity.getThighLeftCm() == null) {
          statement.bindNull(15);
        } else {
          statement.bindDouble(15, entity.getThighLeftCm());
        }
        if (entity.getThighRightCm() == null) {
          statement.bindNull(16);
        } else {
          statement.bindDouble(16, entity.getThighRightCm());
        }
        if (entity.getCalfLeftCm() == null) {
          statement.bindNull(17);
        } else {
          statement.bindDouble(17, entity.getCalfLeftCm());
        }
        if (entity.getCalfRightCm() == null) {
          statement.bindNull(18);
        } else {
          statement.bindDouble(18, entity.getCalfRightCm());
        }
        if (entity.getBodyFatPercent() == null) {
          statement.bindNull(19);
        } else {
          statement.bindDouble(19, entity.getBodyFatPercent());
        }
        if (entity.getLeanMassKg() == null) {
          statement.bindNull(20);
        } else {
          statement.bindDouble(20, entity.getLeanMassKg());
        }
        if (entity.getFatMassKg() == null) {
          statement.bindNull(21);
        } else {
          statement.bindDouble(21, entity.getFatMassKg());
        }
        if (entity.getBodyWaterPercent() == null) {
          statement.bindNull(22);
        } else {
          statement.bindDouble(22, entity.getBodyWaterPercent());
        }
        if (entity.getTricepsFoldMm() == null) {
          statement.bindNull(23);
        } else {
          statement.bindDouble(23, entity.getTricepsFoldMm());
        }
        if (entity.getSubscapularFoldMm() == null) {
          statement.bindNull(24);
        } else {
          statement.bindDouble(24, entity.getSubscapularFoldMm());
        }
        if (entity.getSuprailiacFoldMm() == null) {
          statement.bindNull(25);
        } else {
          statement.bindDouble(25, entity.getSuprailiacFoldMm());
        }
        if (entity.getAbdominalFoldMm() == null) {
          statement.bindNull(26);
        } else {
          statement.bindDouble(26, entity.getAbdominalFoldMm());
        }
        if (entity.getThighFoldMm() == null) {
          statement.bindNull(27);
        } else {
          statement.bindDouble(27, entity.getThighFoldMm());
        }
        if (entity.getChestFoldMm() == null) {
          statement.bindNull(28);
        } else {
          statement.bindDouble(28, entity.getChestFoldMm());
        }
        if (entity.getMidaxillaryFoldMm() == null) {
          statement.bindNull(29);
        } else {
          statement.bindDouble(29, entity.getMidaxillaryFoldMm());
        }
      }
    };
    this.__preparedStmtOfClearSetEntries = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM set_entries";
        return _query;
      }
    };
    this.__preparedStmtOfClearTemplateExercises = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM template_exercises";
        return _query;
      }
    };
    this.__preparedStmtOfClearMealFoodItems = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM meal_food_items";
        return _query;
      }
    };
    this.__preparedStmtOfClearWorkoutSessions = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM workout_sessions";
        return _query;
      }
    };
    this.__preparedStmtOfClearMeals = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM meals";
        return _query;
      }
    };
    this.__preparedStmtOfClearWorkoutTemplates = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM workout_templates";
        return _query;
      }
    };
    this.__preparedStmtOfClearExercises = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM exercises";
        return _query;
      }
    };
    this.__preparedStmtOfClearFoods = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM foods";
        return _query;
      }
    };
    this.__preparedStmtOfClearMacroGoals = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM macro_goal";
        return _query;
      }
    };
    this.__preparedStmtOfClearUserProfiles = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM user_profile";
        return _query;
      }
    };
    this.__preparedStmtOfClearBodyMeasurements = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM body_measurements";
        return _query;
      }
    };
  }

  @Override
  public Object insertExercises(final List<ExerciseEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfExerciseEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertFoods(final List<FoodEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfFoodEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertWorkoutTemplates(final List<WorkoutTemplateEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfWorkoutTemplateEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertMeals(final List<MealEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMealEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertWorkoutSessions(final List<WorkoutSessionEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfWorkoutSessionEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertTemplateExercises(final List<TemplateExerciseEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTemplateExerciseEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertSetEntries(final List<SetEntryEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSetEntryEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertMealFoodItems(final List<MealFoodItemEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMealFoodItemEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertMacroGoals(final List<MacroGoalEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMacroGoalEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertUserProfiles(final List<UserProfileEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUserProfileEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertBodyMeasurements(final List<BodyMeasurementEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBodyMeasurementEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object restoreAll(final List<ExerciseEntity> exercises, final List<FoodEntity> foods,
      final List<WorkoutTemplateEntity> workoutTemplates, final List<MealEntity> meals,
      final List<WorkoutSessionEntity> workoutSessions,
      final List<TemplateExerciseEntity> templateExercises, final List<SetEntryEntity> setEntries,
      final List<MealFoodItemEntity> mealFoodItems, final List<MacroGoalEntity> macroGoals,
      final List<UserProfileEntity> userProfiles,
      final List<BodyMeasurementEntity> bodyMeasurements,
      final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> BackupDao.DefaultImpls.restoreAll(BackupDao_Impl.this, exercises, foods, workoutTemplates, meals, workoutSessions, templateExercises, setEntries, mealFoodItems, macroGoals, userProfiles, bodyMeasurements, __cont), $completion);
  }

  @Override
  public Object clearSetEntries(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearSetEntries.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearSetEntries.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearTemplateExercises(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearTemplateExercises.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearTemplateExercises.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearMealFoodItems(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearMealFoodItems.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearMealFoodItems.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearWorkoutSessions(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearWorkoutSessions.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearWorkoutSessions.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearMeals(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearMeals.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearMeals.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearWorkoutTemplates(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearWorkoutTemplates.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearWorkoutTemplates.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearExercises(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearExercises.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearExercises.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearFoods(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearFoods.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearFoods.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearMacroGoals(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearMacroGoals.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearMacroGoals.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearUserProfiles(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearUserProfiles.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearUserProfiles.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearBodyMeasurements(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearBodyMeasurements.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearBodyMeasurements.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllExercises(final Continuation<? super List<ExerciseEntity>> $completion) {
    final String _sql = "SELECT * FROM exercises";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ExerciseEntity>>() {
      @Override
      @NonNull
      public List<ExerciseEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfMuscleGroup = CursorUtil.getColumnIndexOrThrow(_cursor, "muscleGroup");
          final int _cursorIndexOfEquipment = CursorUtil.getColumnIndexOrThrow(_cursor, "equipment");
          final int _cursorIndexOfIsCustom = CursorUtil.getColumnIndexOrThrow(_cursor, "isCustom");
          final List<ExerciseEntity> _result = new ArrayList<ExerciseEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExerciseEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpMuscleGroup;
            _tmpMuscleGroup = _cursor.getString(_cursorIndexOfMuscleGroup);
            final Equipment _tmpEquipment;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfEquipment);
            _tmpEquipment = __converters.toEquipment(_tmp);
            final boolean _tmpIsCustom;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsCustom);
            _tmpIsCustom = _tmp_1 != 0;
            _item = new ExerciseEntity(_tmpId,_tmpName,_tmpMuscleGroup,_tmpEquipment,_tmpIsCustom);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllFoods(final Continuation<? super List<FoodEntity>> $completion) {
    final String _sql = "SELECT * FROM foods";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<FoodEntity>>() {
      @Override
      @NonNull
      public List<FoodEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCaloriesPer100g = CursorUtil.getColumnIndexOrThrow(_cursor, "caloriesPer100g");
          final int _cursorIndexOfProteinPer100g = CursorUtil.getColumnIndexOrThrow(_cursor, "proteinPer100g");
          final int _cursorIndexOfCarbsPer100g = CursorUtil.getColumnIndexOrThrow(_cursor, "carbsPer100g");
          final int _cursorIndexOfFatPer100g = CursorUtil.getColumnIndexOrThrow(_cursor, "fatPer100g");
          final int _cursorIndexOfIsCustom = CursorUtil.getColumnIndexOrThrow(_cursor, "isCustom");
          final List<FoodEntity> _result = new ArrayList<FoodEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FoodEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final double _tmpCaloriesPer100g;
            _tmpCaloriesPer100g = _cursor.getDouble(_cursorIndexOfCaloriesPer100g);
            final double _tmpProteinPer100g;
            _tmpProteinPer100g = _cursor.getDouble(_cursorIndexOfProteinPer100g);
            final double _tmpCarbsPer100g;
            _tmpCarbsPer100g = _cursor.getDouble(_cursorIndexOfCarbsPer100g);
            final double _tmpFatPer100g;
            _tmpFatPer100g = _cursor.getDouble(_cursorIndexOfFatPer100g);
            final boolean _tmpIsCustom;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCustom);
            _tmpIsCustom = _tmp != 0;
            _item = new FoodEntity(_tmpId,_tmpName,_tmpCaloriesPer100g,_tmpProteinPer100g,_tmpCarbsPer100g,_tmpFatPer100g,_tmpIsCustom);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllWorkoutTemplates(
      final Continuation<? super List<WorkoutTemplateEntity>> $completion) {
    final String _sql = "SELECT * FROM workout_templates";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<WorkoutTemplateEntity>>() {
      @Override
      @NonNull
      public List<WorkoutTemplateEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<WorkoutTemplateEntity> _result = new ArrayList<WorkoutTemplateEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WorkoutTemplateEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new WorkoutTemplateEntity(_tmpId,_tmpTitle,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllMeals(final Continuation<? super List<MealEntity>> $completion) {
    final String _sql = "SELECT * FROM meals";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MealEntity>>() {
      @Override
      @NonNull
      public List<MealEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfMealType = CursorUtil.getColumnIndexOrThrow(_cursor, "mealType");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<MealEntity> _result = new ArrayList<MealEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MealEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final MealType _tmpMealType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfMealType);
            _tmpMealType = __converters.toMealType(_tmp);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new MealEntity(_tmpId,_tmpName,_tmpMealType,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllWorkoutSessions(
      final Continuation<? super List<WorkoutSessionEntity>> $completion) {
    final String _sql = "SELECT * FROM workout_sessions";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<WorkoutSessionEntity>>() {
      @Override
      @NonNull
      public List<WorkoutSessionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfStartedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "startedAt");
          final int _cursorIndexOfFinishedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "finishedAt");
          final int _cursorIndexOfTemplateId = CursorUtil.getColumnIndexOrThrow(_cursor, "templateId");
          final List<WorkoutSessionEntity> _result = new ArrayList<WorkoutSessionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WorkoutSessionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final long _tmpStartedAt;
            _tmpStartedAt = _cursor.getLong(_cursorIndexOfStartedAt);
            final Long _tmpFinishedAt;
            if (_cursor.isNull(_cursorIndexOfFinishedAt)) {
              _tmpFinishedAt = null;
            } else {
              _tmpFinishedAt = _cursor.getLong(_cursorIndexOfFinishedAt);
            }
            final Long _tmpTemplateId;
            if (_cursor.isNull(_cursorIndexOfTemplateId)) {
              _tmpTemplateId = null;
            } else {
              _tmpTemplateId = _cursor.getLong(_cursorIndexOfTemplateId);
            }
            _item = new WorkoutSessionEntity(_tmpId,_tmpName,_tmpStartedAt,_tmpFinishedAt,_tmpTemplateId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllTemplateExercises(
      final Continuation<? super List<TemplateExerciseEntity>> $completion) {
    final String _sql = "SELECT * FROM template_exercises";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<TemplateExerciseEntity>>() {
      @Override
      @NonNull
      public List<TemplateExerciseEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTemplateId = CursorUtil.getColumnIndexOrThrow(_cursor, "templateId");
          final int _cursorIndexOfExerciseId = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseId");
          final int _cursorIndexOfOrderIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "orderIndex");
          final int _cursorIndexOfSets = CursorUtil.getColumnIndexOrThrow(_cursor, "sets");
          final int _cursorIndexOfRepRangeMin = CursorUtil.getColumnIndexOrThrow(_cursor, "repRangeMin");
          final int _cursorIndexOfRepRangeMax = CursorUtil.getColumnIndexOrThrow(_cursor, "repRangeMax");
          final int _cursorIndexOfRestSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "restSeconds");
          final int _cursorIndexOfIntensity = CursorUtil.getColumnIndexOrThrow(_cursor, "intensity");
          final List<TemplateExerciseEntity> _result = new ArrayList<TemplateExerciseEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TemplateExerciseEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTemplateId;
            _tmpTemplateId = _cursor.getLong(_cursorIndexOfTemplateId);
            final long _tmpExerciseId;
            _tmpExerciseId = _cursor.getLong(_cursorIndexOfExerciseId);
            final int _tmpOrderIndex;
            _tmpOrderIndex = _cursor.getInt(_cursorIndexOfOrderIndex);
            final int _tmpSets;
            _tmpSets = _cursor.getInt(_cursorIndexOfSets);
            final int _tmpRepRangeMin;
            _tmpRepRangeMin = _cursor.getInt(_cursorIndexOfRepRangeMin);
            final int _tmpRepRangeMax;
            _tmpRepRangeMax = _cursor.getInt(_cursorIndexOfRepRangeMax);
            final int _tmpRestSeconds;
            _tmpRestSeconds = _cursor.getInt(_cursorIndexOfRestSeconds);
            final Intensity _tmpIntensity;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfIntensity);
            _tmpIntensity = __converters.toIntensity(_tmp);
            _item = new TemplateExerciseEntity(_tmpId,_tmpTemplateId,_tmpExerciseId,_tmpOrderIndex,_tmpSets,_tmpRepRangeMin,_tmpRepRangeMax,_tmpRestSeconds,_tmpIntensity);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllSetEntries(final Continuation<? super List<SetEntryEntity>> $completion) {
    final String _sql = "SELECT * FROM set_entries";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<SetEntryEntity>>() {
      @Override
      @NonNull
      public List<SetEntryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSessionId = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionId");
          final int _cursorIndexOfExerciseId = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseId");
          final int _cursorIndexOfSetNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "setNumber");
          final int _cursorIndexOfReps = CursorUtil.getColumnIndexOrThrow(_cursor, "reps");
          final int _cursorIndexOfWeightKg = CursorUtil.getColumnIndexOrThrow(_cursor, "weightKg");
          final int _cursorIndexOfLoggedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "loggedAt");
          final List<SetEntryEntity> _result = new ArrayList<SetEntryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SetEntryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpSessionId;
            _tmpSessionId = _cursor.getLong(_cursorIndexOfSessionId);
            final long _tmpExerciseId;
            _tmpExerciseId = _cursor.getLong(_cursorIndexOfExerciseId);
            final int _tmpSetNumber;
            _tmpSetNumber = _cursor.getInt(_cursorIndexOfSetNumber);
            final int _tmpReps;
            _tmpReps = _cursor.getInt(_cursorIndexOfReps);
            final double _tmpWeightKg;
            _tmpWeightKg = _cursor.getDouble(_cursorIndexOfWeightKg);
            final long _tmpLoggedAt;
            _tmpLoggedAt = _cursor.getLong(_cursorIndexOfLoggedAt);
            _item = new SetEntryEntity(_tmpId,_tmpSessionId,_tmpExerciseId,_tmpSetNumber,_tmpReps,_tmpWeightKg,_tmpLoggedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllMealFoodItems(
      final Continuation<? super List<MealFoodItemEntity>> $completion) {
    final String _sql = "SELECT * FROM meal_food_items";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MealFoodItemEntity>>() {
      @Override
      @NonNull
      public List<MealFoodItemEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMealId = CursorUtil.getColumnIndexOrThrow(_cursor, "mealId");
          final int _cursorIndexOfFoodId = CursorUtil.getColumnIndexOrThrow(_cursor, "foodId");
          final int _cursorIndexOfGrams = CursorUtil.getColumnIndexOrThrow(_cursor, "grams");
          final List<MealFoodItemEntity> _result = new ArrayList<MealFoodItemEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MealFoodItemEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpMealId;
            _tmpMealId = _cursor.getLong(_cursorIndexOfMealId);
            final long _tmpFoodId;
            _tmpFoodId = _cursor.getLong(_cursorIndexOfFoodId);
            final double _tmpGrams;
            _tmpGrams = _cursor.getDouble(_cursorIndexOfGrams);
            _item = new MealFoodItemEntity(_tmpId,_tmpMealId,_tmpFoodId,_tmpGrams);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllMacroGoals(final Continuation<? super List<MacroGoalEntity>> $completion) {
    final String _sql = "SELECT * FROM macro_goal";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MacroGoalEntity>>() {
      @Override
      @NonNull
      public List<MacroGoalEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCalorieGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "calorieGoal");
          final int _cursorIndexOfProteinGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "proteinGoal");
          final int _cursorIndexOfCarbGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "carbGoal");
          final int _cursorIndexOfFatGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "fatGoal");
          final List<MacroGoalEntity> _result = new ArrayList<MacroGoalEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MacroGoalEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpCalorieGoal;
            _tmpCalorieGoal = _cursor.getInt(_cursorIndexOfCalorieGoal);
            final int _tmpProteinGoal;
            _tmpProteinGoal = _cursor.getInt(_cursorIndexOfProteinGoal);
            final int _tmpCarbGoal;
            _tmpCarbGoal = _cursor.getInt(_cursorIndexOfCarbGoal);
            final int _tmpFatGoal;
            _tmpFatGoal = _cursor.getInt(_cursorIndexOfFatGoal);
            _item = new MacroGoalEntity(_tmpId,_tmpCalorieGoal,_tmpProteinGoal,_tmpCarbGoal,_tmpFatGoal);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllUserProfiles(
      final Continuation<? super List<UserProfileEntity>> $completion) {
    final String _sql = "SELECT * FROM user_profile";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<UserProfileEntity>>() {
      @Override
      @NonNull
      public List<UserProfileEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAgeYears = CursorUtil.getColumnIndexOrThrow(_cursor, "ageYears");
          final int _cursorIndexOfSex = CursorUtil.getColumnIndexOrThrow(_cursor, "sex");
          final int _cursorIndexOfActivityLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "activityLevel");
          final int _cursorIndexOfGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "goal");
          final List<UserProfileEntity> _result = new ArrayList<UserProfileEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final UserProfileEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpAgeYears;
            _tmpAgeYears = _cursor.getInt(_cursorIndexOfAgeYears);
            final BiologicalSex _tmpSex;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSex);
            _tmpSex = __converters.toBiologicalSex(_tmp);
            final ActivityLevel _tmpActivityLevel;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfActivityLevel);
            _tmpActivityLevel = __converters.toActivityLevel(_tmp_1);
            final NutritionGoal _tmpGoal;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfGoal);
            _tmpGoal = __converters.toNutritionGoal(_tmp_2);
            _item = new UserProfileEntity(_tmpId,_tmpName,_tmpAgeYears,_tmpSex,_tmpActivityLevel,_tmpGoal);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllBodyMeasurements(
      final Continuation<? super List<BodyMeasurementEntity>> $completion) {
    final String _sql = "SELECT * FROM body_measurements";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<BodyMeasurementEntity>>() {
      @Override
      @NonNull
      public List<BodyMeasurementEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfWeightKg = CursorUtil.getColumnIndexOrThrow(_cursor, "weightKg");
          final int _cursorIndexOfHeightCm = CursorUtil.getColumnIndexOrThrow(_cursor, "heightCm");
          final int _cursorIndexOfChestCm = CursorUtil.getColumnIndexOrThrow(_cursor, "chestCm");
          final int _cursorIndexOfWaistCm = CursorUtil.getColumnIndexOrThrow(_cursor, "waistCm");
          final int _cursorIndexOfAbdomenCm = CursorUtil.getColumnIndexOrThrow(_cursor, "abdomenCm");
          final int _cursorIndexOfHipCm = CursorUtil.getColumnIndexOrThrow(_cursor, "hipCm");
          final int _cursorIndexOfArmLeftRelaxedCm = CursorUtil.getColumnIndexOrThrow(_cursor, "armLeftRelaxedCm");
          final int _cursorIndexOfArmLeftFlexedCm = CursorUtil.getColumnIndexOrThrow(_cursor, "armLeftFlexedCm");
          final int _cursorIndexOfArmRightRelaxedCm = CursorUtil.getColumnIndexOrThrow(_cursor, "armRightRelaxedCm");
          final int _cursorIndexOfArmRightFlexedCm = CursorUtil.getColumnIndexOrThrow(_cursor, "armRightFlexedCm");
          final int _cursorIndexOfForearmLeftCm = CursorUtil.getColumnIndexOrThrow(_cursor, "forearmLeftCm");
          final int _cursorIndexOfForearmRightCm = CursorUtil.getColumnIndexOrThrow(_cursor, "forearmRightCm");
          final int _cursorIndexOfThighLeftCm = CursorUtil.getColumnIndexOrThrow(_cursor, "thighLeftCm");
          final int _cursorIndexOfThighRightCm = CursorUtil.getColumnIndexOrThrow(_cursor, "thighRightCm");
          final int _cursorIndexOfCalfLeftCm = CursorUtil.getColumnIndexOrThrow(_cursor, "calfLeftCm");
          final int _cursorIndexOfCalfRightCm = CursorUtil.getColumnIndexOrThrow(_cursor, "calfRightCm");
          final int _cursorIndexOfBodyFatPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "bodyFatPercent");
          final int _cursorIndexOfLeanMassKg = CursorUtil.getColumnIndexOrThrow(_cursor, "leanMassKg");
          final int _cursorIndexOfFatMassKg = CursorUtil.getColumnIndexOrThrow(_cursor, "fatMassKg");
          final int _cursorIndexOfBodyWaterPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "bodyWaterPercent");
          final int _cursorIndexOfTricepsFoldMm = CursorUtil.getColumnIndexOrThrow(_cursor, "tricepsFoldMm");
          final int _cursorIndexOfSubscapularFoldMm = CursorUtil.getColumnIndexOrThrow(_cursor, "subscapularFoldMm");
          final int _cursorIndexOfSuprailiacFoldMm = CursorUtil.getColumnIndexOrThrow(_cursor, "suprailiacFoldMm");
          final int _cursorIndexOfAbdominalFoldMm = CursorUtil.getColumnIndexOrThrow(_cursor, "abdominalFoldMm");
          final int _cursorIndexOfThighFoldMm = CursorUtil.getColumnIndexOrThrow(_cursor, "thighFoldMm");
          final int _cursorIndexOfChestFoldMm = CursorUtil.getColumnIndexOrThrow(_cursor, "chestFoldMm");
          final int _cursorIndexOfMidaxillaryFoldMm = CursorUtil.getColumnIndexOrThrow(_cursor, "midaxillaryFoldMm");
          final List<BodyMeasurementEntity> _result = new ArrayList<BodyMeasurementEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BodyMeasurementEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final double _tmpWeightKg;
            _tmpWeightKg = _cursor.getDouble(_cursorIndexOfWeightKg);
            final double _tmpHeightCm;
            _tmpHeightCm = _cursor.getDouble(_cursorIndexOfHeightCm);
            final Double _tmpChestCm;
            if (_cursor.isNull(_cursorIndexOfChestCm)) {
              _tmpChestCm = null;
            } else {
              _tmpChestCm = _cursor.getDouble(_cursorIndexOfChestCm);
            }
            final Double _tmpWaistCm;
            if (_cursor.isNull(_cursorIndexOfWaistCm)) {
              _tmpWaistCm = null;
            } else {
              _tmpWaistCm = _cursor.getDouble(_cursorIndexOfWaistCm);
            }
            final Double _tmpAbdomenCm;
            if (_cursor.isNull(_cursorIndexOfAbdomenCm)) {
              _tmpAbdomenCm = null;
            } else {
              _tmpAbdomenCm = _cursor.getDouble(_cursorIndexOfAbdomenCm);
            }
            final Double _tmpHipCm;
            if (_cursor.isNull(_cursorIndexOfHipCm)) {
              _tmpHipCm = null;
            } else {
              _tmpHipCm = _cursor.getDouble(_cursorIndexOfHipCm);
            }
            final Double _tmpArmLeftRelaxedCm;
            if (_cursor.isNull(_cursorIndexOfArmLeftRelaxedCm)) {
              _tmpArmLeftRelaxedCm = null;
            } else {
              _tmpArmLeftRelaxedCm = _cursor.getDouble(_cursorIndexOfArmLeftRelaxedCm);
            }
            final Double _tmpArmLeftFlexedCm;
            if (_cursor.isNull(_cursorIndexOfArmLeftFlexedCm)) {
              _tmpArmLeftFlexedCm = null;
            } else {
              _tmpArmLeftFlexedCm = _cursor.getDouble(_cursorIndexOfArmLeftFlexedCm);
            }
            final Double _tmpArmRightRelaxedCm;
            if (_cursor.isNull(_cursorIndexOfArmRightRelaxedCm)) {
              _tmpArmRightRelaxedCm = null;
            } else {
              _tmpArmRightRelaxedCm = _cursor.getDouble(_cursorIndexOfArmRightRelaxedCm);
            }
            final Double _tmpArmRightFlexedCm;
            if (_cursor.isNull(_cursorIndexOfArmRightFlexedCm)) {
              _tmpArmRightFlexedCm = null;
            } else {
              _tmpArmRightFlexedCm = _cursor.getDouble(_cursorIndexOfArmRightFlexedCm);
            }
            final Double _tmpForearmLeftCm;
            if (_cursor.isNull(_cursorIndexOfForearmLeftCm)) {
              _tmpForearmLeftCm = null;
            } else {
              _tmpForearmLeftCm = _cursor.getDouble(_cursorIndexOfForearmLeftCm);
            }
            final Double _tmpForearmRightCm;
            if (_cursor.isNull(_cursorIndexOfForearmRightCm)) {
              _tmpForearmRightCm = null;
            } else {
              _tmpForearmRightCm = _cursor.getDouble(_cursorIndexOfForearmRightCm);
            }
            final Double _tmpThighLeftCm;
            if (_cursor.isNull(_cursorIndexOfThighLeftCm)) {
              _tmpThighLeftCm = null;
            } else {
              _tmpThighLeftCm = _cursor.getDouble(_cursorIndexOfThighLeftCm);
            }
            final Double _tmpThighRightCm;
            if (_cursor.isNull(_cursorIndexOfThighRightCm)) {
              _tmpThighRightCm = null;
            } else {
              _tmpThighRightCm = _cursor.getDouble(_cursorIndexOfThighRightCm);
            }
            final Double _tmpCalfLeftCm;
            if (_cursor.isNull(_cursorIndexOfCalfLeftCm)) {
              _tmpCalfLeftCm = null;
            } else {
              _tmpCalfLeftCm = _cursor.getDouble(_cursorIndexOfCalfLeftCm);
            }
            final Double _tmpCalfRightCm;
            if (_cursor.isNull(_cursorIndexOfCalfRightCm)) {
              _tmpCalfRightCm = null;
            } else {
              _tmpCalfRightCm = _cursor.getDouble(_cursorIndexOfCalfRightCm);
            }
            final Double _tmpBodyFatPercent;
            if (_cursor.isNull(_cursorIndexOfBodyFatPercent)) {
              _tmpBodyFatPercent = null;
            } else {
              _tmpBodyFatPercent = _cursor.getDouble(_cursorIndexOfBodyFatPercent);
            }
            final Double _tmpLeanMassKg;
            if (_cursor.isNull(_cursorIndexOfLeanMassKg)) {
              _tmpLeanMassKg = null;
            } else {
              _tmpLeanMassKg = _cursor.getDouble(_cursorIndexOfLeanMassKg);
            }
            final Double _tmpFatMassKg;
            if (_cursor.isNull(_cursorIndexOfFatMassKg)) {
              _tmpFatMassKg = null;
            } else {
              _tmpFatMassKg = _cursor.getDouble(_cursorIndexOfFatMassKg);
            }
            final Double _tmpBodyWaterPercent;
            if (_cursor.isNull(_cursorIndexOfBodyWaterPercent)) {
              _tmpBodyWaterPercent = null;
            } else {
              _tmpBodyWaterPercent = _cursor.getDouble(_cursorIndexOfBodyWaterPercent);
            }
            final Double _tmpTricepsFoldMm;
            if (_cursor.isNull(_cursorIndexOfTricepsFoldMm)) {
              _tmpTricepsFoldMm = null;
            } else {
              _tmpTricepsFoldMm = _cursor.getDouble(_cursorIndexOfTricepsFoldMm);
            }
            final Double _tmpSubscapularFoldMm;
            if (_cursor.isNull(_cursorIndexOfSubscapularFoldMm)) {
              _tmpSubscapularFoldMm = null;
            } else {
              _tmpSubscapularFoldMm = _cursor.getDouble(_cursorIndexOfSubscapularFoldMm);
            }
            final Double _tmpSuprailiacFoldMm;
            if (_cursor.isNull(_cursorIndexOfSuprailiacFoldMm)) {
              _tmpSuprailiacFoldMm = null;
            } else {
              _tmpSuprailiacFoldMm = _cursor.getDouble(_cursorIndexOfSuprailiacFoldMm);
            }
            final Double _tmpAbdominalFoldMm;
            if (_cursor.isNull(_cursorIndexOfAbdominalFoldMm)) {
              _tmpAbdominalFoldMm = null;
            } else {
              _tmpAbdominalFoldMm = _cursor.getDouble(_cursorIndexOfAbdominalFoldMm);
            }
            final Double _tmpThighFoldMm;
            if (_cursor.isNull(_cursorIndexOfThighFoldMm)) {
              _tmpThighFoldMm = null;
            } else {
              _tmpThighFoldMm = _cursor.getDouble(_cursorIndexOfThighFoldMm);
            }
            final Double _tmpChestFoldMm;
            if (_cursor.isNull(_cursorIndexOfChestFoldMm)) {
              _tmpChestFoldMm = null;
            } else {
              _tmpChestFoldMm = _cursor.getDouble(_cursorIndexOfChestFoldMm);
            }
            final Double _tmpMidaxillaryFoldMm;
            if (_cursor.isNull(_cursorIndexOfMidaxillaryFoldMm)) {
              _tmpMidaxillaryFoldMm = null;
            } else {
              _tmpMidaxillaryFoldMm = _cursor.getDouble(_cursorIndexOfMidaxillaryFoldMm);
            }
            _item = new BodyMeasurementEntity(_tmpId,_tmpCreatedAt,_tmpWeightKg,_tmpHeightCm,_tmpChestCm,_tmpWaistCm,_tmpAbdomenCm,_tmpHipCm,_tmpArmLeftRelaxedCm,_tmpArmLeftFlexedCm,_tmpArmRightRelaxedCm,_tmpArmRightFlexedCm,_tmpForearmLeftCm,_tmpForearmRightCm,_tmpThighLeftCm,_tmpThighRightCm,_tmpCalfLeftCm,_tmpCalfRightCm,_tmpBodyFatPercent,_tmpLeanMassKg,_tmpFatMassKg,_tmpBodyWaterPercent,_tmpTricepsFoldMm,_tmpSubscapularFoldMm,_tmpSuprailiacFoldMm,_tmpAbdominalFoldMm,_tmpThighFoldMm,_tmpChestFoldMm,_tmpMidaxillaryFoldMm);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

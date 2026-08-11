package com.vitorsousa.stallfit.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.vitorsousa.stallfit.data.local.entity.MealEntity;
import com.vitorsousa.stallfit.data.local.entity.MealFoodItemEntity;
import com.vitorsousa.stallfit.data.local.entity.MealType;
import com.vitorsousa.stallfit.data.local.relation.MealFoodItemWithFood;
import com.vitorsousa.stallfit.data.local.relation.MealWithTotals;
import java.lang.Class;
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
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MealDao_Impl implements MealDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MealEntity> __insertionAdapterOfMealEntity;

  private final Converters __converters = new Converters();

  private final EntityInsertionAdapter<MealFoodItemEntity> __insertionAdapterOfMealFoodItemEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteMeal;

  public MealDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMealEntity = new EntityInsertionAdapter<MealEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `meals` (`id`,`name`,`mealType`,`createdAt`) VALUES (nullif(?, 0),?,?,?)";
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
    this.__insertionAdapterOfMealFoodItemEntity = new EntityInsertionAdapter<MealFoodItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `meal_food_items` (`id`,`mealId`,`foodId`,`grams`) VALUES (nullif(?, 0),?,?,?)";
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
    this.__preparedStmtOfDeleteMeal = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM meals WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertMeal(final MealEntity meal, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfMealEntity.insertAndReturnId(meal);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertItems(final List<MealFoodItemEntity> items,
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
  public Object insertMealWithItems(final MealEntity meal, final List<MealFoodItemEntity> items,
      final Continuation<? super Long> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> MealDao.DefaultImpls.insertMealWithItems(MealDao_Impl.this, meal, items, __cont), $completion);
  }

  @Override
  public Object deleteMeal(final long mealId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteMeal.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, mealId);
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
          __preparedStmtOfDeleteMeal.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<MealEntity> getMealFlow(final long mealId) {
    final String _sql = "SELECT * FROM meals WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, mealId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"meals"}, new Callable<MealEntity>() {
      @Override
      @Nullable
      public MealEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfMealType = CursorUtil.getColumnIndexOrThrow(_cursor, "mealType");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final MealEntity _result;
          if (_cursor.moveToFirst()) {
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
            _result = new MealEntity(_tmpId,_tmpName,_tmpMealType,_tmpCreatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<MealWithTotals>> getMealsWithTotals() {
    final String _sql = "\n"
            + "        SELECT meals.id AS mealId, meals.name AS mealName, meals.mealType AS mealType, meals.createdAt AS createdAt,\n"
            + "               COALESCE(SUM(foods.caloriesPer100g * meal_food_items.grams / 100.0), 0.0) AS totalCalories,\n"
            + "               COALESCE(SUM(foods.proteinPer100g * meal_food_items.grams / 100.0), 0.0) AS totalProtein,\n"
            + "               COALESCE(SUM(foods.carbsPer100g * meal_food_items.grams / 100.0), 0.0) AS totalCarbs,\n"
            + "               COALESCE(SUM(foods.fatPer100g * meal_food_items.grams / 100.0), 0.0) AS totalFat\n"
            + "        FROM meals\n"
            + "        LEFT JOIN meal_food_items ON meal_food_items.mealId = meals.id\n"
            + "        LEFT JOIN foods ON foods.id = meal_food_items.foodId\n"
            + "        GROUP BY meals.id\n"
            + "        ORDER BY meals.createdAt DESC\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"meals", "meal_food_items",
        "foods"}, new Callable<List<MealWithTotals>>() {
      @Override
      @NonNull
      public List<MealWithTotals> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMealId = 0;
          final int _cursorIndexOfMealName = 1;
          final int _cursorIndexOfMealType = 2;
          final int _cursorIndexOfCreatedAt = 3;
          final int _cursorIndexOfTotalCalories = 4;
          final int _cursorIndexOfTotalProtein = 5;
          final int _cursorIndexOfTotalCarbs = 6;
          final int _cursorIndexOfTotalFat = 7;
          final List<MealWithTotals> _result = new ArrayList<MealWithTotals>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MealWithTotals _item;
            final long _tmpMealId;
            _tmpMealId = _cursor.getLong(_cursorIndexOfMealId);
            final String _tmpMealName;
            _tmpMealName = _cursor.getString(_cursorIndexOfMealName);
            final MealType _tmpMealType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfMealType);
            _tmpMealType = __converters.toMealType(_tmp);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final double _tmpTotalCalories;
            _tmpTotalCalories = _cursor.getDouble(_cursorIndexOfTotalCalories);
            final double _tmpTotalProtein;
            _tmpTotalProtein = _cursor.getDouble(_cursorIndexOfTotalProtein);
            final double _tmpTotalCarbs;
            _tmpTotalCarbs = _cursor.getDouble(_cursorIndexOfTotalCarbs);
            final double _tmpTotalFat;
            _tmpTotalFat = _cursor.getDouble(_cursorIndexOfTotalFat);
            _item = new MealWithTotals(_tmpMealId,_tmpMealName,_tmpMealType,_tmpCreatedAt,_tmpTotalCalories,_tmpTotalProtein,_tmpTotalCarbs,_tmpTotalFat);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<MealFoodItemWithFood>> getItemsForMeal(final long mealId) {
    final String _sql = "\n"
            + "        SELECT meal_food_items.*, foods.name AS foodName, foods.caloriesPer100g,\n"
            + "               foods.proteinPer100g, foods.carbsPer100g, foods.fatPer100g\n"
            + "        FROM meal_food_items\n"
            + "        JOIN foods ON foods.id = meal_food_items.foodId\n"
            + "        WHERE meal_food_items.mealId = ?\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, mealId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"meal_food_items",
        "foods"}, new Callable<List<MealFoodItemWithFood>>() {
      @Override
      @NonNull
      public List<MealFoodItemWithFood> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMealId = CursorUtil.getColumnIndexOrThrow(_cursor, "mealId");
          final int _cursorIndexOfFoodId = CursorUtil.getColumnIndexOrThrow(_cursor, "foodId");
          final int _cursorIndexOfGrams = CursorUtil.getColumnIndexOrThrow(_cursor, "grams");
          final int _cursorIndexOfFoodName = CursorUtil.getColumnIndexOrThrow(_cursor, "foodName");
          final int _cursorIndexOfCaloriesPer100g = CursorUtil.getColumnIndexOrThrow(_cursor, "caloriesPer100g");
          final int _cursorIndexOfProteinPer100g = CursorUtil.getColumnIndexOrThrow(_cursor, "proteinPer100g");
          final int _cursorIndexOfCarbsPer100g = CursorUtil.getColumnIndexOrThrow(_cursor, "carbsPer100g");
          final int _cursorIndexOfFatPer100g = CursorUtil.getColumnIndexOrThrow(_cursor, "fatPer100g");
          final List<MealFoodItemWithFood> _result = new ArrayList<MealFoodItemWithFood>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MealFoodItemWithFood _item;
            final String _tmpFoodName;
            _tmpFoodName = _cursor.getString(_cursorIndexOfFoodName);
            final double _tmpCaloriesPer100g;
            _tmpCaloriesPer100g = _cursor.getDouble(_cursorIndexOfCaloriesPer100g);
            final double _tmpProteinPer100g;
            _tmpProteinPer100g = _cursor.getDouble(_cursorIndexOfProteinPer100g);
            final double _tmpCarbsPer100g;
            _tmpCarbsPer100g = _cursor.getDouble(_cursorIndexOfCarbsPer100g);
            final double _tmpFatPer100g;
            _tmpFatPer100g = _cursor.getDouble(_cursorIndexOfFatPer100g);
            final MealFoodItemEntity _tmpItem;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpMealId;
            _tmpMealId = _cursor.getLong(_cursorIndexOfMealId);
            final long _tmpFoodId;
            _tmpFoodId = _cursor.getLong(_cursorIndexOfFoodId);
            final double _tmpGrams;
            _tmpGrams = _cursor.getDouble(_cursorIndexOfGrams);
            _tmpItem = new MealFoodItemEntity(_tmpId,_tmpMealId,_tmpFoodId,_tmpGrams);
            _item = new MealFoodItemWithFood(_tmpItem,_tmpFoodName,_tmpCaloriesPer100g,_tmpProteinPer100g,_tmpCarbsPer100g,_tmpFatPer100g);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

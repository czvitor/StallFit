package com.vitorsousa.stallfit.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.vitorsousa.stallfit.data.local.Converters;
import com.vitorsousa.stallfit.data.local.entity.MealEntryEntity;
import com.vitorsousa.stallfit.data.local.entity.MealType;
import com.vitorsousa.stallfit.data.local.relation.MealEntryWithFood;
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
public final class MealEntryDao_Impl implements MealEntryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MealEntryEntity> __insertionAdapterOfMealEntryEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<MealEntryEntity> __deletionAdapterOfMealEntryEntity;

  public MealEntryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMealEntryEntity = new EntityInsertionAdapter<MealEntryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `meal_entries` (`id`,`foodId`,`mealType`,`grams`,`dateEpochDay`,`loggedAt`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MealEntryEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getFoodId());
        final String _tmp = __converters.fromMealType(entity.getMealType());
        statement.bindString(3, _tmp);
        statement.bindDouble(4, entity.getGrams());
        statement.bindLong(5, entity.getDateEpochDay());
        statement.bindLong(6, entity.getLoggedAt());
      }
    };
    this.__deletionAdapterOfMealEntryEntity = new EntityDeletionOrUpdateAdapter<MealEntryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `meal_entries` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MealEntryEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final MealEntryEntity mealEntry,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfMealEntryEntity.insertAndReturnId(mealEntry);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final MealEntryEntity mealEntry,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfMealEntryEntity.handle(mealEntry);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<MealEntryWithFood>> getEntriesForDate(final long dateEpochDay) {
    final String _sql = "\n"
            + "        SELECT meal_entries.*, foods.name AS foodName, foods.caloriesPer100g,\n"
            + "               foods.proteinPer100g, foods.carbsPer100g, foods.fatPer100g\n"
            + "        FROM meal_entries\n"
            + "        JOIN foods ON foods.id = meal_entries.foodId\n"
            + "        WHERE meal_entries.dateEpochDay = ?\n"
            + "        ORDER BY meal_entries.loggedAt ASC\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, dateEpochDay);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"meal_entries",
        "foods"}, new Callable<List<MealEntryWithFood>>() {
      @Override
      @NonNull
      public List<MealEntryWithFood> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFoodId = CursorUtil.getColumnIndexOrThrow(_cursor, "foodId");
          final int _cursorIndexOfMealType = CursorUtil.getColumnIndexOrThrow(_cursor, "mealType");
          final int _cursorIndexOfGrams = CursorUtil.getColumnIndexOrThrow(_cursor, "grams");
          final int _cursorIndexOfDateEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "dateEpochDay");
          final int _cursorIndexOfLoggedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "loggedAt");
          final int _cursorIndexOfFoodName = CursorUtil.getColumnIndexOrThrow(_cursor, "foodName");
          final int _cursorIndexOfCaloriesPer100g = CursorUtil.getColumnIndexOrThrow(_cursor, "caloriesPer100g");
          final int _cursorIndexOfProteinPer100g = CursorUtil.getColumnIndexOrThrow(_cursor, "proteinPer100g");
          final int _cursorIndexOfCarbsPer100g = CursorUtil.getColumnIndexOrThrow(_cursor, "carbsPer100g");
          final int _cursorIndexOfFatPer100g = CursorUtil.getColumnIndexOrThrow(_cursor, "fatPer100g");
          final List<MealEntryWithFood> _result = new ArrayList<MealEntryWithFood>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MealEntryWithFood _item;
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
            final MealEntryEntity _tmpMealEntry;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpFoodId;
            _tmpFoodId = _cursor.getLong(_cursorIndexOfFoodId);
            final MealType _tmpMealType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfMealType);
            _tmpMealType = __converters.toMealType(_tmp);
            final double _tmpGrams;
            _tmpGrams = _cursor.getDouble(_cursorIndexOfGrams);
            final long _tmpDateEpochDay;
            _tmpDateEpochDay = _cursor.getLong(_cursorIndexOfDateEpochDay);
            final long _tmpLoggedAt;
            _tmpLoggedAt = _cursor.getLong(_cursorIndexOfLoggedAt);
            _tmpMealEntry = new MealEntryEntity(_tmpId,_tmpFoodId,_tmpMealType,_tmpGrams,_tmpDateEpochDay,_tmpLoggedAt);
            _item = new MealEntryWithFood(_tmpMealEntry,_tmpFoodName,_tmpCaloriesPer100g,_tmpProteinPer100g,_tmpCarbsPer100g,_tmpFatPer100g);
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

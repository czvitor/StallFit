package com.vitorsousa.stallfit.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.vitorsousa.stallfit.data.local.Converters;
import com.vitorsousa.stallfit.data.local.entity.ActivityLevel;
import com.vitorsousa.stallfit.data.local.entity.BiologicalSex;
import com.vitorsousa.stallfit.data.local.entity.NutritionGoal;
import com.vitorsousa.stallfit.data.local.entity.UserProfileEntity;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class UserProfileDao_Impl implements UserProfileDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserProfileEntity> __insertionAdapterOfUserProfileEntity;

  private final Converters __converters = new Converters();

  public UserProfileDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserProfileEntity = new EntityInsertionAdapter<UserProfileEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `user_profile` (`id`,`ageYears`,`weightKg`,`heightCm`,`sex`,`activityLevel`,`goal`,`armCm`,`chestCm`,`hipCm`,`thighCm`,`calfCm`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserProfileEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getAgeYears());
        statement.bindDouble(3, entity.getWeightKg());
        statement.bindDouble(4, entity.getHeightCm());
        final String _tmp = __converters.fromBiologicalSex(entity.getSex());
        statement.bindString(5, _tmp);
        final String _tmp_1 = __converters.fromActivityLevel(entity.getActivityLevel());
        statement.bindString(6, _tmp_1);
        final String _tmp_2 = __converters.fromNutritionGoal(entity.getGoal());
        statement.bindString(7, _tmp_2);
        if (entity.getArmCm() == null) {
          statement.bindNull(8);
        } else {
          statement.bindDouble(8, entity.getArmCm());
        }
        if (entity.getChestCm() == null) {
          statement.bindNull(9);
        } else {
          statement.bindDouble(9, entity.getChestCm());
        }
        if (entity.getHipCm() == null) {
          statement.bindNull(10);
        } else {
          statement.bindDouble(10, entity.getHipCm());
        }
        if (entity.getThighCm() == null) {
          statement.bindNull(11);
        } else {
          statement.bindDouble(11, entity.getThighCm());
        }
        if (entity.getCalfCm() == null) {
          statement.bindNull(12);
        } else {
          statement.bindDouble(12, entity.getCalfCm());
        }
      }
    };
  }

  @Override
  public Object upsert(final UserProfileEntity profile,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUserProfileEntity.insert(profile);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<UserProfileEntity> getProfile() {
    final String _sql = "SELECT * FROM user_profile WHERE id = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"user_profile"}, new Callable<UserProfileEntity>() {
      @Override
      @Nullable
      public UserProfileEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAgeYears = CursorUtil.getColumnIndexOrThrow(_cursor, "ageYears");
          final int _cursorIndexOfWeightKg = CursorUtil.getColumnIndexOrThrow(_cursor, "weightKg");
          final int _cursorIndexOfHeightCm = CursorUtil.getColumnIndexOrThrow(_cursor, "heightCm");
          final int _cursorIndexOfSex = CursorUtil.getColumnIndexOrThrow(_cursor, "sex");
          final int _cursorIndexOfActivityLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "activityLevel");
          final int _cursorIndexOfGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "goal");
          final int _cursorIndexOfArmCm = CursorUtil.getColumnIndexOrThrow(_cursor, "armCm");
          final int _cursorIndexOfChestCm = CursorUtil.getColumnIndexOrThrow(_cursor, "chestCm");
          final int _cursorIndexOfHipCm = CursorUtil.getColumnIndexOrThrow(_cursor, "hipCm");
          final int _cursorIndexOfThighCm = CursorUtil.getColumnIndexOrThrow(_cursor, "thighCm");
          final int _cursorIndexOfCalfCm = CursorUtil.getColumnIndexOrThrow(_cursor, "calfCm");
          final UserProfileEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpAgeYears;
            _tmpAgeYears = _cursor.getInt(_cursorIndexOfAgeYears);
            final double _tmpWeightKg;
            _tmpWeightKg = _cursor.getDouble(_cursorIndexOfWeightKg);
            final double _tmpHeightCm;
            _tmpHeightCm = _cursor.getDouble(_cursorIndexOfHeightCm);
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
            final Double _tmpArmCm;
            if (_cursor.isNull(_cursorIndexOfArmCm)) {
              _tmpArmCm = null;
            } else {
              _tmpArmCm = _cursor.getDouble(_cursorIndexOfArmCm);
            }
            final Double _tmpChestCm;
            if (_cursor.isNull(_cursorIndexOfChestCm)) {
              _tmpChestCm = null;
            } else {
              _tmpChestCm = _cursor.getDouble(_cursorIndexOfChestCm);
            }
            final Double _tmpHipCm;
            if (_cursor.isNull(_cursorIndexOfHipCm)) {
              _tmpHipCm = null;
            } else {
              _tmpHipCm = _cursor.getDouble(_cursorIndexOfHipCm);
            }
            final Double _tmpThighCm;
            if (_cursor.isNull(_cursorIndexOfThighCm)) {
              _tmpThighCm = null;
            } else {
              _tmpThighCm = _cursor.getDouble(_cursorIndexOfThighCm);
            }
            final Double _tmpCalfCm;
            if (_cursor.isNull(_cursorIndexOfCalfCm)) {
              _tmpCalfCm = null;
            } else {
              _tmpCalfCm = _cursor.getDouble(_cursorIndexOfCalfCm);
            }
            _result = new UserProfileEntity(_tmpId,_tmpAgeYears,_tmpWeightKg,_tmpHeightCm,_tmpSex,_tmpActivityLevel,_tmpGoal,_tmpArmCm,_tmpChestCm,_tmpHipCm,_tmpThighCm,_tmpCalfCm);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

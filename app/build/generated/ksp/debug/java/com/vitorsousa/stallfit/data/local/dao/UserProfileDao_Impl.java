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
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAgeYears = CursorUtil.getColumnIndexOrThrow(_cursor, "ageYears");
          final int _cursorIndexOfSex = CursorUtil.getColumnIndexOrThrow(_cursor, "sex");
          final int _cursorIndexOfActivityLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "activityLevel");
          final int _cursorIndexOfGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "goal");
          final UserProfileEntity _result;
          if (_cursor.moveToFirst()) {
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
            _result = new UserProfileEntity(_tmpId,_tmpName,_tmpAgeYears,_tmpSex,_tmpActivityLevel,_tmpGoal);
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

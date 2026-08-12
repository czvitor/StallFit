package com.vitorsousa.stallfit.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.vitorsousa.stallfit.data.local.entity.MacroGoalEntity;
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
public final class MacroGoalDao_Impl implements MacroGoalDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MacroGoalEntity> __insertionAdapterOfMacroGoalEntity;

  public MacroGoalDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
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
  }

  @Override
  public Object upsert(final MacroGoalEntity goal, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMacroGoalEntity.insert(goal);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<MacroGoalEntity> getGoal() {
    final String _sql = "SELECT * FROM macro_goal WHERE id = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"macro_goal"}, new Callable<MacroGoalEntity>() {
      @Override
      @Nullable
      public MacroGoalEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCalorieGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "calorieGoal");
          final int _cursorIndexOfProteinGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "proteinGoal");
          final int _cursorIndexOfCarbGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "carbGoal");
          final int _cursorIndexOfFatGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "fatGoal");
          final MacroGoalEntity _result;
          if (_cursor.moveToFirst()) {
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
            _result = new MacroGoalEntity(_tmpId,_tmpCalorieGoal,_tmpProteinGoal,_tmpCarbGoal,_tmpFatGoal);
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
  public Object getGoalOnce(final Continuation<? super MacroGoalEntity> $completion) {
    final String _sql = "SELECT * FROM macro_goal WHERE id = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MacroGoalEntity>() {
      @Override
      @Nullable
      public MacroGoalEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCalorieGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "calorieGoal");
          final int _cursorIndexOfProteinGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "proteinGoal");
          final int _cursorIndexOfCarbGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "carbGoal");
          final int _cursorIndexOfFatGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "fatGoal");
          final MacroGoalEntity _result;
          if (_cursor.moveToFirst()) {
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
            _result = new MacroGoalEntity(_tmpId,_tmpCalorieGoal,_tmpProteinGoal,_tmpCarbGoal,_tmpFatGoal);
          } else {
            _result = null;
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

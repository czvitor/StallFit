package com.vitorsousa.stallfit.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.vitorsousa.stallfit.data.local.entity.SetEntryEntity;
import com.vitorsousa.stallfit.data.local.relation.SetEntryWithExercise;
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
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SetEntryDao_Impl implements SetEntryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SetEntryEntity> __insertionAdapterOfSetEntryEntity;

  private final EntityDeletionOrUpdateAdapter<SetEntryEntity> __deletionAdapterOfSetEntryEntity;

  public SetEntryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSetEntryEntity = new EntityInsertionAdapter<SetEntryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `set_entries` (`id`,`sessionId`,`exerciseId`,`setNumber`,`reps`,`weightKg`,`loggedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
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
    this.__deletionAdapterOfSetEntryEntity = new EntityDeletionOrUpdateAdapter<SetEntryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `set_entries` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SetEntryEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final SetEntryEntity setEntry,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfSetEntryEntity.insertAndReturnId(setEntry);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final SetEntryEntity setEntry,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfSetEntryEntity.handle(setEntry);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<SetEntryWithExercise>> getSetsForSession(final long sessionId) {
    final String _sql = "\n"
            + "        SELECT set_entries.*, exercises.name AS exerciseName\n"
            + "        FROM set_entries\n"
            + "        JOIN exercises ON exercises.id = set_entries.exerciseId\n"
            + "        WHERE set_entries.sessionId = ?\n"
            + "        ORDER BY set_entries.loggedAt ASC\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, sessionId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"set_entries",
        "exercises"}, new Callable<List<SetEntryWithExercise>>() {
      @Override
      @NonNull
      public List<SetEntryWithExercise> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSessionId = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionId");
          final int _cursorIndexOfExerciseId = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseId");
          final int _cursorIndexOfSetNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "setNumber");
          final int _cursorIndexOfReps = CursorUtil.getColumnIndexOrThrow(_cursor, "reps");
          final int _cursorIndexOfWeightKg = CursorUtil.getColumnIndexOrThrow(_cursor, "weightKg");
          final int _cursorIndexOfLoggedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "loggedAt");
          final int _cursorIndexOfExerciseName = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseName");
          final List<SetEntryWithExercise> _result = new ArrayList<SetEntryWithExercise>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SetEntryWithExercise _item;
            final String _tmpExerciseName;
            _tmpExerciseName = _cursor.getString(_cursorIndexOfExerciseName);
            final SetEntryEntity _tmpSetEntry;
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
            _tmpSetEntry = new SetEntryEntity(_tmpId,_tmpSessionId,_tmpExerciseId,_tmpSetNumber,_tmpReps,_tmpWeightKg,_tmpLoggedAt);
            _item = new SetEntryWithExercise(_tmpSetEntry,_tmpExerciseName);
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
  public Object getLastSetForExercise(final long exerciseId,
      final Continuation<? super SetEntryEntity> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM set_entries\n"
            + "        WHERE exerciseId = ?\n"
            + "        ORDER BY loggedAt DESC\n"
            + "        LIMIT 1\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, exerciseId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SetEntryEntity>() {
      @Override
      @Nullable
      public SetEntryEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSessionId = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionId");
          final int _cursorIndexOfExerciseId = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseId");
          final int _cursorIndexOfSetNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "setNumber");
          final int _cursorIndexOfReps = CursorUtil.getColumnIndexOrThrow(_cursor, "reps");
          final int _cursorIndexOfWeightKg = CursorUtil.getColumnIndexOrThrow(_cursor, "weightKg");
          final int _cursorIndexOfLoggedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "loggedAt");
          final SetEntryEntity _result;
          if (_cursor.moveToFirst()) {
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
            _result = new SetEntryEntity(_tmpId,_tmpSessionId,_tmpExerciseId,_tmpSetNumber,_tmpReps,_tmpWeightKg,_tmpLoggedAt);
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

  @Override
  public Flow<Double> getVolumeForSession(final long sessionId) {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(reps * weightKg), 0.0)\n"
            + "        FROM set_entries\n"
            + "        WHERE sessionId = ?\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, sessionId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"set_entries"}, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp;
            _tmp = _cursor.getDouble(0);
            _result = _tmp;
          } else {
            _result = 0.0;
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
  public Flow<Double> getVolumeBetween(final long startMillis, final long endMillis) {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(set_entries.reps * set_entries.weightKg), 0.0)\n"
            + "        FROM set_entries\n"
            + "        JOIN workout_sessions ON workout_sessions.id = set_entries.sessionId\n"
            + "        WHERE workout_sessions.startedAt BETWEEN ? AND ?\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startMillis);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endMillis);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"set_entries",
        "workout_sessions"}, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp;
            _tmp = _cursor.getDouble(0);
            _result = _tmp;
          } else {
            _result = 0.0;
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

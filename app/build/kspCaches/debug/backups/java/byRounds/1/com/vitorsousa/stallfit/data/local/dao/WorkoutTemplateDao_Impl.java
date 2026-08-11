package com.vitorsousa.stallfit.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.vitorsousa.stallfit.data.local.Converters;
import com.vitorsousa.stallfit.data.local.entity.Equipment;
import com.vitorsousa.stallfit.data.local.entity.Intensity;
import com.vitorsousa.stallfit.data.local.entity.TemplateExerciseEntity;
import com.vitorsousa.stallfit.data.local.entity.WorkoutTemplateEntity;
import com.vitorsousa.stallfit.data.local.relation.TemplateExerciseWithExercise;
import com.vitorsousa.stallfit.data.local.relation.TemplateWithExercises;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
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
public final class WorkoutTemplateDao_Impl implements WorkoutTemplateDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<WorkoutTemplateEntity> __insertionAdapterOfWorkoutTemplateEntity;

  private final EntityInsertionAdapter<TemplateExerciseEntity> __insertionAdapterOfTemplateExerciseEntity;

  private final Converters __converters = new Converters();

  private final SharedSQLiteStatement __preparedStmtOfDeleteTemplate;

  public WorkoutTemplateDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfWorkoutTemplateEntity = new EntityInsertionAdapter<WorkoutTemplateEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `workout_templates` (`id`,`title`,`createdAt`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WorkoutTemplateEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindLong(3, entity.getCreatedAt());
      }
    };
    this.__insertionAdapterOfTemplateExerciseEntity = new EntityInsertionAdapter<TemplateExerciseEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `template_exercises` (`id`,`templateId`,`exerciseId`,`orderIndex`,`sets`,`repRangeMin`,`repRangeMax`,`restSeconds`,`intensity`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
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
    this.__preparedStmtOfDeleteTemplate = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM workout_templates WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertTemplate(final WorkoutTemplateEntity template,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfWorkoutTemplateEntity.insertAndReturnId(template);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertTemplateExercises(final List<TemplateExerciseEntity> exercises,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTemplateExerciseEntity.insert(exercises);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertTemplateWithExercises(final WorkoutTemplateEntity template,
      final List<TemplateExerciseEntity> exercises, final Continuation<? super Long> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> WorkoutTemplateDao.DefaultImpls.insertTemplateWithExercises(WorkoutTemplateDao_Impl.this, template, exercises, __cont), $completion);
  }

  @Override
  public Object deleteTemplate(final long templateId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteTemplate.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, templateId);
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
          __preparedStmtOfDeleteTemplate.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getTemplateOnce(final long templateId,
      final Continuation<? super WorkoutTemplateEntity> $completion) {
    final String _sql = "SELECT * FROM workout_templates WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, templateId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<WorkoutTemplateEntity>() {
      @Override
      @Nullable
      public WorkoutTemplateEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final WorkoutTemplateEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new WorkoutTemplateEntity(_tmpId,_tmpTitle,_tmpCreatedAt);
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
  public Flow<WorkoutTemplateEntity> getTemplateFlow(final long templateId) {
    final String _sql = "SELECT * FROM workout_templates WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, templateId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"workout_templates"}, new Callable<WorkoutTemplateEntity>() {
      @Override
      @Nullable
      public WorkoutTemplateEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final WorkoutTemplateEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new WorkoutTemplateEntity(_tmpId,_tmpTitle,_tmpCreatedAt);
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
  public Flow<List<TemplateWithExercises>> getAllTemplatesWithExercises() {
    final String _sql = "SELECT * FROM workout_templates ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"template_exercises",
        "workout_templates"}, new Callable<List<TemplateWithExercises>>() {
      @Override
      @NonNull
      public List<TemplateWithExercises> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
            final LongSparseArray<ArrayList<TemplateExerciseEntity>> _collectionExercises = new LongSparseArray<ArrayList<TemplateExerciseEntity>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionExercises.containsKey(_tmpKey)) {
                _collectionExercises.put(_tmpKey, new ArrayList<TemplateExerciseEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshiptemplateExercisesAscomVitorsousaStallfitDataLocalEntityTemplateExerciseEntity(_collectionExercises);
            final List<TemplateWithExercises> _result = new ArrayList<TemplateWithExercises>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final TemplateWithExercises _item;
              final WorkoutTemplateEntity _tmpTemplate;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpTitle;
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
              final long _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              _tmpTemplate = new WorkoutTemplateEntity(_tmpId,_tmpTitle,_tmpCreatedAt);
              final ArrayList<TemplateExerciseEntity> _tmpExercisesCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpExercisesCollection = _collectionExercises.get(_tmpKey_1);
              _item = new TemplateWithExercises(_tmpTemplate,_tmpExercisesCollection);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<TemplateExerciseWithExercise>> getExercisesForTemplate(final long templateId) {
    final String _sql = "\n"
            + "        SELECT template_exercises.*, exercises.name AS exerciseName,\n"
            + "               exercises.muscleGroup AS muscleGroup, exercises.equipment AS equipment\n"
            + "        FROM template_exercises\n"
            + "        JOIN exercises ON exercises.id = template_exercises.exerciseId\n"
            + "        WHERE template_exercises.templateId = ?\n"
            + "        ORDER BY template_exercises.orderIndex ASC\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, templateId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"template_exercises",
        "exercises"}, new Callable<List<TemplateExerciseWithExercise>>() {
      @Override
      @NonNull
      public List<TemplateExerciseWithExercise> call() throws Exception {
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
          final int _cursorIndexOfExerciseName = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseName");
          final int _cursorIndexOfMuscleGroup = CursorUtil.getColumnIndexOrThrow(_cursor, "muscleGroup");
          final int _cursorIndexOfEquipment = CursorUtil.getColumnIndexOrThrow(_cursor, "equipment");
          final List<TemplateExerciseWithExercise> _result = new ArrayList<TemplateExerciseWithExercise>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TemplateExerciseWithExercise _item;
            final String _tmpExerciseName;
            _tmpExerciseName = _cursor.getString(_cursorIndexOfExerciseName);
            final String _tmpMuscleGroup;
            _tmpMuscleGroup = _cursor.getString(_cursorIndexOfMuscleGroup);
            final Equipment _tmpEquipment;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfEquipment);
            _tmpEquipment = __converters.toEquipment(_tmp);
            final TemplateExerciseEntity _tmpTemplateExercise;
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
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfIntensity);
            _tmpIntensity = __converters.toIntensity(_tmp_1);
            _tmpTemplateExercise = new TemplateExerciseEntity(_tmpId,_tmpTemplateId,_tmpExerciseId,_tmpOrderIndex,_tmpSets,_tmpRepRangeMin,_tmpRepRangeMax,_tmpRestSeconds,_tmpIntensity);
            _item = new TemplateExerciseWithExercise(_tmpTemplateExercise,_tmpExerciseName,_tmpMuscleGroup,_tmpEquipment);
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

  private void __fetchRelationshiptemplateExercisesAscomVitorsousaStallfitDataLocalEntityTemplateExerciseEntity(
      @NonNull final LongSparseArray<ArrayList<TemplateExerciseEntity>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshiptemplateExercisesAscomVitorsousaStallfitDataLocalEntityTemplateExerciseEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`templateId`,`exerciseId`,`orderIndex`,`sets`,`repRangeMin`,`repRangeMax`,`restSeconds`,`intensity` FROM `template_exercises` WHERE `templateId` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "templateId");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfTemplateId = 1;
      final int _cursorIndexOfExerciseId = 2;
      final int _cursorIndexOfOrderIndex = 3;
      final int _cursorIndexOfSets = 4;
      final int _cursorIndexOfRepRangeMin = 5;
      final int _cursorIndexOfRepRangeMax = 6;
      final int _cursorIndexOfRestSeconds = 7;
      final int _cursorIndexOfIntensity = 8;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<TemplateExerciseEntity> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final TemplateExerciseEntity _item_1;
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
          _item_1 = new TemplateExerciseEntity(_tmpId,_tmpTemplateId,_tmpExerciseId,_tmpOrderIndex,_tmpSets,_tmpRepRangeMin,_tmpRepRangeMax,_tmpRestSeconds,_tmpIntensity);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}

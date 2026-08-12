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
import com.vitorsousa.stallfit.data.local.entity.BodyMeasurementEntity;
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
public final class BodyMeasurementDao_Impl implements BodyMeasurementDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BodyMeasurementEntity> __insertionAdapterOfBodyMeasurementEntity;

  private final EntityDeletionOrUpdateAdapter<BodyMeasurementEntity> __deletionAdapterOfBodyMeasurementEntity;

  public BodyMeasurementDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBodyMeasurementEntity = new EntityInsertionAdapter<BodyMeasurementEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `body_measurements` (`id`,`createdAt`,`weightKg`,`heightCm`,`chestCm`,`waistCm`,`abdomenCm`,`hipCm`,`armLeftRelaxedCm`,`armLeftFlexedCm`,`armRightRelaxedCm`,`armRightFlexedCm`,`forearmLeftCm`,`forearmRightCm`,`thighLeftCm`,`thighRightCm`,`calfLeftCm`,`calfRightCm`,`bodyFatPercent`,`leanMassKg`,`fatMassKg`,`bodyWaterPercent`,`tricepsFoldMm`,`subscapularFoldMm`,`suprailiacFoldMm`,`abdominalFoldMm`,`thighFoldMm`,`chestFoldMm`,`midaxillaryFoldMm`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
    this.__deletionAdapterOfBodyMeasurementEntity = new EntityDeletionOrUpdateAdapter<BodyMeasurementEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `body_measurements` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BodyMeasurementEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final BodyMeasurementEntity measurement,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfBodyMeasurementEntity.insertAndReturnId(measurement);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final BodyMeasurementEntity measurement,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfBodyMeasurementEntity.handle(measurement);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<BodyMeasurementEntity>> getAll() {
    final String _sql = "SELECT * FROM body_measurements ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"body_measurements"}, new Callable<List<BodyMeasurementEntity>>() {
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
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<BodyMeasurementEntity> getLatest() {
    final String _sql = "SELECT * FROM body_measurements ORDER BY createdAt DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"body_measurements"}, new Callable<BodyMeasurementEntity>() {
      @Override
      @Nullable
      public BodyMeasurementEntity call() throws Exception {
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
          final BodyMeasurementEntity _result;
          if (_cursor.moveToFirst()) {
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
            _result = new BodyMeasurementEntity(_tmpId,_tmpCreatedAt,_tmpWeightKg,_tmpHeightCm,_tmpChestCm,_tmpWaistCm,_tmpAbdomenCm,_tmpHipCm,_tmpArmLeftRelaxedCm,_tmpArmLeftFlexedCm,_tmpArmRightRelaxedCm,_tmpArmRightFlexedCm,_tmpForearmLeftCm,_tmpForearmRightCm,_tmpThighLeftCm,_tmpThighRightCm,_tmpCalfLeftCm,_tmpCalfRightCm,_tmpBodyFatPercent,_tmpLeanMassKg,_tmpFatMassKg,_tmpBodyWaterPercent,_tmpTricepsFoldMm,_tmpSubscapularFoldMm,_tmpSuprailiacFoldMm,_tmpAbdominalFoldMm,_tmpThighFoldMm,_tmpChestFoldMm,_tmpMidaxillaryFoldMm);
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
  public Object getLatestOnce(final Continuation<? super BodyMeasurementEntity> $completion) {
    final String _sql = "SELECT * FROM body_measurements ORDER BY createdAt DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<BodyMeasurementEntity>() {
      @Override
      @Nullable
      public BodyMeasurementEntity call() throws Exception {
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
          final BodyMeasurementEntity _result;
          if (_cursor.moveToFirst()) {
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
            _result = new BodyMeasurementEntity(_tmpId,_tmpCreatedAt,_tmpWeightKg,_tmpHeightCm,_tmpChestCm,_tmpWaistCm,_tmpAbdomenCm,_tmpHipCm,_tmpArmLeftRelaxedCm,_tmpArmLeftFlexedCm,_tmpArmRightRelaxedCm,_tmpArmRightFlexedCm,_tmpForearmLeftCm,_tmpForearmRightCm,_tmpThighLeftCm,_tmpThighRightCm,_tmpCalfLeftCm,_tmpCalfRightCm,_tmpBodyFatPercent,_tmpLeanMassKg,_tmpFatMassKg,_tmpBodyWaterPercent,_tmpTricepsFoldMm,_tmpSubscapularFoldMm,_tmpSuprailiacFoldMm,_tmpAbdominalFoldMm,_tmpThighFoldMm,_tmpChestFoldMm,_tmpMidaxillaryFoldMm);
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

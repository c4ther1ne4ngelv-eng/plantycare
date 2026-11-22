package com.example.plantycare.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.plantycare.data.local.entity.PlantLogEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PlantLogDao_Impl implements PlantLogDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PlantLogEntity> __insertionAdapterOfPlantLogEntity;

  public PlantLogDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPlantLogEntity = new EntityInsertionAdapter<PlantLogEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `plant_logs` (`id`,`plantId`,`logType`,`description`,`recordedValue`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PlantLogEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getPlantId());
        statement.bindString(3, entity.getLogType());
        statement.bindString(4, entity.getDescription());
        statement.bindString(5, entity.getRecordedValue());
        statement.bindLong(6, entity.getTimestamp());
      }
    };
  }

  @Override
  public Object insertLog(final PlantLogEntity log, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfPlantLogEntity.insertAndReturnId(log);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PlantLogEntity>> getAllLogs() {
    final String _sql = "SELECT * FROM plant_logs ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"plant_logs"}, new Callable<List<PlantLogEntity>>() {
      @Override
      @NonNull
      public List<PlantLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPlantId = CursorUtil.getColumnIndexOrThrow(_cursor, "plantId");
          final int _cursorIndexOfLogType = CursorUtil.getColumnIndexOrThrow(_cursor, "logType");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfRecordedValue = CursorUtil.getColumnIndexOrThrow(_cursor, "recordedValue");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<PlantLogEntity> _result = new ArrayList<PlantLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PlantLogEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPlantId;
            _tmpPlantId = _cursor.getLong(_cursorIndexOfPlantId);
            final String _tmpLogType;
            _tmpLogType = _cursor.getString(_cursorIndexOfLogType);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpRecordedValue;
            _tmpRecordedValue = _cursor.getString(_cursorIndexOfRecordedValue);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new PlantLogEntity(_tmpId,_tmpPlantId,_tmpLogType,_tmpDescription,_tmpRecordedValue,_tmpTimestamp);
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
  public Flow<List<PlantLogEntity>> getLogsByPlant(final long plantId) {
    final String _sql = "SELECT * FROM plant_logs WHERE plantId = ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, plantId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"plant_logs"}, new Callable<List<PlantLogEntity>>() {
      @Override
      @NonNull
      public List<PlantLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPlantId = CursorUtil.getColumnIndexOrThrow(_cursor, "plantId");
          final int _cursorIndexOfLogType = CursorUtil.getColumnIndexOrThrow(_cursor, "logType");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfRecordedValue = CursorUtil.getColumnIndexOrThrow(_cursor, "recordedValue");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<PlantLogEntity> _result = new ArrayList<PlantLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PlantLogEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPlantId;
            _tmpPlantId = _cursor.getLong(_cursorIndexOfPlantId);
            final String _tmpLogType;
            _tmpLogType = _cursor.getString(_cursorIndexOfLogType);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpRecordedValue;
            _tmpRecordedValue = _cursor.getString(_cursorIndexOfRecordedValue);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new PlantLogEntity(_tmpId,_tmpPlantId,_tmpLogType,_tmpDescription,_tmpRecordedValue,_tmpTimestamp);
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
  public Flow<Integer> getTotalWateringCount() {
    final String _sql = "SELECT COUNT(*) FROM plant_logs WHERE logType LIKE 'WATERING%'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"plant_logs"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
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

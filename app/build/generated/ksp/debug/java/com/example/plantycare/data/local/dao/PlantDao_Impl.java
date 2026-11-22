package com.example.plantycare.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.plantycare.data.local.entity.PlantEntity;
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
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PlantDao_Impl implements PlantDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PlantEntity> __insertionAdapterOfPlantEntity;

  private final EntityDeletionOrUpdateAdapter<PlantEntity> __deletionAdapterOfPlantEntity;

  private final EntityDeletionOrUpdateAdapter<PlantEntity> __updateAdapterOfPlantEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateLastWatered;

  private final SharedSQLiteStatement __preparedStmtOfUpdatePhoto;

  public PlantDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPlantEntity = new EntityInsertionAdapter<PlantEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `plants` (`id`,`name`,`species`,`location`,`minLux`,`maxLux`,`wateringIntervalDays`,`wateringIntervalHours`,`lastWateredTimestamp`,`photoUri`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PlantEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getSpecies());
        statement.bindString(4, entity.getLocation());
        statement.bindDouble(5, entity.getMinLux());
        statement.bindDouble(6, entity.getMaxLux());
        statement.bindLong(7, entity.getWateringIntervalDays());
        statement.bindLong(8, entity.getWateringIntervalHours());
        statement.bindLong(9, entity.getLastWateredTimestamp());
        statement.bindString(10, entity.getPhotoUri());
      }
    };
    this.__deletionAdapterOfPlantEntity = new EntityDeletionOrUpdateAdapter<PlantEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `plants` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PlantEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfPlantEntity = new EntityDeletionOrUpdateAdapter<PlantEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `plants` SET `id` = ?,`name` = ?,`species` = ?,`location` = ?,`minLux` = ?,`maxLux` = ?,`wateringIntervalDays` = ?,`wateringIntervalHours` = ?,`lastWateredTimestamp` = ?,`photoUri` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PlantEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getSpecies());
        statement.bindString(4, entity.getLocation());
        statement.bindDouble(5, entity.getMinLux());
        statement.bindDouble(6, entity.getMaxLux());
        statement.bindLong(7, entity.getWateringIntervalDays());
        statement.bindLong(8, entity.getWateringIntervalHours());
        statement.bindLong(9, entity.getLastWateredTimestamp());
        statement.bindString(10, entity.getPhotoUri());
        statement.bindLong(11, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateLastWatered = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE plants SET lastWateredTimestamp = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdatePhoto = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE plants SET photoUri = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertPlant(final PlantEntity plant, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfPlantEntity.insertAndReturnId(plant);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deletePlant(final PlantEntity plant, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfPlantEntity.handle(plant);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updatePlant(final PlantEntity plant, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfPlantEntity.handle(plant);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateLastWatered(final long plantId, final long timestamp,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateLastWatered.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, timestamp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, plantId);
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
          __preparedStmtOfUpdateLastWatered.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updatePhoto(final long plantId, final String photoUri,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdatePhoto.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, photoUri);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, plantId);
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
          __preparedStmtOfUpdatePhoto.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PlantEntity>> getAllPlants() {
    final String _sql = "SELECT * FROM plants ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"plants"}, new Callable<List<PlantEntity>>() {
      @Override
      @NonNull
      public List<PlantEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSpecies = CursorUtil.getColumnIndexOrThrow(_cursor, "species");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfMinLux = CursorUtil.getColumnIndexOrThrow(_cursor, "minLux");
          final int _cursorIndexOfMaxLux = CursorUtil.getColumnIndexOrThrow(_cursor, "maxLux");
          final int _cursorIndexOfWateringIntervalDays = CursorUtil.getColumnIndexOrThrow(_cursor, "wateringIntervalDays");
          final int _cursorIndexOfWateringIntervalHours = CursorUtil.getColumnIndexOrThrow(_cursor, "wateringIntervalHours");
          final int _cursorIndexOfLastWateredTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastWateredTimestamp");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final List<PlantEntity> _result = new ArrayList<PlantEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PlantEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpSpecies;
            _tmpSpecies = _cursor.getString(_cursorIndexOfSpecies);
            final String _tmpLocation;
            _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            final float _tmpMinLux;
            _tmpMinLux = _cursor.getFloat(_cursorIndexOfMinLux);
            final float _tmpMaxLux;
            _tmpMaxLux = _cursor.getFloat(_cursorIndexOfMaxLux);
            final int _tmpWateringIntervalDays;
            _tmpWateringIntervalDays = _cursor.getInt(_cursorIndexOfWateringIntervalDays);
            final int _tmpWateringIntervalHours;
            _tmpWateringIntervalHours = _cursor.getInt(_cursorIndexOfWateringIntervalHours);
            final long _tmpLastWateredTimestamp;
            _tmpLastWateredTimestamp = _cursor.getLong(_cursorIndexOfLastWateredTimestamp);
            final String _tmpPhotoUri;
            _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            _item = new PlantEntity(_tmpId,_tmpName,_tmpSpecies,_tmpLocation,_tmpMinLux,_tmpMaxLux,_tmpWateringIntervalDays,_tmpWateringIntervalHours,_tmpLastWateredTimestamp,_tmpPhotoUri);
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
  public Flow<PlantEntity> getPlantById(final long id) {
    final String _sql = "SELECT * FROM plants WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"plants"}, new Callable<PlantEntity>() {
      @Override
      @Nullable
      public PlantEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSpecies = CursorUtil.getColumnIndexOrThrow(_cursor, "species");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfMinLux = CursorUtil.getColumnIndexOrThrow(_cursor, "minLux");
          final int _cursorIndexOfMaxLux = CursorUtil.getColumnIndexOrThrow(_cursor, "maxLux");
          final int _cursorIndexOfWateringIntervalDays = CursorUtil.getColumnIndexOrThrow(_cursor, "wateringIntervalDays");
          final int _cursorIndexOfWateringIntervalHours = CursorUtil.getColumnIndexOrThrow(_cursor, "wateringIntervalHours");
          final int _cursorIndexOfLastWateredTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastWateredTimestamp");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final PlantEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpSpecies;
            _tmpSpecies = _cursor.getString(_cursorIndexOfSpecies);
            final String _tmpLocation;
            _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            final float _tmpMinLux;
            _tmpMinLux = _cursor.getFloat(_cursorIndexOfMinLux);
            final float _tmpMaxLux;
            _tmpMaxLux = _cursor.getFloat(_cursorIndexOfMaxLux);
            final int _tmpWateringIntervalDays;
            _tmpWateringIntervalDays = _cursor.getInt(_cursorIndexOfWateringIntervalDays);
            final int _tmpWateringIntervalHours;
            _tmpWateringIntervalHours = _cursor.getInt(_cursorIndexOfWateringIntervalHours);
            final long _tmpLastWateredTimestamp;
            _tmpLastWateredTimestamp = _cursor.getLong(_cursorIndexOfLastWateredTimestamp);
            final String _tmpPhotoUri;
            _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            _result = new PlantEntity(_tmpId,_tmpName,_tmpSpecies,_tmpLocation,_tmpMinLux,_tmpMaxLux,_tmpWateringIntervalDays,_tmpWateringIntervalHours,_tmpLastWateredTimestamp,_tmpPhotoUri);
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
  public Flow<Integer> getPlantCount() {
    final String _sql = "SELECT COUNT(*) FROM plants";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"plants"}, new Callable<Integer>() {
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

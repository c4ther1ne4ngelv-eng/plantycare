package com.example.plantycare.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.example.plantycare.data.local.dao.PlantDao;
import com.example.plantycare.data.local.dao.PlantDao_Impl;
import com.example.plantycare.data.local.dao.PlantLogDao;
import com.example.plantycare.data.local.dao.PlantLogDao_Impl;
import com.example.plantycare.data.local.dao.UserProfileDao;
import com.example.plantycare.data.local.dao.UserProfileDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PlantDatabase_Impl extends PlantDatabase {
  private volatile PlantDao _plantDao;

  private volatile PlantLogDao _plantLogDao;

  private volatile UserProfileDao _userProfileDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(4) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `plants` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `species` TEXT NOT NULL, `location` TEXT NOT NULL, `minLux` REAL NOT NULL, `maxLux` REAL NOT NULL, `wateringIntervalDays` INTEGER NOT NULL, `wateringIntervalHours` INTEGER NOT NULL, `lastWateredTimestamp` INTEGER NOT NULL, `photoUri` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `plant_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `plantId` INTEGER NOT NULL, `logType` TEXT NOT NULL, `description` TEXT NOT NULL, `recordedValue` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, FOREIGN KEY(`plantId`) REFERENCES `plants`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_plant_logs_plantId` ON `plant_logs` (`plantId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `user_profile` (`id` INTEGER NOT NULL, `displayName` TEXT NOT NULL, `username` TEXT NOT NULL, `email` TEXT NOT NULL, `passwordHash` TEXT NOT NULL, `photoUri` TEXT, `gardenerLevel` TEXT NOT NULL, `isLoggedIn` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'bfbe32911b6743141e665864b1e78997')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `plants`");
        db.execSQL("DROP TABLE IF EXISTS `plant_logs`");
        db.execSQL("DROP TABLE IF EXISTS `user_profile`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsPlants = new HashMap<String, TableInfo.Column>(10);
        _columnsPlants.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlants.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlants.put("species", new TableInfo.Column("species", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlants.put("location", new TableInfo.Column("location", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlants.put("minLux", new TableInfo.Column("minLux", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlants.put("maxLux", new TableInfo.Column("maxLux", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlants.put("wateringIntervalDays", new TableInfo.Column("wateringIntervalDays", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlants.put("wateringIntervalHours", new TableInfo.Column("wateringIntervalHours", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlants.put("lastWateredTimestamp", new TableInfo.Column("lastWateredTimestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlants.put("photoUri", new TableInfo.Column("photoUri", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPlants = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPlants = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPlants = new TableInfo("plants", _columnsPlants, _foreignKeysPlants, _indicesPlants);
        final TableInfo _existingPlants = TableInfo.read(db, "plants");
        if (!_infoPlants.equals(_existingPlants)) {
          return new RoomOpenHelper.ValidationResult(false, "plants(com.example.plantycare.data.local.entity.PlantEntity).\n"
                  + " Expected:\n" + _infoPlants + "\n"
                  + " Found:\n" + _existingPlants);
        }
        final HashMap<String, TableInfo.Column> _columnsPlantLogs = new HashMap<String, TableInfo.Column>(6);
        _columnsPlantLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlantLogs.put("plantId", new TableInfo.Column("plantId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlantLogs.put("logType", new TableInfo.Column("logType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlantLogs.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlantLogs.put("recordedValue", new TableInfo.Column("recordedValue", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlantLogs.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPlantLogs = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysPlantLogs.add(new TableInfo.ForeignKey("plants", "CASCADE", "NO ACTION", Arrays.asList("plantId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesPlantLogs = new HashSet<TableInfo.Index>(1);
        _indicesPlantLogs.add(new TableInfo.Index("index_plant_logs_plantId", false, Arrays.asList("plantId"), Arrays.asList("ASC")));
        final TableInfo _infoPlantLogs = new TableInfo("plant_logs", _columnsPlantLogs, _foreignKeysPlantLogs, _indicesPlantLogs);
        final TableInfo _existingPlantLogs = TableInfo.read(db, "plant_logs");
        if (!_infoPlantLogs.equals(_existingPlantLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "plant_logs(com.example.plantycare.data.local.entity.PlantLogEntity).\n"
                  + " Expected:\n" + _infoPlantLogs + "\n"
                  + " Found:\n" + _existingPlantLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsUserProfile = new HashMap<String, TableInfo.Column>(8);
        _columnsUserProfile.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("displayName", new TableInfo.Column("displayName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("username", new TableInfo.Column("username", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("email", new TableInfo.Column("email", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("passwordHash", new TableInfo.Column("passwordHash", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("photoUri", new TableInfo.Column("photoUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("gardenerLevel", new TableInfo.Column("gardenerLevel", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("isLoggedIn", new TableInfo.Column("isLoggedIn", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUserProfile = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUserProfile = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUserProfile = new TableInfo("user_profile", _columnsUserProfile, _foreignKeysUserProfile, _indicesUserProfile);
        final TableInfo _existingUserProfile = TableInfo.read(db, "user_profile");
        if (!_infoUserProfile.equals(_existingUserProfile)) {
          return new RoomOpenHelper.ValidationResult(false, "user_profile(com.example.plantycare.data.local.entity.UserProfileEntity).\n"
                  + " Expected:\n" + _infoUserProfile + "\n"
                  + " Found:\n" + _existingUserProfile);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "bfbe32911b6743141e665864b1e78997", "93617acd656f8a8ab66b3813406468af");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "plants","plant_logs","user_profile");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `plants`");
      _db.execSQL("DELETE FROM `plant_logs`");
      _db.execSQL("DELETE FROM `user_profile`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(PlantDao.class, PlantDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PlantLogDao.class, PlantLogDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(UserProfileDao.class, UserProfileDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public PlantDao plantDao() {
    if (_plantDao != null) {
      return _plantDao;
    } else {
      synchronized(this) {
        if(_plantDao == null) {
          _plantDao = new PlantDao_Impl(this);
        }
        return _plantDao;
      }
    }
  }

  @Override
  public PlantLogDao plantLogDao() {
    if (_plantLogDao != null) {
      return _plantLogDao;
    } else {
      synchronized(this) {
        if(_plantLogDao == null) {
          _plantLogDao = new PlantLogDao_Impl(this);
        }
        return _plantLogDao;
      }
    }
  }

  @Override
  public UserProfileDao userProfileDao() {
    if (_userProfileDao != null) {
      return _userProfileDao;
    } else {
      synchronized(this) {
        if(_userProfileDao == null) {
          _userProfileDao = new UserProfileDao_Impl(this);
        }
        return _userProfileDao;
      }
    }
  }
}

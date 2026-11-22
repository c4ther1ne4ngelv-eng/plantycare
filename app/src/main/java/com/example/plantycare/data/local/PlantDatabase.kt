package com.example.plantycare.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.plantycare.data.local.dao.PlantDao
import com.example.plantycare.data.local.dao.PlantLogDao
import com.example.plantycare.data.local.dao.UserProfileDao
import com.example.plantycare.data.local.entity.PlantEntity
import com.example.plantycare.data.local.entity.PlantLogEntity
import com.example.plantycare.data.local.entity.UserProfileEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [PlantEntity::class, PlantLogEntity::class, UserProfileEntity::class],
    version = 4,
    exportSchema = false
)
abstract class PlantDatabase : RoomDatabase() {

    abstract fun plantDao(): PlantDao
    abstract fun plantLogDao(): PlantLogDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: PlantDatabase? = null

        fun getDatabase(context: Context): PlantDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlantDatabase::class.java,
                    "plantycare_database.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database)
                    }
                }
            }

            private suspend fun populateInitialData(db: PlantDatabase) {
                // Initial Default User Profile (Starts with isLoggedIn = false)
                db.userProfileDao().insertOrUpdateProfile(
                    UserProfileEntity(
                        id = 1,
                        displayName = "Nabil Ahmad",
                        username = "nabilgardener",
                        email = "nabil@plantycare.app",
                        passwordHash = "123456",
                        gardenerLevel = "Level 3 Green Thumb",
                        isLoggedIn = false
                    )
                )

                // Initial Seed Plants with REAL HIGH-RES PHOTOS
                val p1Id = db.plantDao().insertPlant(
                    PlantEntity(
                        name = "Monstera Deliciosa",
                        species = "Monstera deliciosa",
                        location = "Ruang Tamu Dekat Jendela",
                        minLux = 1000f,
                        maxLux = 2500f,
                        wateringIntervalDays = 4,
                        lastWateredTimestamp = System.currentTimeMillis() - (2 * 24 * 60 * 60 * 1000L),
                        photoUri = "https://images.unsplash.com/photo-1614594975525-e45190c55d0b?w=600&auto=format&fit=crop&q=80"
                    )
                )

                val p2Id = db.plantDao().insertPlant(
                    PlantEntity(
                        name = "Lidah Mertua",
                        species = "Sansevieria trifasciata",
                        location = "Meja Belajar",
                        minLux = 400f,
                        maxLux = 1200f,
                        wateringIntervalDays = 7,
                        lastWateredTimestamp = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000L),
                        photoUri = "https://images.unsplash.com/photo-1593482892290-f54927ae1bf6?w=600&auto=format&fit=crop&q=80"
                    )
                )

                // Initial Sample Logs
                db.plantLogDao().insertLog(
                    PlantLogEntity(
                        plantId = p1Id,
                        logType = "WATERING_SHAKE",
                        description = "Disiram melalui gesture goyang ponsel (Accelerometer)",
                        recordedValue = "Shake Sensor Active",
                        timestamp = System.currentTimeMillis() - (2 * 24 * 60 * 60 * 1000L)
                    )
                )

                db.plantLogDao().insertLog(
                    PlantLogEntity(
                        plantId = p1Id,
                        logType = "LIGHT_CHECK",
                        description = "Pengecekan cahaya ruangan (Optimal)",
                        recordedValue = "1.850 Lux",
                        timestamp = System.currentTimeMillis() - (2 * 24 * 60 * 60 * 1000L) + 100000L
                    )
                )
            }
        }
    }
}

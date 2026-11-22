package com.example.plantycare.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.plantycare.data.local.entity.PlantLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantLogDao {

    @Query("SELECT * FROM plant_logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<PlantLogEntity>>

    @Query("SELECT * FROM plant_logs WHERE plantId = :plantId ORDER BY timestamp DESC")
    fun getLogsByPlant(plantId: Long): Flow<List<PlantLogEntity>>

    @Query("SELECT COUNT(*) FROM plant_logs WHERE logType LIKE 'WATERING%'")
    fun getTotalWateringCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: PlantLogEntity): Long
}

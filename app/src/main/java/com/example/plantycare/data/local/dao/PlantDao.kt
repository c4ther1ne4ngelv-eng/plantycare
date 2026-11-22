package com.example.plantycare.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.plantycare.data.local.entity.PlantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDao {

    @Query("SELECT * FROM plants ORDER BY id DESC")
    fun getAllPlants(): Flow<List<PlantEntity>>

    @Query("SELECT * FROM plants WHERE id = :id")
    fun getPlantById(id: Long): Flow<PlantEntity?>

    @Query("SELECT COUNT(*) FROM plants")
    fun getPlantCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlant(plant: PlantEntity): Long

    @Update
    suspend fun updatePlant(plant: PlantEntity)

    @Delete
    suspend fun deletePlant(plant: PlantEntity)

    @Query("UPDATE plants SET lastWateredTimestamp = :timestamp WHERE id = :plantId")
    suspend fun updateLastWatered(plantId: Long, timestamp: Long)

    @Query("UPDATE plants SET photoUri = :photoUri WHERE id = :plantId")
    suspend fun updatePhoto(plantId: Long, photoUri: String)
}

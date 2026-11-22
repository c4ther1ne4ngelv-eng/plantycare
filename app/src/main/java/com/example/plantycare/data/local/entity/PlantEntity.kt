package com.example.plantycare.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plants")
data class PlantEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val species: String = "",
    val location: String,
    val minLux: Float,
    val maxLux: Float,
    val wateringIntervalDays: Int,
    val wateringIntervalHours: Int = 0,  // 0 = pakai days; >0 = interval per jam (untuk tanaman yang sering disiram)
    val lastWateredTimestamp: Long = System.currentTimeMillis(),
    val photoUri: String = "https://images.unsplash.com/photo-1614594975525-e45190c55d0b?w=600&auto=format&fit=crop&q=80"
)

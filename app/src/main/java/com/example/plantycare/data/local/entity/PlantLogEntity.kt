package com.example.plantycare.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "plant_logs",
    foreignKeys = [
        ForeignKey(
            entity = PlantEntity::class,
            parentColumns = ["id"],
            childColumns = ["plantId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["plantId"])]
)
data class PlantLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val plantId: Long,
    val logType: String, // "WATERING_SHAKE", "WATERING_MANUAL", "LIGHT_CHECK"
    val description: String,
    val recordedValue: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

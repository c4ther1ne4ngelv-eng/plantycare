package com.example.plantycare

import android.app.Application
import com.example.plantycare.data.local.PlantDatabase
import com.example.plantycare.data.repository.PlantRepository
import com.example.plantycare.data.repository.SensorRepository

class PlantyCareApp : Application() {

    val database: PlantDatabase by lazy {
        PlantDatabase.getDatabase(this)
    }

    val plantRepository: PlantRepository by lazy {
        PlantRepository(
            plantDao = database.plantDao(),
            plantLogDao = database.plantLogDao(),
            userProfileDao = database.userProfileDao()
        )
    }

    val sensorRepository: SensorRepository by lazy {
        SensorRepository(this)
    }
}

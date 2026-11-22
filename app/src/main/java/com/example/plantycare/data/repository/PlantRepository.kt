package com.example.plantycare.data.repository

import com.example.plantycare.data.local.dao.PlantDao
import com.example.plantycare.data.local.dao.PlantLogDao
import com.example.plantycare.data.local.dao.UserProfileDao
import com.example.plantycare.data.local.entity.PlantEntity
import com.example.plantycare.data.local.entity.PlantLogEntity
import com.example.plantycare.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class PlantRepository(
    private val plantDao: PlantDao,
    private val plantLogDao: PlantLogDao,
    private val userProfileDao: UserProfileDao
) {
    val allPlants: Flow<List<PlantEntity>> = plantDao.getAllPlants()
    val allLogs: Flow<List<PlantLogEntity>> = plantLogDao.getAllLogs()
    val plantCount: Flow<Int> = plantDao.getPlantCount()
    val totalWateringCount: Flow<Int> = plantLogDao.getTotalWateringCount()
    val userProfile: Flow<UserProfileEntity?> = userProfileDao.getUserProfile()

    fun getPlantById(id: Long): Flow<PlantEntity?> = plantDao.getPlantById(id)

    fun getLogsByPlant(plantId: Long): Flow<List<PlantLogEntity>> = plantLogDao.getLogsByPlant(plantId)

    suspend fun insertPlant(plant: PlantEntity): Long = plantDao.insertPlant(plant)

    suspend fun updatePlant(plant: PlantEntity) = plantDao.updatePlant(plant)

    suspend fun deletePlant(plant: PlantEntity) = plantDao.deletePlant(plant)

    suspend fun updatePlantPhoto(plantId: Long, photoUri: String) = plantDao.updatePhoto(plantId, photoUri)

    suspend fun logWatering(plantId: Long, plantName: String, isShake: Boolean) {
        val now = System.currentTimeMillis()
        plantDao.updateLastWatered(plantId, now)

        val logType = if (isShake) "WATERING_SHAKE" else "WATERING_MANUAL"
        val desc = if (isShake) {
            "Disiram dengan goyangan HP (Motion Sensor) untuk $plantName"
        } else {
            "Disiram secara manual untuk $plantName"
        }

        plantLogDao.insertLog(
            PlantLogEntity(
                plantId = plantId,
                logType = logType,
                description = desc,
                recordedValue = if (isShake) "Accelerometer Triggered" else "Manual Button",
                timestamp = now
            )
        )
    }

    suspend fun logLightMeasurement(plantId: Long, luxValue: Float, statusText: String) {
        plantLogDao.insertLog(
            PlantLogEntity(
                plantId = plantId,
                logType = "LIGHT_CHECK",
                description = "Pengecekan cahaya ruangan ($statusText)",
                recordedValue = "%.0f Lux".format(luxValue),
                timestamp = System.currentTimeMillis()
            )
        )
    }

    suspend fun updateUserProfile(profile: UserProfileEntity) {
        userProfileDao.insertOrUpdateProfile(profile)
    }

    suspend fun login(email: String, password: String):Boolean {
        val profile = userProfile.firstOrNull() ?: UserProfileEntity()
        // If profile matches or guest/first login
        val updated = profile.copy(
            email = if (profile.email.isBlank()) email else profile.email,
            isLoggedIn = true
        )
        userProfileDao.insertOrUpdateProfile(updated)
        return true
    }

    suspend fun register(displayName: String, username: String, email: String, password: String): Boolean {
        val newProfile = UserProfileEntity(
            id = 1,
            displayName = displayName,
            username = username,
            email = email,
            passwordHash = password,
            isLoggedIn = true
        )
        userProfileDao.insertOrUpdateProfile(newProfile)
        return true
    }

    suspend fun logout() {
        userProfileDao.setLoginStatus(false)
    }
}

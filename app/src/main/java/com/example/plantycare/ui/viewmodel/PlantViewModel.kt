package com.example.plantycare.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.plantycare.data.local.entity.PlantEntity
import com.example.plantycare.data.local.entity.PlantLogEntity
import com.example.plantycare.data.repository.PlantRepository
import com.example.plantycare.data.repository.SensorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlantViewModel(
    private val plantRepository: PlantRepository,
    private val sensorRepository: SensorRepository
) : ViewModel() {

    // Live Sensor Flow (Environment: Ambient Light)
    val liveLux: StateFlow<Float> = sensorRepository.lightFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1450f)

    // Motion Shake Sensor Event Flow
    private val _shakeEvent = MutableSharedFlow<Unit>()
    val shakeEvent: SharedFlow<Unit> = _shakeEvent.asSharedFlow()

    // Database Flows
    val plants: StateFlow<List<PlantEntity>> = plantRepository.allPlants
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val careLogs: StateFlow<List<PlantLogEntity>> = plantRepository.allLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage: SharedFlow<String> = _snackbarMessage.asSharedFlow()

    private var hasAlertVibratedThisSession = false

    init {
        // Listen to Accelerometer Shake events
        viewModelScope.launch {
            sensorRepository.shakeFlow.collect {
                _shakeEvent.emit(Unit)
            }
        }

        // Automatic Smart Vibration Alert: Check if plants are due for water
        viewModelScope.launch {
            plants.collect { plantList ->
                val needsWater = plantList.any { plant ->
                    if (plant.wateringIntervalHours > 0) {
                        val hoursSince = (System.currentTimeMillis() - plant.lastWateredTimestamp) / (60 * 60 * 1000L)
                        hoursSince >= plant.wateringIntervalHours
                    } else {
                        val daysSince = (System.currentTimeMillis() - plant.lastWateredTimestamp) / (24 * 60 * 60 * 1000L)
                        daysSince >= plant.wateringIntervalDays
                    }
                }
                if (needsWater && !hasAlertVibratedThisSession) {
                    hasAlertVibratedThisSession = true
                    sensorRepository.triggerWateringAlertVibration()
                }
            }
        }
    }

    // Trigger watering alert vibration pattern
    fun triggerWateringAlert() {
        sensorRepository.triggerWateringAlertVibration()
        viewModelScope.launch {
            _snackbarMessage.emit("📳 Pengingat Getar Aktif: Waktunya menyiram tanaman!")
        }
    }

    // Stable Flow: Fixed the infinite recomposition / flickering bug
    fun getPlant(plantId: Long): Flow<PlantEntity?> {
        return plantRepository.getPlantById(plantId)
    }

    fun waterPlant(plant: PlantEntity, isShake: Boolean) {
        viewModelScope.launch {
            plantRepository.logWatering(plant.id, plant.name, isShake)
            sensorRepository.triggerSuccessVibration()
            val msg = if (isShake) {
                "📳 Sensor Gerak: Berhasil menyiram ${plant.name}!"
            } else {
                "💧 ${plant.name} berhasil disiram!"
            }
            _snackbarMessage.emit(msg)
        }
    }

    fun saveLightLog(plantId: Long, luxValue: Float, statusText: String) {
        viewModelScope.launch {
            plantRepository.logLightMeasurement(plantId, luxValue, statusText)
            _snackbarMessage.emit("☀️ Nilai cahaya (${luxValue.toInt()} Lux) berhasil disimpan ke riwayat!")
        }
    }

    fun addPlant(
        name: String,
        location: String,
        minLux: Float,
        maxLux: Float,
        intervalDays: Int,
        intervalHours: Int = 0,
        photoUri: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val newPlant = PlantEntity(
                name = name.trim(),
                location = location.trim(),
                minLux = minLux,
                maxLux = maxLux,
                wateringIntervalDays = intervalDays,
                wateringIntervalHours = intervalHours,
                photoUri = photoUri
            )
            val newId = plantRepository.insertPlant(newPlant)
            plantRepository.logLightMeasurement(newId, liveLux.value, "Pengukuran Awal")
            _snackbarMessage.emit("🌿 Tanaman ${name.trim()} berhasil ditambahkan!")
            onSuccess()
        }
    }

    fun deletePlant(plant: PlantEntity, onComplete: () -> Unit) {
        viewModelScope.launch {
            plantRepository.deletePlant(plant)
            _snackbarMessage.emit("Tanaman ${plant.name} telah dihapus.")
            onComplete()
        }
    }

    fun updatePlantPhoto(plantId: Long, photoUri: String) {
        viewModelScope.launch {
            plantRepository.updatePlantPhoto(plantId, photoUri)
            _snackbarMessage.emit("📸 Foto tanaman berhasil diperbarui!")
        }
    }

    class Factory(
        private val plantRepository: PlantRepository,
        private val sensorRepository: SensorRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PlantViewModel(plantRepository, sensorRepository) as T
        }
    }
}

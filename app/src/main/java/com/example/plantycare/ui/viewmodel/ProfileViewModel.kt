package com.example.plantycare.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.plantycare.data.local.entity.UserProfileEntity
import com.example.plantycare.data.repository.PlantRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val plantRepository: PlantRepository
) : ViewModel() {

    val profile: StateFlow<UserProfileEntity?> = plantRepository.userProfile
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            UserProfileEntity()
        )

    val plantCount: StateFlow<Int> = plantRepository.plantCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val wateringCount: StateFlow<Int> = plantRepository.totalWateringCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val _uiEvent = MutableSharedFlow<String>()
    val uiEvent: SharedFlow<String> = _uiEvent.asSharedFlow()

    fun updateProfile(
        displayName: String,
        username: String,
        email: String,
        photoUri: String?
    ) {
        viewModelScope.launch {
            val current = profile.value ?: UserProfileEntity()
            val updated = current.copy(
                displayName = displayName.trim(),
                username = username.trim().removePrefix("@"),
                email = email.trim(),
                photoUri = photoUri ?: current.photoUri
            )
            plantRepository.updateUserProfile(updated)
            _uiEvent.emit("Profil berhasil diperbarui!")
        }
    }

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (email.isBlank() || password.isBlank()) {
                _uiEvent.emit("Email dan password tidak boleh kosong")
                return@launch
            }
            plantRepository.login(email, password)
            _uiEvent.emit("Selamat datang kembali!")
            onSuccess()
        }
    }

    fun register(displayName: String, username: String, email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (displayName.isBlank() || email.isBlank() || password.isBlank()) {
                _uiEvent.emit("Mohon lengkapi semua data pendaftaran")
                return@launch
            }
            plantRepository.register(displayName, username, email, password)
            _uiEvent.emit("Akun berhasil dibuat!")
            onSuccess()
        }
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            plantRepository.logout()
            _uiEvent.emit("Berhasil keluar")
            onSuccess()
        }
    }

    class Factory(
        private val plantRepository: PlantRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProfileViewModel(plantRepository) as T
        }
    }
}

package com.example.plantycare.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey
    val id: Int = 1,
    val displayName: String = "Nabil Ahmad",
    val username: String = "nabilgardener",
    val email: String = "nabil@plantycare.app",
    val passwordHash: String = "123456",
    val photoUri: String? = null,
    val gardenerLevel: String = "Level 3 Green Thumb",
    val isLoggedIn: Boolean = false
)

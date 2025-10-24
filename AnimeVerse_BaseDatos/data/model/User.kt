package com.example.animeverse.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val email: String,
    val password: String, // En producción debería estar hasheada
    val fullName: String,
    val avatar: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

// Modelo para mostrar en la UI (sin password)
data class UserDisplay(
    val id: Int,
    val username: String,
    val email: String,
    val fullName: String,
    val avatar: String?
)

// Función helper para convertir User a UserDisplay
fun User.toUserDisplay(): UserDisplay {
    return UserDisplay(
        id = id,
        username = username,
        email = email,
        fullName = fullName,
        avatar = avatar
    )
}



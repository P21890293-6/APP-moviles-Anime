package com.example.animeverse.data.local.user

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// @Entity declara una tabla SQLite para el sistema simple de usuarios
@Entity(
    tableName = "users",
    indices = [
        Index(value = ["email"], unique = true),
        Index(value = ["username"], unique = true)
    ]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val username: String,
    val email: String,
    val password: String,               // En producción debería estar hasheada
    val fullName: String,
    val phoneNumber: String? = null,    // Número de teléfono
    val avatar: String? = null,
    val role: String = "USER",          // Rol: USER o ADMIN
    val banned: Boolean = false,        // Si está baneado o no
    val createdAt: Long = System.currentTimeMillis()
)

// Modelo para mostrar en la UI (sin password)
data class UserDisplay(
    val id: Long,
    val username: String,
    val email: String,
    val fullName: String,
    val avatar: String?,
    val role: UserRole,
    val createdAt: Long
)

// Función helper para convertir UserEntity a UserDisplay
fun UserEntity.toUserDisplay(): UserDisplay {
    return UserDisplay(
        id = id,
        username = username,
        email = email,
        fullName = fullName,
        avatar = avatar,
        role = UserRole.fromString(role),
        createdAt = createdAt
    )
}

// Función helper para verificar si es administrador
fun UserEntity.isAdmin(): Boolean {
    return UserRole.fromString(role) == UserRole.ADMIN
}

// Función helper para verificar si es usuario público
fun UserEntity.isUser(): Boolean {
    return UserRole.fromString(role) == UserRole.USER
}


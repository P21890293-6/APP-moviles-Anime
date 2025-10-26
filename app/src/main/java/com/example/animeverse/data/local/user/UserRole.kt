package com.example.animeverse.data.local.user

/**
 * Enum que define los roles de usuario en AnimeVerse.
 */
enum class UserRole(val displayName: String) {
    USER("Usuario Público"),
    ADMIN("Administrador");
    
    companion object {
        fun fromString(value: String): UserRole {
            return when (value.uppercase()) {
                "ADMIN" -> ADMIN
                "USER" -> USER
                else -> USER // Por defecto es usuario público
            }
        }
    }
}


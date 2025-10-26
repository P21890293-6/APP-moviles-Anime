package com.example.animeverse.data.local.rol

import androidx.room.Entity
import androidx.room.PrimaryKey

// @Entity declara una tabla SQLite para roles de usuario
@Entity(tableName = "rol")
data class RolEntity(
    @PrimaryKey(autoGenerate = true)
    val id_rol: Int = 0,
    val nombre: String              // Administrador, Usuario, Moderador
)


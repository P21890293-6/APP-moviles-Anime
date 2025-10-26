package com.example.animeverse.data.local.estado

import androidx.room.Entity
import androidx.room.PrimaryKey

// @Entity declara una tabla SQLite para estados de usuario
@Entity(tableName = "estado")
data class EstadoEntity(
    @PrimaryKey(autoGenerate = true)
    val id_estado: Int = 0,
    val nombre: String              // Activo, Inactivo, Baneado
)


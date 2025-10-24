package com.example.animeverse.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rol")
data class RolEntity(
    @PrimaryKey(autoGenerate = true)
    val id_rol: Int = 0,
    val nombre: String
)

@Entity(tableName = "estado")
data class EstadoEntity(
    @PrimaryKey(autoGenerate = true)
    val id_estado: Int = 0,
    val nombre: String
)

@Entity(tableName = "tema")
data class TemaEntity(
    @PrimaryKey(autoGenerate = true)
    val id_tema: Int = 0,
    val nombre: String
)


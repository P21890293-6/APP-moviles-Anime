package com.example.animeverse.data.local.hora_baneo

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.animeverse.data.local.usuario.UsuarioEntity

// @Entity declara una tabla SQLite para historial de baneos temporales
@Entity(
    tableName = "hora_baneo",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["id_usuario"],
            childColumns = ["id_usuario"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["id_usuario"])
    ]
)
data class HoraBaneoEntity(
    @PrimaryKey(autoGenerate = true)
    val id_h_baneo: Int = 0,
    val fecha_baneo: String,
    val motivo: String,
    val cantidad: Int,                  // Horas de baneo
    val id_usuario: Int
)


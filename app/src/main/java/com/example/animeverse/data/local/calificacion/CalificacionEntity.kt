package com.example.animeverse.data.local.calificacion

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.animeverse.data.local.publicacion.PublicacionEntity
import com.example.animeverse.data.local.usuario.UsuarioEntity

// @Entity declara una tabla SQLite para calificaciones (1-5 estrellas)
@Entity(
    tableName = "calificacion",
    foreignKeys = [
        ForeignKey(
            entity = PublicacionEntity::class,
            parentColumns = ["id_publi"],
            childColumns = ["id_publi"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["id_usuario"],
            childColumns = ["id_usuario"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["id_publi"]),
        Index(value = ["id_usuario"])
    ]
)
data class CalificacionEntity(
    @PrimaryKey(autoGenerate = true)
    val id_calificacion: Int = 0,
    val fecha_calificacion: String,
    val calificacion: Int,              // 1-5 estrellas
    val id_publi: Int,
    val id_usuario: Int
)


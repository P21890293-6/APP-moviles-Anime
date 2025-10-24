package com.example.animeverse.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "publicacion",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["id_usuario"],
            childColumns = ["id_usuario"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TemaEntity::class,
            parentColumns = ["id_tema"],
            childColumns = ["id_tema"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["id_usuario"]),
        Index(value = ["id_tema"])
    ]
)
data class PublicacionEntity(
    @PrimaryKey(autoGenerate = true)
    val id_publi: Int = 0,
    val nombre: String,
    val fecha_registro: String,
    val descripcion: String,
    val foto_publicacion: String? = null,
    val fecha_baneo: String? = null,
    val motivo_baneo: String? = null,
    val id_usuario: Int,
    val id_tema: Int
)


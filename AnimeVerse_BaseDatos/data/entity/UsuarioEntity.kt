package com.example.animeverse.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "usuario",
    foreignKeys = [
        ForeignKey(
            entity = RolEntity::class,
            parentColumns = ["id_rol"],
            childColumns = ["id_rol"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EstadoEntity::class,
            parentColumns = ["id_estado"],
            childColumns = ["id_estado"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["id_rol"]),
        Index(value = ["id_estado"])
    ]
)
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id_usuario: Int = 0,
    val nombre: String,
    val correo: String,
    val clave: String,
    val nickname: String,
    val foto_perfil: String? = null,
    val id_rol: Int,
    val id_estado: Int
)


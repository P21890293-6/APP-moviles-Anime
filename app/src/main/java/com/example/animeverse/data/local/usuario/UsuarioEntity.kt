package com.example.animeverse.data.local.usuario

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.animeverse.data.local.rol.RolEntity
import com.example.animeverse.data.local.estado.EstadoEntity

// @Entity declara una tabla SQLite para usuarios del sistema completo
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
        Index(value = ["id_estado"]),
        Index(value = ["correo"], unique = true),
        Index(value = ["nickname"], unique = true)
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


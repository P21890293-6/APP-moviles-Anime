package com.example.animeverse.data.local.usuario

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// @Dao define operaciones para la tabla usuario
@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuario")
    fun getAllUsuarios(): Flow<List<UsuarioEntity>>

    @Query("SELECT * FROM usuario WHERE id_usuario = :id")
    suspend fun getUsuarioById(id: Int): UsuarioEntity?

    @Query("SELECT * FROM usuario WHERE correo = :correo LIMIT 1")
    suspend fun getUsuarioByEmail(correo: String): UsuarioEntity?

    @Query("SELECT * FROM usuario WHERE nickname = :nickname LIMIT 1")
    suspend fun getUsuarioByNickname(nickname: String): UsuarioEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUsuario(usuario: UsuarioEntity): Long

    @Update
    suspend fun updateUsuario(usuario: UsuarioEntity)

    @Delete
    suspend fun deleteUsuario(usuario: UsuarioEntity)
    
    @Query("SELECT COUNT(*) FROM usuario")
    suspend fun count(): Int
    
    @Query("SELECT * FROM usuario ORDER BY id_usuario ASC")
    suspend fun getAll(): List<UsuarioEntity>
}


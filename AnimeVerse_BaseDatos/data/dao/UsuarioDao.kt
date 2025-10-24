package com.example.animeverse.data.dao

import androidx.room.*
import com.example.animeverse.data.entity.UsuarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuario")
    fun getAllUsuarios(): Flow<List<UsuarioEntity>>

    @Query("SELECT * FROM usuario WHERE id_usuario = :id")
    suspend fun getUsuarioById(id: Int): UsuarioEntity?

    @Query("SELECT * FROM usuario WHERE correo = :correo")
    suspend fun getUsuarioByEmail(correo: String): UsuarioEntity?

    @Query("SELECT * FROM usuario WHERE nickname = :nickname")
    suspend fun getUsuarioByNickname(nickname: String): UsuarioEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsuario(usuario: UsuarioEntity): Long

    @Update
    suspend fun updateUsuario(usuario: UsuarioEntity)

    @Delete
    suspend fun deleteUsuario(usuario: UsuarioEntity)

    @Query("DELETE FROM usuario WHERE id_usuario = :id")
    suspend fun deleteUsuarioById(id: Int)
}


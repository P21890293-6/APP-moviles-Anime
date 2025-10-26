package com.example.animeverse.data.local.comentario

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// @Dao define operaciones para la tabla comentario
@Dao
interface ComentarioDao {
    @Query("SELECT * FROM comentario ORDER BY fecha_registro DESC")
    fun getAllComentarios(): Flow<List<ComentarioEntity>>

    @Query("SELECT * FROM comentario WHERE id_comentario = :id")
    suspend fun getComentarioById(id: Int): ComentarioEntity?

    @Query("SELECT * FROM comentario WHERE id_publi = :idPublicacion ORDER BY fecha_registro ASC")
    fun getComentariosByPublicacion(idPublicacion: Int): Flow<List<ComentarioEntity>>

    @Query("SELECT * FROM comentario WHERE id_usuario = :idUsuario ORDER BY fecha_registro DESC")
    fun getComentariosByUsuario(idUsuario: Int): Flow<List<ComentarioEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComentario(comentario: ComentarioEntity): Long

    @Update
    suspend fun updateComentario(comentario: ComentarioEntity)

    @Delete
    suspend fun deleteComentario(comentario: ComentarioEntity)
    
    @Query("SELECT COUNT(*) FROM comentario WHERE id_publi = :idPublicacion")
    suspend fun countByPublicacion(idPublicacion: Int): Int
}


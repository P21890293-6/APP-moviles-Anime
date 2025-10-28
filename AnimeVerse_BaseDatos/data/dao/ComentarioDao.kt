package com.example.animeverse.data.dao

import androidx.room.*
import com.example.animeverse.data.entity.ComentarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ComentarioDao {
    @Query("SELECT * FROM comentario")
    fun getAllComentarios(): Flow<List<ComentarioEntity>>

    @Query("SELECT * FROM comentario WHERE id_comentario = :id")
    suspend fun getComentarioById(id: Int): ComentarioEntity?

    @Query("SELECT * FROM comentario WHERE id_publi = :idPublicacion")
    fun getComentariosByPublicacion(idPublicacion: Int): Flow<List<ComentarioEntity>>

    @Query("SELECT * FROM comentario WHERE id_usuario = :idUsuario")
    fun getComentariosByUsuario(idUsuario: Int): Flow<List<ComentarioEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComentario(comentario: ComentarioEntity): Long

    @Update
    suspend fun updateComentario(comentario: ComentarioEntity)

    @Delete
    suspend fun deleteComentario(comentario: ComentarioEntity)

    @Query("DELETE FROM comentario WHERE id_comentario = :id")
    suspend fun deleteComentarioById(id: Int)
}


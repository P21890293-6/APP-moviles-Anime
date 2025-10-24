package com.example.animeverse.data.dao

import androidx.room.*
import com.example.animeverse.data.entity.PublicacionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PublicacionDao {
    @Query("SELECT * FROM publicacion")
    fun getAllPublicaciones(): Flow<List<PublicacionEntity>>

    @Query("SELECT * FROM publicacion WHERE id_publi = :id")
    suspend fun getPublicacionById(id: Int): PublicacionEntity?

    @Query("SELECT * FROM publicacion WHERE id_usuario = :idUsuario")
    fun getPublicacionesByUsuario(idUsuario: Int): Flow<List<PublicacionEntity>>

    @Query("SELECT * FROM publicacion WHERE id_tema = :idTema")
    fun getPublicacionesByTema(idTema: Int): Flow<List<PublicacionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPublicacion(publicacion: PublicacionEntity): Long

    @Update
    suspend fun updatePublicacion(publicacion: PublicacionEntity)

    @Delete
    suspend fun deletePublicacion(publicacion: PublicacionEntity)

    @Query("DELETE FROM publicacion WHERE id_publi = :id")
    suspend fun deletePublicacionById(id: Int)
}


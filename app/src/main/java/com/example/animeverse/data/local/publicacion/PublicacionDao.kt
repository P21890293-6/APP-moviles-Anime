package com.example.animeverse.data.local.publicacion

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// @Dao define operaciones para la tabla publicacion
@Dao
interface PublicacionDao {
    @Query("SELECT * FROM publicacion ORDER BY fecha_registro DESC")
    fun getAllPublicaciones(): Flow<List<PublicacionEntity>>

    @Query("SELECT * FROM publicacion WHERE id_publi = :id")
    suspend fun getPublicacionById(id: Int): PublicacionEntity?

    @Query("SELECT * FROM publicacion WHERE id_usuario = :idUsuario ORDER BY fecha_registro DESC")
    fun getPublicacionesByUsuario(idUsuario: Int): Flow<List<PublicacionEntity>>

    @Query("SELECT * FROM publicacion WHERE id_tema = :idTema ORDER BY fecha_registro DESC")
    fun getPublicacionesByTema(idTema: Int): Flow<List<PublicacionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPublicacion(publicacion: PublicacionEntity): Long

    @Update
    suspend fun updatePublicacion(publicacion: PublicacionEntity)

    @Delete
    suspend fun deletePublicacion(publicacion: PublicacionEntity)
    
    @Query("SELECT COUNT(*) FROM publicacion")
    suspend fun count(): Int
}


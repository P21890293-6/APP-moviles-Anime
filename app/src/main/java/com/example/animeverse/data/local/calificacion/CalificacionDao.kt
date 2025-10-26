package com.example.animeverse.data.local.calificacion

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// @Dao define operaciones para la tabla calificacion
@Dao
interface CalificacionDao {
    @Query("SELECT * FROM calificacion")
    fun getAllCalificaciones(): Flow<List<CalificacionEntity>>

    @Query("SELECT * FROM calificacion WHERE id_calificacion = :id")
    suspend fun getCalificacionById(id: Int): CalificacionEntity?

    @Query("SELECT * FROM calificacion WHERE id_publi = :idPublicacion")
    fun getCalificacionesByPublicacion(idPublicacion: Int): Flow<List<CalificacionEntity>>

    @Query("SELECT * FROM calificacion WHERE id_usuario = :idUsuario")
    fun getCalificacionesByUsuario(idUsuario: Int): Flow<List<CalificacionEntity>>

    @Query("SELECT AVG(calificacion) FROM calificacion WHERE id_publi = :idPublicacion")
    suspend fun getPromedioCalificacion(idPublicacion: Int): Double?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalificacion(calificacion: CalificacionEntity): Long

    @Update
    suspend fun updateCalificacion(calificacion: CalificacionEntity)

    @Delete
    suspend fun deleteCalificacion(calificacion: CalificacionEntity)
}


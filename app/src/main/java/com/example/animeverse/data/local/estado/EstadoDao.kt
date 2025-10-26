package com.example.animeverse.data.local.estado

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// @Dao define operaciones para la tabla estado
@Dao
interface EstadoDao {
    @Query("SELECT * FROM estado")
    fun getAllEstados(): Flow<List<EstadoEntity>>

    @Query("SELECT * FROM estado WHERE id_estado = :id")
    suspend fun getEstadoById(id: Int): EstadoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEstado(estado: EstadoEntity): Long

    @Update
    suspend fun updateEstado(estado: EstadoEntity)

    @Delete
    suspend fun deleteEstado(estado: EstadoEntity)
    
    @Query("SELECT COUNT(*) FROM estado")
    suspend fun count(): Int
}


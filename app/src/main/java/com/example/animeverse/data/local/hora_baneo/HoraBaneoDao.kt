package com.example.animeverse.data.local.hora_baneo

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// @Dao define operaciones para la tabla hora_baneo
@Dao
interface HoraBaneoDao {
    @Query("SELECT * FROM hora_baneo ORDER BY fecha_baneo DESC")
    fun getAllHoraBaneos(): Flow<List<HoraBaneoEntity>>

    @Query("SELECT * FROM hora_baneo WHERE id_h_baneo = :id")
    suspend fun getHoraBaneoById(id: Int): HoraBaneoEntity?

    @Query("SELECT * FROM hora_baneo WHERE id_usuario = :idUsuario ORDER BY fecha_baneo DESC")
    fun getHoraBaneosByUsuario(idUsuario: Int): Flow<List<HoraBaneoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHoraBaneo(horaBaneo: HoraBaneoEntity): Long

    @Update
    suspend fun updateHoraBaneo(horaBaneo: HoraBaneoEntity)

    @Delete
    suspend fun deleteHoraBaneo(horaBaneo: HoraBaneoEntity)
}


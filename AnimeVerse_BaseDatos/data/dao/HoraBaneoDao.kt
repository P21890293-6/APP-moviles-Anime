package com.example.animeverse.data.dao

import androidx.room.*
import com.example.animeverse.data.entity.HoraBaneoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HoraBaneoDao {
    @Query("SELECT * FROM hora_baneo")
    fun getAllHoraBaneos(): Flow<List<HoraBaneoEntity>>

    @Query("SELECT * FROM hora_baneo WHERE id_h_baneo = :id")
    suspend fun getHoraBaneoById(id: Int): HoraBaneoEntity?

    @Query("SELECT * FROM hora_baneo WHERE id_usuario = :idUsuario")
    fun getHoraBaneosByUsuario(idUsuario: Int): Flow<List<HoraBaneoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHoraBaneo(horaBaneo: HoraBaneoEntity): Long

    @Update
    suspend fun updateHoraBaneo(horaBaneo: HoraBaneoEntity)

    @Delete
    suspend fun deleteHoraBaneo(horaBaneo: HoraBaneoEntity)

    @Query("DELETE FROM hora_baneo WHERE id_h_baneo = :id")
    suspend fun deleteHoraBaneoById(id: Int)
}


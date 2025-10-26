package com.example.animeverse.data.local.tema

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// @Dao define operaciones para la tabla tema
@Dao
interface TemaDao {
    @Query("SELECT * FROM tema")
    fun getAllTemas(): Flow<List<TemaEntity>>

    @Query("SELECT * FROM tema WHERE id_tema = :id")
    suspend fun getTemaById(id: Int): TemaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTema(tema: TemaEntity): Long

    @Update
    suspend fun updateTema(tema: TemaEntity)

    @Delete
    suspend fun deleteTema(tema: TemaEntity)
    
    @Query("SELECT COUNT(*) FROM tema")
    suspend fun count(): Int
}


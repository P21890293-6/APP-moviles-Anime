package com.example.animeverse.data.dao

import androidx.room.*
import com.example.animeverse.data.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RolDao {
    @Query("SELECT * FROM rol")
    fun getAllRoles(): Flow<List<RolEntity>>

    @Query("SELECT * FROM rol WHERE id_rol = :id")
    suspend fun getRolById(id: Int): RolEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRol(rol: RolEntity): Long

    @Update
    suspend fun updateRol(rol: RolEntity)

    @Delete
    suspend fun deleteRol(rol: RolEntity)

    @Query("DELETE FROM rol WHERE id_rol = :id")
    suspend fun deleteRolById(id: Int)
}

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

    @Query("DELETE FROM estado WHERE id_estado = :id")
    suspend fun deleteEstadoById(id: Int)
}

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

    @Query("DELETE FROM tema WHERE id_tema = :id")
    suspend fun deleteTemaById(id: Int)
}








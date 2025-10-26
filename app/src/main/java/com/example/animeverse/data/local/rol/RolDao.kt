package com.example.animeverse.data.local.rol

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// @Dao define operaciones para la tabla rol
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
    
    @Query("SELECT COUNT(*) FROM rol")
    suspend fun count(): Int
}


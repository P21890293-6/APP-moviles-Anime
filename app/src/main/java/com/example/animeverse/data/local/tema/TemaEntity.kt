package com.example.animeverse.data.local.tema

import androidx.room.Entity
import androidx.room.PrimaryKey

// @Entity declara una tabla SQLite para temas/categorías
@Entity(tableName = "tema")
data class TemaEntity(
    @PrimaryKey(autoGenerate = true)
    val id_tema: Int = 0,
    val nombre: String              // Anime, Manga, Gaming, General
)


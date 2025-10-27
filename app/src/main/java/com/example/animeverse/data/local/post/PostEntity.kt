package com.example.animeverse.data.local.post

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

// @Entity declara una tabla SQLite para el sistema simple de publicaciones
@Entity(
    tableName = "posts",
    indices = [
        Index(value = ["authorId"]),
        Index(value = ["themeId"])
    ]
)
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val content: String,
    val authorId: Long,
    val authorName: String,
    val themeId: Int,                   // 1: Anime, 2: Manga, 3: Gaming, 4: General
    val imageUri: String? = null,       // URI de la imagen adjunta (opcional)
    val createdAt: Long = System.currentTimeMillis(),
    val likes: Int = 0,
    val comments: Int = 0
)

// Modelo para mostrar en la UI
data class PostDisplay(
    val id: Long,
    val title: String,
    val content: String,
    val authorName: String,
    val themeName: String,
    val createdAt: String,
    val likes: Int,
    val comments: Int
)

// Función helper para convertir PostEntity a PostDisplay
fun PostEntity.toPostDisplay(): PostDisplay {
    val themeName = when (themeId) {
        1 -> "Anime"
        2 -> "Manga"
        3 -> "Gaming"
        4 -> "General"
        else -> "Otro"
    }
    
    val date = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        .format(Date(createdAt))
    
    return PostDisplay(
        id = id,
        title = title,
        content = content,
        authorName = authorName,
        themeName = themeName,
        createdAt = date,
        likes = likes,
        comments = comments
    )
}


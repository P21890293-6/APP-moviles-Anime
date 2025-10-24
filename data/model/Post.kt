package com.example.animeverse.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val content: String,
    val authorId: Int,
    val authorName: String,
    val themeId: Int, // 1: Anime, 2: Manga, 3: Gaming
    val createdAt: Long = System.currentTimeMillis(),
    val likes: Int = 0,
    val comments: Int = 0
)

// Modelo para mostrar en la UI
data class PostDisplay(
    val id: Int,
    val title: String,
    val content: String,
    val authorName: String,
    val themeName: String,
    val createdAt: String,
    val likes: Int,
    val comments: Int
)

// Función helper para convertir Post a PostDisplay
fun Post.toPostDisplay(): PostDisplay {
    val themeName = when (themeId) {
        1 -> "Anime"
        2 -> "Manga" 
        3 -> "Gaming"
        else -> "Otro"
    }
    
    val date = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
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



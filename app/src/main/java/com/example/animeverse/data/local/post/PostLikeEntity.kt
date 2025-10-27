package com.example.animeverse.data.local.post

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Tabla para trackear qué usuario dio like a qué post.
 * Permite implementar "un like por usuario".
 */
@Entity(
    tableName = "post_likes",
    indices = [
        Index(value = ["postId", "userId"], unique = true)
    ]
)
data class PostLikeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val postId: Long,
    val userId: Long,
    val likedAt: Long = System.currentTimeMillis()
)



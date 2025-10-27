package com.example.animeverse.data.local.post

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Tabla para reportes de publicaciones.
 */
@Entity(
    tableName = "post_reports",
    indices = [
        Index(value = ["postId"]),
        Index(value = ["reportedBy"])
    ]
)
data class PostReportEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val postId: Long,
    val postTitle: String,
    val postAuthorName: String,
    val reportedBy: Long,
    val reportedByName: String,
    val reason: String = "Contenido inapropiado",
    val reportedAt: Long = System.currentTimeMillis(),
    val status: String = "PENDING" // PENDING, REVIEWED, DISMISSED
)



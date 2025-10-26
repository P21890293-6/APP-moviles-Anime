package com.example.animeverse.data.local.post

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// @Dao define operaciones para la tabla posts (sistema simple)
@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY createdAt DESC")
    fun getAllPosts(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE id = :id LIMIT 1")
    suspend fun getPostById(id: Long): PostEntity?

    @Query("SELECT * FROM posts WHERE authorId = :authorId ORDER BY createdAt DESC")
    fun getPostsByAuthor(authorId: Long): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE themeId = :themeId ORDER BY createdAt DESC")
    fun getPostsByTheme(themeId: Int): Flow<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity): Long

    @Update
    suspend fun updatePost(post: PostEntity)

    @Delete
    suspend fun deletePost(post: PostEntity)
    
    @Query("DELETE FROM posts WHERE id = :postId")
    suspend fun deletePostById(postId: Long)
    
    @Query("SELECT * FROM posts ORDER BY createdAt DESC")
    suspend fun getAll(): List<PostEntity>

    @Query("SELECT COUNT(*) FROM posts")
    suspend fun count(): Int

    @Query("UPDATE posts SET likes = likes + 1 WHERE id = :id")
    suspend fun incrementLikes(id: Long)
    
    @Query("UPDATE posts SET likes = likes - 1 WHERE id = :id AND likes > 0")
    suspend fun decrementLikes(id: Long)
    
    // Métodos para manejar likes por usuario
    @Insert
    suspend fun insertLike(like: PostLikeEntity)
    
    @Query("DELETE FROM post_likes WHERE postId = :postId AND userId = :userId")
    suspend fun deleteLike(postId: Long, userId: Long)
    
    @Query("SELECT EXISTS(SELECT 1 FROM post_likes WHERE postId = :postId AND userId = :userId)")
    suspend fun hasUserLiked(postId: Long, userId: Long): Boolean
    
    @Query("SELECT COUNT(*) FROM post_likes WHERE postId = :postId")
    suspend fun getLikeCount(postId: Long): Int

    @Query("UPDATE posts SET comments = comments + 1 WHERE id = :id")
    suspend fun incrementComments(id: Long)
    
    // Métodos para reportes
    @Insert
    suspend fun insertReport(report: PostReportEntity): Long
    
    @Query("SELECT * FROM post_reports WHERE status = 'PENDING' ORDER BY reportedAt DESC")
    suspend fun getAllPendingReports(): List<PostReportEntity>
    
    @Query("SELECT * FROM post_reports ORDER BY reportedAt DESC")
    suspend fun getAllReports(): List<PostReportEntity>
    
    @Query("UPDATE post_reports SET status = :status WHERE id = :reportId")
    suspend fun updateReportStatus(reportId: Long, status: String)
    
    @Query("DELETE FROM post_reports WHERE id = :reportId")
    suspend fun deleteReport(reportId: Long)
    
    @Query("SELECT COUNT(*) FROM post_reports WHERE status = 'PENDING'")
    suspend fun getPendingReportsCount(): Int
}


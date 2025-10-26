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

    @Query("SELECT COUNT(*) FROM posts")
    suspend fun count(): Int

    @Query("UPDATE posts SET likes = likes + 1 WHERE id = :id")
    suspend fun incrementLikes(id: Long)

    @Query("UPDATE posts SET comments = comments + 1 WHERE id = :id")
    suspend fun incrementComments(id: Long)
}


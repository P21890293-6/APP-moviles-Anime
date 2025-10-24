package com.example.animeverse.data.dao

import androidx.room.*
import com.example.animeverse.data.model.Post
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Query("SELECT * FROM posts")
    fun getAllPosts(): Flow<List<Post>>

    @Query("SELECT * FROM posts WHERE id = :id")
    suspend fun getPostById(id: Int): Post?

    @Query("SELECT * FROM posts WHERE authorId = :authorId")
    fun getPostsByAuthor(authorId: Int): Flow<List<Post>>

    @Query("SELECT * FROM posts WHERE themeId = :themeId")
    fun getPostsByTheme(themeId: Int): Flow<List<Post>>

    @Query("SELECT * FROM posts ORDER BY createdAt DESC")
    fun getAllPostsOrderedByDate(): Flow<List<Post>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: Post): Long

    @Update
    suspend fun updatePost(post: Post)

    @Delete
    suspend fun deletePost(post: Post)

    @Query("DELETE FROM posts WHERE id = :id")
    suspend fun deletePostById(id: Int)

    @Query("SELECT COUNT(*) FROM posts")
    suspend fun getPostCount(): Int

    @Query("UPDATE posts SET likes = likes + 1 WHERE id = :id")
    suspend fun incrementLikes(id: Int)

    @Query("UPDATE posts SET comments = comments + 1 WHERE id = :id")
    suspend fun incrementComments(id: Int)
}

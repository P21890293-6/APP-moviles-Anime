package com.example.animeverse.data.mock

import com.example.animeverse.data.local.user.UserEntity
import com.example.animeverse.data.local.post.PostEntity

/**
 * Objeto con datos de ejemplo para testing y desarrollo.
 * Útil para poblar la UI durante el desarrollo sin necesidad de crear datos manualmente.
 */
object MockData {
    
    // Usuarios de ejemplo para el sistema simple
    val mockUsers = listOf(
        UserEntity(
            id = 1,
            username = "anime_lover",
            email = "anime@example.com",
            password = "Maria@123",
            fullName = "María García",
            phoneNumber = "87654321",
            avatar = null
        ),
        UserEntity(
            id = 2,
            username = "manga_reader",
            email = "manga@example.com", 
            password = "Carlos@123",
            fullName = "Carlos López",
            phoneNumber = "11223344",
            avatar = null
        ),
        UserEntity(
            id = 3,
            username = "gamer_pro",
            email = "gamer@example.com",
            password = "Ana@123", 
            fullName = "Ana Rodríguez",
            phoneNumber = "55667788",
            avatar = null
        )
    )
    
    // Publicaciones de ejemplo para el sistema simple
    val mockPosts = listOf(
        PostEntity(
            id = 1,
            title = "¿Cuál es tu anime favorito de 2024?",
            content = "Hola a todos! Quería saber cuáles han sido sus animes favoritos de este año. Personalmente me encantó Jujutsu Kaisen y Attack on Titan. ¿Qué opinan ustedes?",
            authorId = 1,
            authorName = "María García",
            themeId = 1, // Anime
            likes = 15,
            comments = 8
        ),
        PostEntity(
            id = 2,
            title = "Recomendaciones de manga para principiantes",
            content = "Estoy empezando a leer manga y me gustaría algunas recomendaciones. He leído Death Note y me encantó. ¿Qué otros mangas me recomiendan?",
            authorId = 2,
            authorName = "Carlos López", 
            themeId = 2, // Manga
            likes = 12,
            comments = 5
        ),
        PostEntity(
            id = 3,
            title = "Mejores juegos de anime para PC",
            content = "¿Cuáles son los mejores juegos basados en anime que han jugado? He probado Dragon Ball FighterZ y está increíble. ¿Alguna otra recomendación?",
            authorId = 3,
            authorName = "Ana Rodríguez",
            themeId = 3, // Gaming
            likes = 20,
            comments = 12
        ),
        PostEntity(
            id = 4,
            title = "One Piece: ¿Vale la pena verlo completo?",
            content = "Siempre he querido ver One Piece pero me da miedo que sean tantos episodios. ¿Realmente vale la pena invertir tanto tiempo?",
            authorId = 1,
            authorName = "María García",
            themeId = 1, // Anime
            likes = 25,
            comments = 15
        ),
        PostEntity(
            id = 5,
            title = "Nuevos lanzamientos de manga en 2024",
            content = "¿Cuáles han sido los mejores lanzamientos de manga este año? He estado un poco desconectado y me gustaría ponerme al día.",
            authorId = 2,
            authorName = "Carlos López",
            themeId = 2, // Manga
            likes = 8,
            comments = 3
        )
    )
    
    // IDs de temas para referencia
    object ThemeIds {
        const val ANIME = 1
        const val MANGA = 2
        const val GAMING = 3
        const val GENERAL = 4
    }
    
    // Nombres de temas
    val themeNames = mapOf(
        1 to "Anime",
        2 to "Manga",
        3 to "Gaming",
        4 to "General"
    )
}


package com.example.animeverse.data.mock

import com.example.animeverse.data.model.Post
import com.example.animeverse.data.model.User

object MockData {
    
    // Usuarios de ejemplo
    val mockUsers = listOf(
        User(
            id = 1,
            username = "anime_lover",
            email = "anime@example.com",
            password = "123456",
            fullName = "María García",
            avatar = null
        ),
        User(
            id = 2,
            username = "manga_reader",
            email = "manga@example.com", 
            password = "123456",
            fullName = "Carlos López",
            avatar = null
        ),
        User(
            id = 3,
            username = "gamer_pro",
            email = "gamer@example.com",
            password = "123456", 
            fullName = "Ana Rodríguez",
            avatar = null
        )
    )
    
    // Publicaciones de ejemplo
    val mockPosts = listOf(
        Post(
            id = 1,
            title = "¿Cuál es tu anime favorito de 2024?",
            content = "Hola a todos! Quería saber cuáles han sido sus animes favoritos de este año. Personalmente me encantó Jujutsu Kaisen y Attack on Titan. ¿Qué opinan ustedes?",
            authorId = 1,
            authorName = "María García",
            themeId = 1, // Anime
            createdAt = System.currentTimeMillis() - 3600000, // 1 hora atrás
            likes = 15,
            comments = 8
        ),
        Post(
            id = 2,
            title = "Recomendaciones de manga para principiantes",
            content = "Estoy empezando a leer manga y me gustaría algunas recomendaciones. He leído Death Note y me encantó. ¿Qué otros mangas me recomiendan?",
            authorId = 2,
            authorName = "Carlos López", 
            themeId = 2, // Manga
            createdAt = System.currentTimeMillis() - 7200000, // 2 horas atrás
            likes = 12,
            comments = 5
        ),
        Post(
            id = 3,
            title = "Mejores juegos de anime para PC",
            content = "¿Cuáles son los mejores juegos basados en anime que han jugado? He probado Dragon Ball FighterZ y está increíble. ¿Alguna otra recomendación?",
            authorId = 3,
            authorName = "Ana Rodríguez",
            themeId = 3, // Gaming
            createdAt = System.currentTimeMillis() - 10800000, // 3 horas atrás
            likes = 20,
            comments = 12
        ),
        Post(
            id = 4,
            title = "One Piece: ¿Vale la pena verlo completo?",
            content = "Siempre he querido ver One Piece pero me da miedo que sean tantos episodios. ¿Realmente vale la pena invertir tanto tiempo? ¿Por dónde debería empezar?",
            authorId = 1,
            authorName = "María García",
            themeId = 1, // Anime
            createdAt = System.currentTimeMillis() - 14400000, // 4 horas atrás
            likes = 25,
            comments = 15
        ),
        Post(
            id = 5,
            title = "Nuevos lanzamientos de manga en 2024",
            content = "¿Cuáles han sido los mejores lanzamientos de manga este año? He estado un poco desconectado y me gustaría ponerme al día con las novedades.",
            authorId = 2,
            authorName = "Carlos López",
            themeId = 2, // Manga
            createdAt = System.currentTimeMillis() - 18000000, // 5 horas atrás
            likes = 8,
            comments = 3
        )
    )
}

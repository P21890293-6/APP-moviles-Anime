package com.example.animeverse.data.repository

import com.example.animeverse.data.database.AppDatabase

// Factory para crear el repositorio con inyección de dependencias
object AnimeVerseRepositoryFactory {
    
    fun create(database: AppDatabase): AnimeVerseRepository {
        return AnimeVerseRepository(
            rolDao = database.rolDao(),
            estadoDao = database.estadoDao(),
            usuarioDao = database.usuarioDao(),
            temaDao = database.temaDao(),
            publicacionDao = database.publicacionDao(),
            comentarioDao = database.comentarioDao(),
            calificacionDao = database.calificacionDao(),
            horaBaneoDao = database.horaBaneoDao(),
            userDao = database.userDao(),
            postDao = database.postDao()
        )
    }
}


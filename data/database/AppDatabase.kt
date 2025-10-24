package com.example.animeverse.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.animeverse.data.dao.*
import com.example.animeverse.data.entity.*
import com.example.animeverse.data.model.User
import com.example.animeverse.data.model.Post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Database(
    entities = [
        RolEntity::class,
        EstadoEntity::class,
        UsuarioEntity::class,
        TemaEntity::class,
        PublicacionEntity::class,
        ComentarioEntity::class,
        CalificacionEntity::class,
        HoraBaneoEntity::class,
        User::class,
        Post::class
    ],
    version = 3,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    
    // Exponemos todos los DAOs
    abstract fun rolDao(): RolDao
    abstract fun estadoDao(): EstadoDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun temaDao(): TemaDao
    abstract fun publicacionDao(): PublicacionDao
    abstract fun comentarioDao(): ComentarioDao
    abstract fun calificacionDao(): CalificacionDao
    abstract fun horaBaneoDao(): HoraBaneoDao
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private const val DB_NAME = "animeverse.db"

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                                val currentDate = dateFormat.format(Date())
                                
                                val rolDao = getInstance(context).rolDao()
                                val estadoDao = getInstance(context).estadoDao()
                                val usuarioDao = getInstance(context).usuarioDao()
                                val temaDao = getInstance(context).temaDao()
                                val publicacionDao = getInstance(context).publicacionDao()
                                val comentarioDao = getInstance(context).comentarioDao()
                                val calificacionDao = getInstance(context).calificacionDao()
                                val horaBaneoDao = getInstance(context).horaBaneoDao()
                                val userDao = getInstance(context).userDao()
                                val postDao = getInstance(context).postDao()

                                // Insertar datos de ejemplo
                                // Insertar roles
                                    val rolAdmin = RolEntity(nombre = "Administrador")
                                    val rolUsuario = RolEntity(nombre = "Usuario")
                                    val rolModerador = RolEntity(nombre = "Moderador")
                                    
                                    val idRolAdmin = rolDao.insertRol(rolAdmin)
                                    val idRolUsuario = rolDao.insertRol(rolUsuario)
                                    val idRolModerador = rolDao.insertRol(rolModerador)
                                    
                                    // Insertar estados
                                    val estadoActivo = EstadoEntity(nombre = "Activo")
                                    val estadoInactivo = EstadoEntity(nombre = "Inactivo")
                                    val estadoBaneado = EstadoEntity(nombre = "Baneado")
                                    
                                    val idEstadoActivo = estadoDao.insertEstado(estadoActivo)
                                    val idEstadoInactivo = estadoDao.insertEstado(estadoInactivo)
                                    val idEstadoBaneado = estadoDao.insertEstado(estadoBaneado)
                                    
                                    // Insertar temas
                                    val temaAnime = TemaEntity(nombre = "Anime")
                                    val temaManga = TemaEntity(nombre = "Manga")
                                    val temaGaming = TemaEntity(nombre = "Gaming")
                                    val temaGeneral = TemaEntity(nombre = "General")
                                    
                                    val idTemaAnime = temaDao.insertTema(temaAnime)
                                    val idTemaManga = temaDao.insertTema(temaManga)
                                    val idTemaGaming = temaDao.insertTema(temaGaming)
                                    val idTemaGeneral = temaDao.insertTema(temaGeneral)
                                    
                                    // Insertar usuarios
                                    val usuario1 = UsuarioEntity(
                                        nombre = "Juan Pérez",
                                        correo = "juan@example.com",
                                        clave = "password123",
                                        nickname = "JuanAnime",
                                        foto_perfil = null,
                                        id_rol = idRolUsuario.toInt(),
                                        id_estado = idEstadoActivo.toInt()
                                    )
                                    
                                    val usuario2 = UsuarioEntity(
                                        nombre = "María García",
                                        correo = "maria@example.com",
                                        clave = "password456",
                                        nickname = "MariaManga",
                                        foto_perfil = null,
                                        id_rol = idRolModerador.toInt(),
                                        id_estado = idEstadoActivo.toInt()
                                    )
                                    
                                    val idUsuario1 = usuarioDao.insertUsuario(usuario1)
                                    val idUsuario2 = usuarioDao.insertUsuario(usuario2)
                                    
                                    // Insertar publicaciones
                                    val publicacion1 = PublicacionEntity(
                                        nombre = "Mi anime favorito: Attack on Titan",
                                        fecha_registro = currentDate,
                                        descripcion = "Attack on Titan es una obra maestra del anime que combina acción, drama y filosofía de manera excepcional.",
                                        foto_publicacion = null,
                                        fecha_baneo = null,
                                        motivo_baneo = null,
                                        id_usuario = idUsuario1.toInt(),
                                        id_tema = idTemaAnime.toInt()
                                    )
                                    
                                    val publicacion2 = PublicacionEntity(
                                        nombre = "Recomendaciones de Manga",
                                        fecha_registro = currentDate,
                                        descripcion = "Aquí comparto mis mangas favoritos que todo fan debería leer.",
                                        foto_publicacion = null,
                                        fecha_baneo = null,
                                        motivo_baneo = null,
                                        id_usuario = idUsuario2.toInt(),
                                        id_tema = idTemaManga.toInt()
                                    )
                                    
                                    val idPublicacion1 = publicacionDao.insertPublicacion(publicacion1)
                                    val idPublicacion2 = publicacionDao.insertPublicacion(publicacion2)
                                    
                                    // Insertar comentarios
                                    val comentario1 = ComentarioEntity(
                                        fecha_registro = currentDate,
                                        comentario = "¡Excelente recomendación! Attack on Titan es realmente increíble.",
                                        fecha_baneo = null,
                                        motivo_baneo = null,
                                        id_publi = idPublicacion1.toInt(),
                                        id_usuario = idUsuario2.toInt()
                                    )
                                    
                                    val comentario2 = ComentarioEntity(
                                        fecha_registro = currentDate,
                                        comentario = "Gracias por compartir estas recomendaciones de manga.",
                                        fecha_baneo = null,
                                        motivo_baneo = null,
                                        id_publi = idPublicacion2.toInt(),
                                        id_usuario = idUsuario1.toInt()
                                    )
                                    
                                    comentarioDao.insertComentario(comentario1)
                                    comentarioDao.insertComentario(comentario2)
                                    
                                    // Insertar calificaciones
                                    val calificacion1 = CalificacionEntity(
                                        fecha_calificacion = currentDate,
                                        calificacion = 5,
                                        id_publi = idPublicacion1.toInt(),
                                        id_usuario = idUsuario2.toInt()
                                    )
                                    
                                    val calificacion2 = CalificacionEntity(
                                        fecha_calificacion = currentDate,
                                        calificacion = 4,
                                        id_publi = idPublicacion2.toInt(),
                                        id_usuario = idUsuario1.toInt()
                                    )
                                    
                                    calificacionDao.insertCalificacion(calificacion1)
                                    calificacionDao.insertCalificacion(calificacion2)
                                    
                                    // Insertar usuarios del modelo User (datos de ejemplo de MockData.kt)
                                    val user1 = User(
                                        username = "anime_lover",
                                        email = "anime@example.com",
                                        password = "123456",
                                        fullName = "María García",
                                        avatar = null
                                    )
                                    
                                    val user2 = User(
                                        username = "manga_reader",
                                        email = "manga@example.com",
                                        password = "123456",
                                        fullName = "Carlos López",
                                        avatar = null
                                    )
                                    
                                    val user3 = User(
                                        username = "gamer_pro",
                                        email = "gamer@example.com",
                                        password = "123456",
                                        fullName = "Ana Rodríguez",
                                        avatar = null
                                    )
                                    
                                    val userId1 = userDao.insertUser(user1)
                                    val userId2 = userDao.insertUser(user2)
                                    val userId3 = userDao.insertUser(user3)
                                    
                                    // Insertar publicaciones del modelo Post (datos de ejemplo de MockData.kt)
                                    val post1 = Post(
                                        title = "¿Cuál es tu anime favorito de 2024?",
                                        content = "Hola a todos! Quería saber cuáles han sido sus animes favoritos de este año. Personalmente me encantó Jujutsu Kaisen y Attack on Titan. ¿Qué opinan ustedes?",
                                        authorId = userId1.toInt(),
                                        authorName = "María García",
                                        themeId = 1, // Anime
                                        createdAt = System.currentTimeMillis() - 3600000, // 1 hora atrás
                                        likes = 15,
                                        comments = 8
                                    )
                                    
                                    val post2 = Post(
                                        title = "Recomendaciones de manga para principiantes",
                                        content = "Estoy empezando a leer manga y me gustaría algunas recomendaciones. He leído Death Note y me encantó. ¿Qué otros mangas me recomiendan?",
                                        authorId = userId2.toInt(),
                                        authorName = "Carlos López",
                                        themeId = 2, // Manga
                                        createdAt = System.currentTimeMillis() - 7200000, // 2 horas atrás
                                        likes = 12,
                                        comments = 5
                                    )
                                    
                                    val post3 = Post(
                                        title = "Mejores juegos de anime para PC",
                                        content = "¿Cuáles son los mejores juegos basados en anime que han jugado? He probado Dragon Ball FighterZ y está increíble. ¿Alguna otra recomendación?",
                                        authorId = userId3.toInt(),
                                        authorName = "Ana Rodríguez",
                                        themeId = 3, // Gaming
                                        createdAt = System.currentTimeMillis() - 10800000, // 3 horas atrás
                                        likes = 20,
                                        comments = 12
                                    )
                                    
                                    val post4 = Post(
                                        title = "One Piece: ¿Vale la pena verlo completo?",
                                        content = "Siempre he querido ver One Piece pero me da miedo que sean tantos episodios. ¿Realmente vale la pena invertir tanto tiempo? ¿Por dónde debería empezar?",
                                        authorId = userId1.toInt(),
                                        authorName = "María García",
                                        themeId = 1, // Anime
                                        createdAt = System.currentTimeMillis() - 14400000, // 4 horas atrás
                                        likes = 25,
                                        comments = 15
                                    )
                                    
                                    val post5 = Post(
                                        title = "Nuevos lanzamientos de manga en 2024",
                                        content = "¿Cuáles han sido los mejores lanzamientos de manga este año? He estado un poco desconectado y me gustaría ponerme al día con las novedades.",
                                        authorId = userId2.toInt(),
                                        authorName = "Carlos López",
                                        themeId = 2, // Manga
                                        createdAt = System.currentTimeMillis() - 18000000, // 5 horas atrás
                                        likes = 8,
                                        comments = 3
                                    )
                                    
                                    postDao.insertPost(post1)
                                    postDao.insertPost(post2)
                                    postDao.insertPost(post3)
                                    postDao.insertPost(post4)
                                    postDao.insertPost(post5)
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
                
                INSTANCE = instance
                instance
            }
        }
    }
}


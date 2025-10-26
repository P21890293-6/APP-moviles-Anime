package com.example.animeverse.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.animeverse.data.local.rol.RolEntity
import com.example.animeverse.data.local.rol.RolDao
import com.example.animeverse.data.local.estado.EstadoEntity
import com.example.animeverse.data.local.estado.EstadoDao
import com.example.animeverse.data.local.tema.TemaEntity
import com.example.animeverse.data.local.tema.TemaDao
import com.example.animeverse.data.local.usuario.UsuarioEntity
import com.example.animeverse.data.local.usuario.UsuarioDao
import com.example.animeverse.data.local.publicacion.PublicacionEntity
import com.example.animeverse.data.local.publicacion.PublicacionDao
import com.example.animeverse.data.local.comentario.ComentarioEntity
import com.example.animeverse.data.local.comentario.ComentarioDao
import com.example.animeverse.data.local.calificacion.CalificacionEntity
import com.example.animeverse.data.local.calificacion.CalificacionDao
import com.example.animeverse.data.local.hora_baneo.HoraBaneoEntity
import com.example.animeverse.data.local.hora_baneo.HoraBaneoDao
import com.example.animeverse.data.local.user.UserEntity
import com.example.animeverse.data.local.user.UserDao
import com.example.animeverse.data.local.post.PostEntity
import com.example.animeverse.data.local.post.PostDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

// @Database registra todas las entidades y versión del esquema
@Database(
    entities = [
        RolEntity::class,
        EstadoEntity::class,
        TemaEntity::class,
        UsuarioEntity::class,
        PublicacionEntity::class,
        ComentarioEntity::class,
        CalificacionEntity::class,
        HoraBaneoEntity::class,
        UserEntity::class,
        PostEntity::class
    ],
    version = 4,
    exportSchema = true     // Mantener true para inspección de esquema
)
abstract class AppDatabase : RoomDatabase() {

    // Exponemos todos los DAOs
    abstract fun rolDao(): RolDao
    abstract fun estadoDao(): EstadoDao
    abstract fun temaDao(): TemaDao
    abstract fun usuarioDao(): UsuarioDao
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

        // Obtiene la instancia única de la base de datos
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
                            // Lanzamos una corrutina en IO para insertar datos iniciales
                            CoroutineScope(Dispatchers.IO).launch {
                                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                                val currentDate = dateFormat.format(Date())
                                
                                val rolDao = getInstance(context).rolDao()
                                val estadoDao = getInstance(context).estadoDao()
                                val temaDao = getInstance(context).temaDao()
                                val usuarioDao = getInstance(context).usuarioDao()
                                val publicacionDao = getInstance(context).publicacionDao()
                                val comentarioDao = getInstance(context).comentarioDao()
                                val calificacionDao = getInstance(context).calificacionDao()
                                val userDao = getInstance(context).userDao()
                                val postDao = getInstance(context).postDao()

                                // Inserta seed sólo si las tablas están vacías
                                if (rolDao.count() == 0) {
                                    // Insertar roles
                                    val idRolAdmin = rolDao.insertRol(RolEntity(nombre = "Administrador"))
                                    val idRolUsuario = rolDao.insertRol(RolEntity(nombre = "Usuario"))
                                    val idRolModerador = rolDao.insertRol(RolEntity(nombre = "Moderador"))
                                    
                                    // Insertar estados
                                    val idEstadoActivo = estadoDao.insertEstado(EstadoEntity(nombre = "Activo"))
                                    val idEstadoInactivo = estadoDao.insertEstado(EstadoEntity(nombre = "Inactivo"))
                                    val idEstadoBaneado = estadoDao.insertEstado(EstadoEntity(nombre = "Baneado"))
                                    
                                    // Insertar temas
                                    val idTemaAnime = temaDao.insertTema(TemaEntity(nombre = "Anime"))
                                    val idTemaManga = temaDao.insertTema(TemaEntity(nombre = "Manga"))
                                    val idTemaGaming = temaDao.insertTema(TemaEntity(nombre = "Gaming"))
                                    val idTemaGeneral = temaDao.insertTema(TemaEntity(nombre = "General"))
                                    
                                    // Insertar usuarios de ejemplo (sistema completo)
                                    val idUsuario1 = usuarioDao.insertUsuario(
                                        UsuarioEntity(
                                            nombre = "Juan Pérez",
                                            correo = "juan@animeverse.cl",
                                            clave = "Juan123!",
                                            nickname = "JuanAnime",
                                            foto_perfil = null,
                                            id_rol = idRolUsuario.toInt(),
                                            id_estado = idEstadoActivo.toInt()
                                        )
                                    )
                                    
                                    val idUsuario2 = usuarioDao.insertUsuario(
                                        UsuarioEntity(
                                            nombre = "María García",
                                            correo = "maria@animeverse.cl",
                                            clave = "Maria123!",
                                            nickname = "MariaManga",
                                            foto_perfil = null,
                                            id_rol = idRolModerador.toInt(),
                                            id_estado = idEstadoActivo.toInt()
                                        )
                                    )
                                    
                                    // Insertar publicaciones
                                    val idPublicacion1 = publicacionDao.insertPublicacion(
                                        PublicacionEntity(
                                            nombre = "Mi anime favorito: Attack on Titan",
                                            fecha_registro = currentDate,
                                            descripcion = "Attack on Titan es una obra maestra del anime que combina acción, drama y filosofía de manera excepcional.",
                                            foto_publicacion = null,
                                            fecha_baneo = null,
                                            motivo_baneo = null,
                                            id_usuario = idUsuario1.toInt(),
                                            id_tema = idTemaAnime.toInt()
                                        )
                                    )
                                    
                                    val idPublicacion2 = publicacionDao.insertPublicacion(
                                        PublicacionEntity(
                                            nombre = "Recomendaciones de Manga 2024",
                                            fecha_registro = currentDate,
                                            descripcion = "Aquí comparto mis mangas favoritos que todo fan debería leer este año.",
                                            foto_publicacion = null,
                                            fecha_baneo = null,
                                            motivo_baneo = null,
                                            id_usuario = idUsuario2.toInt(),
                                            id_tema = idTemaManga.toInt()
                                        )
                                    )
                                    
                                    // Insertar comentarios
                                    comentarioDao.insertComentario(
                                        ComentarioEntity(
                                            fecha_registro = currentDate,
                                            comentario = "¡Excelente recomendación! Attack on Titan es realmente increíble.",
                                            fecha_baneo = null,
                                            motivo_baneo = null,
                                            id_publi = idPublicacion1.toInt(),
                                            id_usuario = idUsuario2.toInt()
                                        )
                                    )
                                    
                                    // Insertar calificaciones
                                    calificacionDao.insertCalificacion(
                                        CalificacionEntity(
                                            fecha_calificacion = currentDate,
                                            calificacion = 5,
                                            id_publi = idPublicacion1.toInt(),
                                            id_usuario = idUsuario2.toInt()
                                        )
                                    )
                                }
                                
                                // Insertar usuarios del sistema simple si no existen
                                if (userDao.count() == 0) {
                                    // Usuario ADMIN por defecto
                                    val adminId = userDao.insertUser(
                                        UserEntity(
                                            username = "admin",
                                            email = "admin@animeverse.com",
                                            password = "admin123",
                                            fullName = "Administrador",
                                            role = "ADMIN"
                                        )
                                    )
                                    
                                    // Usuarios públicos de ejemplo
                                    val userId1 = userDao.insertUser(
                                        UserEntity(
                                            username = "anime_lover",
                                            email = "anime@example.com",
                                            password = "123456",
                                            fullName = "María García",
                                            role = "USER"
                                        )
                                    )
                                    
                                    val userId2 = userDao.insertUser(
                                        UserEntity(
                                            username = "manga_reader",
                                            email = "manga@example.com",
                                            password = "123456",
                                            fullName = "Carlos López",
                                            role = "USER"
                                        )
                                    )
                                    
                                    val userId3 = userDao.insertUser(
                                        UserEntity(
                                            username = "gamer_pro",
                                            email = "gamer@example.com",
                                            password = "123456",
                                            fullName = "Ana Rodríguez",
                                            role = "USER"
                                        )
                                    )
                                    
                                    // Insertar posts de ejemplo
                                    postDao.insertPost(
                                        PostEntity(
                                            title = "¿Cuál es tu anime favorito de 2024?",
                                            content = "Hola a todos! Quería saber cuáles han sido sus animes favoritos de este año.",
                                            authorId = userId1,
                                            authorName = "María García",
                                            themeId = 1, // Anime
                                            likes = 15,
                                            comments = 8
                                        )
                                    )
                                    
                                    postDao.insertPost(
                                        PostEntity(
                                            title = "Recomendaciones de manga para principiantes",
                                            content = "He leído Death Note y me encantó. ¿Qué otros mangas me recomiendan?",
                                            authorId = userId2,
                                            authorName = "Carlos López",
                                            themeId = 2, // Manga
                                            likes = 12,
                                            comments = 5
                                        )
                                    )
                                }
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


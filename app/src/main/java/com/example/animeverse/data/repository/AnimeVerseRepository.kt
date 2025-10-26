package com.example.animeverse.data.repository

import com.example.animeverse.data.local.rol.RolDao
import com.example.animeverse.data.local.rol.RolEntity
import com.example.animeverse.data.local.estado.EstadoDao
import com.example.animeverse.data.local.estado.EstadoEntity
import com.example.animeverse.data.local.tema.TemaDao
import com.example.animeverse.data.local.tema.TemaEntity
import com.example.animeverse.data.local.usuario.UsuarioDao
import com.example.animeverse.data.local.usuario.UsuarioEntity
import com.example.animeverse.data.local.publicacion.PublicacionDao
import com.example.animeverse.data.local.publicacion.PublicacionEntity
import com.example.animeverse.data.local.comentario.ComentarioDao
import com.example.animeverse.data.local.comentario.ComentarioEntity
import com.example.animeverse.data.local.calificacion.CalificacionDao
import com.example.animeverse.data.local.calificacion.CalificacionEntity
import com.example.animeverse.data.local.hora_baneo.HoraBaneoDao
import com.example.animeverse.data.local.hora_baneo.HoraBaneoEntity
import com.example.animeverse.data.local.user.UserDao
import com.example.animeverse.data.local.user.UserEntity
import com.example.animeverse.data.local.post.PostDao
import com.example.animeverse.data.local.post.PostEntity
import kotlinx.coroutines.flow.Flow

// Repositorio: orquesta reglas de negocio para todas las operaciones de la base de datos
class AnimeVerseRepository(
    private val rolDao: RolDao,
    private val estadoDao: EstadoDao,
    private val temaDao: TemaDao,
    private val usuarioDao: UsuarioDao,
    private val publicacionDao: PublicacionDao,
    private val comentarioDao: ComentarioDao,
    private val calificacionDao: CalificacionDao,
    private val horaBaneoDao: HoraBaneoDao,
    private val userDao: UserDao,
    private val postDao: PostDao
) {
    
    // ========== OPERACIONES ROL ==========
    fun getAllRoles(): Flow<List<RolEntity>> = rolDao.getAllRoles()
    suspend fun getRolById(id: Int): RolEntity? = rolDao.getRolById(id)
    suspend fun insertRol(rol: RolEntity): Long = rolDao.insertRol(rol)
    
    // ========== OPERACIONES ESTADO ==========
    fun getAllEstados(): Flow<List<EstadoEntity>> = estadoDao.getAllEstados()
    suspend fun getEstadoById(id: Int): EstadoEntity? = estadoDao.getEstadoById(id)
    suspend fun insertEstado(estado: EstadoEntity): Long = estadoDao.insertEstado(estado)
    
    // ========== OPERACIONES TEMA ==========
    fun getAllTemas(): Flow<List<TemaEntity>> = temaDao.getAllTemas()
    suspend fun getTemaById(id: Int): TemaEntity? = temaDao.getTemaById(id)
    suspend fun insertTema(tema: TemaEntity): Long = temaDao.insertTema(tema)
    
    // ========== OPERACIONES USUARIO (Sistema Completo) ==========
    fun getAllUsuarios(): Flow<List<UsuarioEntity>> = usuarioDao.getAllUsuarios()
    suspend fun getUsuarioById(id: Int): UsuarioEntity? = usuarioDao.getUsuarioById(id)
    suspend fun getUsuarioByEmail(correo: String): UsuarioEntity? = usuarioDao.getUsuarioByEmail(correo)
    suspend fun insertUsuario(usuario: UsuarioEntity): Long = usuarioDao.insertUsuario(usuario)
    suspend fun updateUsuario(usuario: UsuarioEntity) = usuarioDao.updateUsuario(usuario)
    suspend fun deleteUsuario(usuario: UsuarioEntity) = usuarioDao.deleteUsuario(usuario)
    
    // Login para usuarios del sistema completo
    suspend fun loginUsuario(email: String, password: String): Result<UsuarioEntity> {
        val usuario = usuarioDao.getUsuarioByEmail(email)
        return if (usuario != null && usuario.clave == password) {
            Result.success(usuario)
        } else {
            Result.failure(IllegalArgumentException("Credenciales inválidas"))
        }
    }
    
    // Registro para usuarios del sistema completo
    suspend fun registerUsuario(
        nombre: String, 
        correo: String, 
        nickname: String, 
        clave: String, 
        idRol: Int, 
        idEstado: Int
    ): Result<Long> {
        val existsEmail = usuarioDao.getUsuarioByEmail(correo) != null
        val existsNickname = usuarioDao.getUsuarioByNickname(nickname) != null
        
        if (existsEmail) {
            return Result.failure(IllegalStateException("El correo ya está registrado"))
        }
        if (existsNickname) {
            return Result.failure(IllegalStateException("El nickname ya está en uso"))
        }
        
        val id = usuarioDao.insertUsuario(
            UsuarioEntity(
                nombre = nombre,
                correo = correo,
                clave = clave,
                nickname = nickname,
                foto_perfil = null,
                id_rol = idRol,
                id_estado = idEstado
            )
        )
        return Result.success(id)
    }
    
    // ========== OPERACIONES PUBLICACION ==========
    fun getAllPublicaciones(): Flow<List<PublicacionEntity>> = publicacionDao.getAllPublicaciones()
    suspend fun getPublicacionById(id: Int): PublicacionEntity? = publicacionDao.getPublicacionById(id)
    fun getPublicacionesByUsuario(idUsuario: Int): Flow<List<PublicacionEntity>> = 
        publicacionDao.getPublicacionesByUsuario(idUsuario)
    fun getPublicacionesByTema(idTema: Int): Flow<List<PublicacionEntity>> = 
        publicacionDao.getPublicacionesByTema(idTema)
    suspend fun insertPublicacion(publicacion: PublicacionEntity): Long = 
        publicacionDao.insertPublicacion(publicacion)
    suspend fun updatePublicacion(publicacion: PublicacionEntity) = 
        publicacionDao.updatePublicacion(publicacion)
    suspend fun deletePublicacion(publicacion: PublicacionEntity) = 
        publicacionDao.deletePublicacion(publicacion)
    
    // ========== OPERACIONES COMENTARIO ==========
    fun getAllComentarios(): Flow<List<ComentarioEntity>> = comentarioDao.getAllComentarios()
    suspend fun getComentarioById(id: Int): ComentarioEntity? = comentarioDao.getComentarioById(id)
    fun getComentariosByPublicacion(idPublicacion: Int): Flow<List<ComentarioEntity>> = 
        comentarioDao.getComentariosByPublicacion(idPublicacion)
    suspend fun insertComentario(comentario: ComentarioEntity): Long = 
        comentarioDao.insertComentario(comentario)
    
    // ========== OPERACIONES CALIFICACION ==========
    fun getAllCalificaciones(): Flow<List<CalificacionEntity>> = calificacionDao.getAllCalificaciones()
    fun getCalificacionesByPublicacion(idPublicacion: Int): Flow<List<CalificacionEntity>> = 
        calificacionDao.getCalificacionesByPublicacion(idPublicacion)
    suspend fun getPromedioCalificacion(idPublicacion: Int): Double? = 
        calificacionDao.getPromedioCalificacion(idPublicacion)
    suspend fun insertCalificacion(calificacion: CalificacionEntity): Long = 
        calificacionDao.insertCalificacion(calificacion)
    
    // ========== OPERACIONES HORA_BANEO ==========
    fun getAllHoraBaneos(): Flow<List<HoraBaneoEntity>> = horaBaneoDao.getAllHoraBaneos()
    fun getHoraBaneosByUsuario(idUsuario: Int): Flow<List<HoraBaneoEntity>> = 
        horaBaneoDao.getHoraBaneosByUsuario(idUsuario)
    suspend fun insertHoraBaneo(horaBaneo: HoraBaneoEntity): Long = 
        horaBaneoDao.insertHoraBaneo(horaBaneo)
    
    // ========== OPERACIONES USER (Sistema Simple) ==========
    fun getAllUsers(): Flow<List<UserEntity>> = userDao.getAllUsers()
    suspend fun getUserById(id: Long): UserEntity? = userDao.getUserById(id)
    suspend fun getUserByEmail(email: String): UserEntity? = userDao.getUserByEmail(email)
    suspend fun insertUser(user: UserEntity): Long = userDao.insertUser(user)
    suspend fun updateUser(user: UserEntity) = userDao.updateUser(user)
    suspend fun deleteUser(user: UserEntity) = userDao.deleteUser(user)
    
    // Login para sistema simple
    suspend fun loginUser(email: String, password: String): Result<UserEntity> {
        val user = userDao.getUserByEmail(email)
        return if (user != null && user.password == password) {
            Result.success(user)
        } else {
            Result.failure(IllegalArgumentException("Credenciales inválidas"))
        }
    }
    
    // Registro para sistema simple
    suspend fun registerUser(
        username: String, 
        email: String, 
        password: String, 
        fullName: String
    ): Result<Long> {
        val existsEmail = userDao.getUserByEmail(email) != null
        val existsUsername = userDao.getUserByUsername(username) != null
        
        if (existsEmail) {
            return Result.failure(IllegalStateException("El correo ya está registrado"))
        }
        if (existsUsername) {
            return Result.failure(IllegalStateException("El nombre de usuario ya está en uso"))
        }
        
        val id = userDao.insertUser(
            UserEntity(
                username = username,
                email = email,
                password = password,
                fullName = fullName
            )
        )
        return Result.success(id)
    }
    
    // ========== OPERACIONES POST (Sistema Simple) ==========
    fun getAllPosts(): Flow<List<PostEntity>> = postDao.getAllPosts()
    suspend fun getPostById(id: Long): PostEntity? = postDao.getPostById(id)
    fun getPostsByAuthor(authorId: Long): Flow<List<PostEntity>> = postDao.getPostsByAuthor(authorId)
    fun getPostsByTheme(themeId: Int): Flow<List<PostEntity>> = postDao.getPostsByTheme(themeId)
    suspend fun insertPost(post: PostEntity): Long = postDao.insertPost(post)
    suspend fun updatePost(post: PostEntity) = postDao.updatePost(post)
    suspend fun deletePost(post: PostEntity) = postDao.deletePost(post)
    suspend fun incrementLikes(id: Long) = postDao.incrementLikes(id)
    suspend fun incrementComments(id: Long) = postDao.incrementComments(id)
}


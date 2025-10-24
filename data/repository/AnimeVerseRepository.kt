package com.example.animeverse.data.repository

import com.example.animeverse.data.dao.*
import com.example.animeverse.data.entity.*
import com.example.animeverse.data.model.User
import com.example.animeverse.data.model.Post
import kotlinx.coroutines.flow.Flow

// Repositorio principal que orquesta todas las operaciones de la base de datos
class AnimeVerseRepository(
    private val rolDao: RolDao,
    private val estadoDao: EstadoDao,
    private val usuarioDao: UsuarioDao,
    private val temaDao: TemaDao,
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
    suspend fun updateRol(rol: RolEntity) = rolDao.updateRol(rol)
    suspend fun deleteRol(rol: RolEntity) = rolDao.deleteRol(rol)
    
    // ========== OPERACIONES ESTADO ==========
    fun getAllEstados(): Flow<List<EstadoEntity>> = estadoDao.getAllEstados()
    suspend fun getEstadoById(id: Int): EstadoEntity? = estadoDao.getEstadoById(id)
    suspend fun insertEstado(estado: EstadoEntity): Long = estadoDao.insertEstado(estado)
    suspend fun updateEstado(estado: EstadoEntity) = estadoDao.updateEstado(estado)
    suspend fun deleteEstado(estado: EstadoEntity) = estadoDao.deleteEstado(estado)
    
    // ========== OPERACIONES USUARIO ==========
    fun getAllUsuarios(): Flow<List<UsuarioEntity>> = usuarioDao.getAllUsuarios()
    suspend fun getUsuarioById(id: Int): UsuarioEntity? = usuarioDao.getUsuarioById(id)
    suspend fun getUsuarioByEmail(correo: String): UsuarioEntity? = usuarioDao.getUsuarioByEmail(correo)
    suspend fun getUsuarioByNickname(nickname: String): UsuarioEntity? = usuarioDao.getUsuarioByNickname(nickname)
    suspend fun insertUsuario(usuario: UsuarioEntity): Long = usuarioDao.insertUsuario(usuario)
    suspend fun updateUsuario(usuario: UsuarioEntity) = usuarioDao.updateUsuario(usuario)
    suspend fun deleteUsuario(usuario: UsuarioEntity) = usuarioDao.deleteUsuario(usuario)
    
    // Login: busca por email y valida contraseña
    suspend fun login(email: String, password: String): Result<UsuarioEntity> {
        val user = usuarioDao.getUsuarioByEmail(email)
        return if (user != null && user.clave == password) {
            Result.success(user)
        } else {
            Result.failure(IllegalArgumentException("Credenciales inválidas"))
        }
    }
    
    // Registro: valida no duplicado y crea nuevo usuario
    suspend fun register(nombre: String, correo: String, nickname: String, clave: String, idRol: Int, idEstado: Int): Result<Long> {
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
    
    // ========== OPERACIONES TEMA ==========
    fun getAllTemas(): Flow<List<TemaEntity>> = temaDao.getAllTemas()
    suspend fun getTemaById(id: Int): TemaEntity? = temaDao.getTemaById(id)
    suspend fun insertTema(tema: TemaEntity): Long = temaDao.insertTema(tema)
    suspend fun updateTema(tema: TemaEntity) = temaDao.updateTema(tema)
    suspend fun deleteTema(tema: TemaEntity) = temaDao.deleteTema(tema)
    
    // ========== OPERACIONES PUBLICACION ==========
    fun getAllPublicaciones(): Flow<List<PublicacionEntity>> = publicacionDao.getAllPublicaciones()
    suspend fun getPublicacionById(id: Int): PublicacionEntity? = publicacionDao.getPublicacionById(id)
    fun getPublicacionesByUsuario(idUsuario: Int): Flow<List<PublicacionEntity>> = publicacionDao.getPublicacionesByUsuario(idUsuario)
    fun getPublicacionesByTema(idTema: Int): Flow<List<PublicacionEntity>> = publicacionDao.getPublicacionesByTema(idTema)
    suspend fun insertPublicacion(publicacion: PublicacionEntity): Long = publicacionDao.insertPublicacion(publicacion)
    suspend fun updatePublicacion(publicacion: PublicacionEntity) = publicacionDao.updatePublicacion(publicacion)
    suspend fun deletePublicacion(publicacion: PublicacionEntity) = publicacionDao.deletePublicacion(publicacion)
    
    // ========== OPERACIONES COMENTARIO ==========
    fun getAllComentarios(): Flow<List<ComentarioEntity>> = comentarioDao.getAllComentarios()
    suspend fun getComentarioById(id: Int): ComentarioEntity? = comentarioDao.getComentarioById(id)
    fun getComentariosByPublicacion(idPublicacion: Int): Flow<List<ComentarioEntity>> = comentarioDao.getComentariosByPublicacion(idPublicacion)
    fun getComentariosByUsuario(idUsuario: Int): Flow<List<ComentarioEntity>> = comentarioDao.getComentariosByUsuario(idUsuario)
    suspend fun insertComentario(comentario: ComentarioEntity): Long = comentarioDao.insertComentario(comentario)
    suspend fun updateComentario(comentario: ComentarioEntity) = comentarioDao.updateComentario(comentario)
    suspend fun deleteComentario(comentario: ComentarioEntity) = comentarioDao.deleteComentario(comentario)
    
    // ========== OPERACIONES CALIFICACION ==========
    fun getAllCalificaciones(): Flow<List<CalificacionEntity>> = calificacionDao.getAllCalificaciones()
    suspend fun getCalificacionById(id: Int): CalificacionEntity? = calificacionDao.getCalificacionById(id)
    fun getCalificacionesByPublicacion(idPublicacion: Int): Flow<List<CalificacionEntity>> = calificacionDao.getCalificacionesByPublicacion(idPublicacion)
    fun getCalificacionesByUsuario(idUsuario: Int): Flow<List<CalificacionEntity>> = calificacionDao.getCalificacionesByUsuario(idUsuario)
    suspend fun getPromedioCalificacion(idPublicacion: Int): Double? = calificacionDao.getPromedioCalificacion(idPublicacion)
    suspend fun insertCalificacion(calificacion: CalificacionEntity): Long = calificacionDao.insertCalificacion(calificacion)
    suspend fun updateCalificacion(calificacion: CalificacionEntity) = calificacionDao.updateCalificacion(calificacion)
    suspend fun deleteCalificacion(calificacion: CalificacionEntity) = calificacionDao.deleteCalificacion(calificacion)
    
    // ========== OPERACIONES HORA_BANEO ==========
    fun getAllHoraBaneos(): Flow<List<HoraBaneoEntity>> = horaBaneoDao.getAllHoraBaneos()
    suspend fun getHoraBaneoById(id: Int): HoraBaneoEntity? = horaBaneoDao.getHoraBaneoById(id)
    fun getHoraBaneosByUsuario(idUsuario: Int): Flow<List<HoraBaneoEntity>> = horaBaneoDao.getHoraBaneosByUsuario(idUsuario)
    suspend fun insertHoraBaneo(horaBaneo: HoraBaneoEntity): Long = horaBaneoDao.insertHoraBaneo(horaBaneo)
    suspend fun updateHoraBaneo(horaBaneo: HoraBaneoEntity) = horaBaneoDao.updateHoraBaneo(horaBaneo)
    suspend fun deleteHoraBaneo(horaBaneo: HoraBaneoEntity) = horaBaneoDao.deleteHoraBaneo(horaBaneo)
    
    // ========== OPERACIONES USER (Modelo Simple) ==========
    fun getAllUsers(): Flow<List<User>> = userDao.getAllUsers()
    suspend fun getUserById(id: Int): User? = userDao.getUserById(id)
    suspend fun getUserByEmail(email: String): User? = userDao.getUserByEmail(email)
    suspend fun getUserByUsername(username: String): User? = userDao.getUserByUsername(username)
    suspend fun insertUser(user: User): Long = userDao.insertUser(user)
    suspend fun updateUser(user: User) = userDao.updateUser(user)
    suspend fun deleteUser(user: User) = userDao.deleteUser(user)
    suspend fun deleteUserById(id: Int) = userDao.deleteUserById(id)
    suspend fun getUserCount(): Int = userDao.getUserCount()
    
    // ========== OPERACIONES POST (Modelo Simple) ==========
    fun getAllPosts(): Flow<List<Post>> = postDao.getAllPosts()
    suspend fun getPostById(id: Int): Post? = postDao.getPostById(id)
    fun getPostsByAuthor(authorId: Int): Flow<List<Post>> = postDao.getPostsByAuthor(authorId)
    fun getPostsByTheme(themeId: Int): Flow<List<Post>> = postDao.getPostsByTheme(themeId)
    fun getAllPostsOrderedByDate(): Flow<List<Post>> = postDao.getAllPostsOrderedByDate()
    suspend fun insertPost(post: Post): Long = postDao.insertPost(post)
    suspend fun updatePost(post: Post) = postDao.updatePost(post)
    suspend fun deletePost(post: Post) = postDao.deletePost(post)
    suspend fun deletePostById(id: Int) = postDao.deletePostById(id)
    suspend fun getPostCount(): Int = postDao.getPostCount()
    suspend fun incrementLikes(id: Int) = postDao.incrementLikes(id)
    suspend fun incrementComments(id: Int) = postDao.incrementComments(id)
}


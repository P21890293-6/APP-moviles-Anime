package com.example.animeverse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.animeverse.data.local.database.AppDatabase
import com.example.animeverse.data.mock.MockData
import com.example.animeverse.ui.screen.HomeScreen
import com.example.animeverse.ui.screen.LoginScreen
import com.example.animeverse.ui.screen.RegisterScreen
import com.example.animeverse.ui.screen.AdminDashboard
import com.example.animeverse.ui.screen.UserHomeScreen
import com.example.animeverse.ui.screen.PostDetailScreen
import com.example.animeverse.ui.screen.SettingsScreen
import com.example.animeverse.ui.screen.ChangePasswordScreen
import com.example.animeverse.ui.screen.EditProfileScreenVm
import com.example.animeverse.ui.screen.MockPost
import com.example.animeverse.data.local.user.isAdmin
import com.example.animeverse.ui.viewmodel.EditProfileViewModel
import com.example.animeverse.ui.viewmodel.EditProfileViewModelFactory
import kotlinx.coroutines.launch
import com.example.animeverse.ui.theme.AnimeVerseTheme
import com.example.animeverse.ui.viewmodel.AuthViewModel
import com.example.animeverse.ui.viewmodel.AuthViewModelFactory
import com.example.animeverse.ui.components.AppDrawer
import com.example.animeverse.ui.components.AppTopBar
import com.example.animeverse.ui.components.drawerItemsForUser
import com.example.animeverse.data.local.session.SessionManager

/**
 * Activity principal de AnimeVerse.
 * Gestiona la navegación entre Login, Register y Home.
 * Ahora conectado con SQLite a través de Room Database.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Crear instancia de la base de datos (SQLite)
            val database = AppDatabase.getInstance(applicationContext)
            val userDao = database.userDao()
            
            // Crear ViewModel con factory
            val authViewModel: AuthViewModel = viewModel(
                factory = AuthViewModelFactory(userDao)
            )
            
            AnimeVerseTheme(darkTheme = false) {  // El tema se controla dentro de AnimeVerseApp
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AnimeVerseApp(authViewModel)
                }
            }
        }
    }
}

/**
 * Componente principal que gestiona la navegación entre pantallas.
 * Ahora conectado con la base de datos SQLite (Room).
 * Incluye navegación basada en roles (USER/ADMIN).
 * Patrón del profesor: ModalNavigationDrawer + Scaffold + TopBar
 */
@Composable
fun AnimeVerseApp(authViewModel: AuthViewModel) {
    val context = LocalContext.current
    val database = AppDatabase.getInstance(context)
    val sessionManager = remember { SessionManager(context) }
    val scope = rememberCoroutineScope()
    
    var isCheckingSession by remember { mutableStateOf(true) }
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Login) }
    val loginState by authViewModel.loginState.collectAsState()
    
    // Estado para lista de usuarios (para admin)
    var allUsers by remember { mutableStateOf<List<com.example.animeverse.data.local.user.UserEntity>>(emptyList()) }
    
    // Estado para lista de publicaciones (para admin y usuarios)
    var allPosts by remember { mutableStateOf<List<com.example.animeverse.data.local.post.PostEntity>>(emptyList()) }
    
    // Estado para publicaciones en formato MockPost para UserHomeScreen
    var mockPosts by remember { mutableStateOf<List<MockPost>>(emptyList()) }
    
    // Estado para reportes (admin)
    var allReports by remember { mutableStateOf<List<com.example.animeverse.data.local.post.PostReportEntity>>(emptyList()) }
    
    // Verificar sesión al iniciar
    LaunchedEffect(Unit) {
        val userId = sessionManager.getUserId()
        if (userId != null) {
            val user = database.userDao().getUserById(userId)
            if (user != null) {
                // Restaurar sesión
                currentScreen = if (user.isAdmin()) {
                    Screen.AdminDashboard(user)
                } else {
                    Screen.UserHome(user)
                }
            } else {
                // Usuario no encontrado, limpiar sesión
                sessionManager.clearSession()
            }
        }
        isCheckingSession = false
    }
    
    // Mostrar pantalla de carga mientras se verifica la sesión
    if (isCheckingSession) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }
    
    // Estado del drawer (patrón del profesor)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    
    // Estado del modo oscuro
    var isDarkMode by remember { mutableStateOf(false) }
    
    // Usuario actual según la pantalla
    val currentUser = when (val screen = currentScreen) {
        is Screen.AdminDashboard -> screen.user
        is Screen.UserHome -> screen.user
        is Screen.Settings -> screen.user
        is Screen.ChangePassword -> screen.user
        is Screen.EditProfile -> screen.user
        is Screen.PostDetail -> screen.user
        else -> null
    }
    
    // Cargar usuarios, posts y reportes cuando se necesiten
    LaunchedEffect(currentScreen) {
        if (currentScreen is Screen.AdminDashboard) {
            scope.launch {
                allUsers = database.userDao().getAll()
                allPosts = database.postDao().getAll()
                allReports = database.postDao().getAllPendingReports()
            }
        } else if (currentScreen is Screen.UserHome) {
            scope.launch {
                allPosts = database.postDao().getAll()
                // Convertir PostEntity a MockPost
                mockPosts = allPosts.map { post ->
                    val themeName = when (post.themeId) {
                        1 -> "Anime"
                        2 -> "Manga"
                        3 -> "Gaming"
                        else -> "General"
                    }
                    MockPost(
                        id = post.id,
                        authorName = post.authorName,
                        title = post.title,
                        content = post.content,
                        category = themeName,
                        imageUri = post.imageUri,
                        likes = post.likes,
                        comments = post.comments
                    )
                }
            }
        }
    }
    
    // Helpers de navegación (patrón del profesor)
    val goHome: () -> Unit = {
        if (currentUser != null) {
            currentScreen = if (currentUser.isAdmin()) {
                Screen.AdminDashboard(currentUser)
            } else {
                Screen.UserHome(currentUser)
            }
        } else {
            currentScreen = Screen.Login
        }
    }
    
    val goLogin: () -> Unit = {
        authViewModel.clearLoginResult()
        currentScreen = Screen.Login
    }
    
    val goRegister: () -> Unit = {
        currentScreen = Screen.Register
    }
    
    val goEditProfile: () -> Unit = {
        if (currentUser != null) {
            currentScreen = Screen.EditProfile(currentUser)
        }
    }
    
    val goSettings: () -> Unit = {
        if (currentUser != null) {
            currentScreen = Screen.Settings(currentUser)
        }
    }
    
    val onLogout: () -> Unit = {
        // Limpiar sesión guardada
        sessionManager.clearSession()
        
        authViewModel.clearLoginResult()
        scope.launch { drawerState.close() }
        currentScreen = Screen.Login
    }
    
    // Aplicar el tema dinámicamente
    AnimeVerseTheme(darkTheme = isDarkMode) {
        // Mostrar drawer solo si hay usuario logueado (patrón del profesor)
        if (currentUser != null) {
            // Usuario logueado: mostrar drawer
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    AppDrawer(
                        currentUser = currentUser,
                        items = drawerItemsForUser(
                            currentUser = currentUser,
                            onHome = {
                                scope.launch { drawerState.close() }
                                goHome()
                            },
                            onLogin = {
                                scope.launch { drawerState.close() }
                                goLogin()
                            },
                            onRegister = {
                                scope.launch { drawerState.close() }
                                goRegister()
                            },
                            onSettings = {
                                scope.launch { drawerState.close() }
                                goSettings()
                            }
                        ),
                        onLogout = onLogout,
                        isDarkMode = isDarkMode,
                        onToggleDarkMode = { isDarkMode = it }
                    )
                }
            ) {
            Scaffold(
                topBar = {
                    // Ocultar la barra superior para pantallas que tienen su propia barra
                    if (currentScreen !is Screen.UserHome && 
                        currentScreen !is Screen.Settings && 
                        currentScreen !is Screen.ChangePassword &&
                        currentScreen !is Screen.EditProfile &&
                        currentScreen !is Screen.PostDetail) {
                        AppTopBar(
                            title = when (currentScreen) {
                                is Screen.AdminDashboard -> "Panel de Administrador"
                                Screen.Login -> "Iniciar Sesión"
                                Screen.Register -> "Registro"
                                Screen.Home -> "AnimeVerse"
                                else -> "AnimeVerse"
                            },
                            currentUser = currentUser,
                            onOpenDrawer = { scope.launch { drawerState.open() } }
                        )
                    }
                }
            ) { paddingValues ->
                // El paddingValues se ignora porque las pantallas internas manejan su propio padding
                @Suppress("UNUSED_EXPRESSION")
                paddingValues
                
                when (val screen = currentScreen) {
                    is Screen.AdminDashboard -> AdminDashboard(
                        currentUser = screen.user,
                        allUsers = allUsers,
                        allPosts = allPosts,
                        allReports = allReports,
                        onLogout = onLogout,
                        onManageUsers = {
                            // TODO: Implementar gestión de usuarios
                        },
                        onManagePosts = {
                            // TODO: Implementar gestión de posts
                        },
                        onManageReports = {
                            // TODO: Implementar gestión de reportes
                        },
                        onViewStats = {
                            // TODO: Implementar estadísticas
                        },
                        onBanUser = { userId, banned ->
                            // Banear/Desbanear usuario
                            scope.launch {
                                database.userDao().setBannedStatus(userId, banned)
                                // Recargar lista de usuarios
                                allUsers = database.userDao().getAll()
                            }
                        },
                        onDeleteUser = { userId ->
                            // Eliminar usuario
                            scope.launch {
                                database.userDao().deleteUserById(userId)
                                // Recargar lista de usuarios
                                allUsers = database.userDao().getAll()
                            }
                        },
                        onDeletePost = { postId ->
                            // Eliminar publicación
                            scope.launch {
                                database.postDao().deletePostById(postId)
                                // Recargar lista de publicaciones
                                allPosts = database.postDao().getAll()
                            }
                        },
                        onDismissReport = { reportId ->
                            scope.launch {
                                // Actualizar estado del reporte a "DISMISSED"
                                database.postDao().updateReportStatus(reportId, "DISMISSED")
                                // Recargar reportes pendientes
                                allReports = database.postDao().getAllPendingReports()
                                android.widget.Toast.makeText(
                                    context,
                                    "Reporte descartado",
                                    android.widget.Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        onDeleteReportedPost = { reportId, postId ->
                            scope.launch {
                                // Eliminar el post
                                database.postDao().deletePostById(postId)
                                // Actualizar el reporte a "REVIEWED"
                                database.postDao().updateReportStatus(reportId, "REVIEWED")
                                // Recargar
                                allPosts = database.postDao().getAll()
                                allReports = database.postDao().getAllPendingReports()
                                android.widget.Toast.makeText(
                                    context,
                                    "Publicación eliminada y reporte revisado",
                                    android.widget.Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    )
                    is Screen.UserHome -> UserHomeScreen(
                        currentUser = screen.user,
                        posts = mockPosts,
                        onLogout = onLogout,
                        onViewProfile = goSettings,
                        onOpenDrawer = { scope.launch { drawerState.open() } },
                        onLikePost = { postId ->
                            scope.launch {
                                val userId = screen.user.id
                                val hasLiked = database.postDao().hasUserLiked(postId, userId)
                                
                                if (hasLiked) {
                                    // Quitar like
                                    database.postDao().deleteLike(postId, userId)
                                    database.postDao().decrementLikes(postId)
                                } else {
                                    // Dar like
                                    database.postDao().insertLike(
                                        com.example.animeverse.data.local.post.PostLikeEntity(
                                            postId = postId,
                                            userId = userId
                                        )
                                    )
                                    database.postDao().incrementLikes(postId)
                                }
                                
                                // Recargar publicaciones
                                allPosts = database.postDao().getAll()
                                mockPosts = allPosts.map { post ->
                                    val themeName = when (post.themeId) {
                                        1 -> "Anime"
                                        2 -> "Manga"
                                        3 -> "Gaming"
                                        else -> "General"
                                    }
                                    MockPost(
                                        id = post.id,
                                        authorName = post.authorName,
                                        title = post.title,
                                        content = post.content,
                                        category = themeName,
                                        likes = post.likes,
                                        comments = post.comments
                                    )
                                }
                            }
                        },
                        onCommentPost = { postId ->
                            // TODO: Implementar sistema de comentarios
                            scope.launch {
                                android.widget.Toast.makeText(
                                    context,
                                    "Sistema de comentarios próximamente",
                                    android.widget.Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        onReportPost = { postId ->
                            scope.launch {
                                // Obtener info del post
                                val post = database.postDao().getPostById(postId)
                                if (post != null) {
                                    // Crear reporte
                                    val report = com.example.animeverse.data.local.post.PostReportEntity(
                                        postId = postId,
                                        postTitle = post.title,
                                        postAuthorName = post.authorName,
                                        reportedBy = screen.user.id,
                                        reportedByName = screen.user.fullName,
                                        reason = "Contenido inapropiado"
                                    )
                                    database.postDao().insertReport(report)
                                    
                                    android.widget.Toast.makeText(
                                        context,
                                        "Publicación reportada. El admin revisará el caso.",
                                        android.widget.Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        },
                        onCreatePost = { title, content, themeId: Int, imageUri: String? ->
                            scope.launch {
                                // Crear nueva publicación en la base de datos
                                val newPost = com.example.animeverse.data.local.post.PostEntity(
                                    title = title,
                                    content = content,
                                    authorId = screen.user.id,
                                    authorName = screen.user.fullName,
                                    themeId = themeId,
                                    imageUri = imageUri
                                )
                                database.postDao().insertPost(newPost)
                                
                                // Recargar publicaciones
                                allPosts = database.postDao().getAll()
                                mockPosts = allPosts.map { post ->
                                    val themeName = when (post.themeId) {
                                        1 -> "Anime"
                                        2 -> "Manga"
                                        3 -> "Gaming"
                                        else -> "General"
                                    }
                                    MockPost(
                                        id = post.id,
                                        authorName = post.authorName,
                                        title = post.title,
                                        content = post.content,
                                        category = themeName,
                                        likes = post.likes,
                                        comments = post.comments
                                    )
                                }
                            }
                        },
                        onViewPostDetail = { postId ->
                            // Buscar el post en mockPosts
                            val post = mockPosts.find { it.id == postId }
                            if (post != null) {
                                currentScreen = Screen.PostDetail(
                                    user = screen.user,
                                    postId = post.id,
                                    authorName = post.authorName,
                                    title = post.title,
                                    content = post.content,
                                    category = post.category,
                                    imageUri = post.imageUri,
                                    likes = post.likes,
                                    comments = post.comments
                                )
                            }
                        }
                    )
                    is Screen.PostDetail -> PostDetailScreen(
                        postId = screen.postId,
                        authorName = screen.authorName,
                        title = screen.title,
                        content = screen.content,
                        category = screen.category,
                        imageUri = screen.imageUri,
                        likes = screen.likes,
                        comments = screen.comments,
                        onBackClick = {
                            currentScreen = Screen.UserHome(screen.user)
                        },
                        onLikeClick = {
                            scope.launch {
                                val userId = screen.user.id
                                val hasLiked = database.postDao().hasUserLiked(screen.postId, userId)
                                
                                if (hasLiked) {
                                    database.postDao().deleteLike(screen.postId, userId)
                                    database.postDao().decrementLikes(screen.postId)
                                } else {
                                    database.postDao().insertLike(
                                        com.example.animeverse.data.local.post.PostLikeEntity(
                                            postId = screen.postId,
                                            userId = userId
                                        )
                                    )
                                    database.postDao().incrementLikes(screen.postId)
                                }
                                
                                // Recargar publicaciones
                                allPosts = database.postDao().getAll()
                            }
                        },
                        onCommentClick = {
                            android.widget.Toast.makeText(
                                context,
                                "Sistema de comentarios próximamente",
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                        },
                        onShareClick = {
                            android.widget.Toast.makeText(
                                context,
                                "Compartir próximamente",
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                    is Screen.Settings -> {
                        SettingsScreen(
                            currentUser = screen.user,
                            onBackClick = {
                                // Volver a UserHome
                                currentScreen = Screen.UserHome(screen.user)
                            },
                            onEditProfile = {
                                // Ir a editar perfil
                                currentScreen = Screen.EditProfile(screen.user)
                            },
                            onChangePassword = {
                                // Ir a cambiar contraseña
                                currentScreen = Screen.ChangePassword(screen.user)
                            },
                            onPrivacySettings = {
                                // TODO: Implementar configuración de privacidad
                                android.widget.Toast.makeText(
                                    context,
                                    "Función próximamente disponible",
                                    android.widget.Toast.LENGTH_SHORT
                                ).show()
                            },
                            onAbout = {
                                // TODO: Implementar acerca de
                                android.widget.Toast.makeText(
                                    context,
                                    "AnimeVerse v1.0.0",
                                    android.widget.Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                    is Screen.ChangePassword -> {
                        ChangePasswordScreen(
                            currentUser = screen.user,
                            onBackClick = {
                                // Volver a Settings
                                currentScreen = Screen.Settings(screen.user)
                            },
                            onPasswordChanged = {
                                // Contraseña cambiada, volver a Settings
                                currentScreen = Screen.Settings(screen.user)
                            }
                        )
                    }
                    is Screen.EditProfile -> {
                        // Crear ViewModel para editar perfil
                        val editProfileViewModel: EditProfileViewModel = viewModel(
                            factory = EditProfileViewModelFactory(database.userDao())
                        )
                        
                        EditProfileScreenVm(
                            vm = editProfileViewModel,
                            currentUser = screen.user,
                            onBackClick = {
                                // Volver a Settings con datos actualizados
                                scope.launch {
                                    val updatedUser = database.userDao().getUserById(screen.user.id)
                                    if (updatedUser != null) {
                                        currentScreen = Screen.Settings(updatedUser)
                                    }
                                }
                            },
                            onProfileUpdated = {
                                // Recargar usuario y volver a Settings
                                scope.launch {
                                    val updatedUser = database.userDao().getUserById(screen.user.id)
                                    if (updatedUser != null) {
                                        currentScreen = Screen.Settings(updatedUser)
                                    }
                                }
                            }
                        )
                    }
                    else -> {
                        // Protección: si se intenta acceder a pantalla no válida con sesión
                        // redirigir a home según rol
                        LaunchedEffect(Unit) {
                            goHome()
                        }
                    }
                }
            }
        }
        } else {
            // Sin usuario: pantallas sin drawer (Login/Register)
    when (currentScreen) {
        Screen.Login -> LoginScreen(
            viewModel = authViewModel,
            onLoginSuccess = {
                        // Navegar según el rol del usuario
                        val user = loginState.loggedInUser
                        if (user != null) {
                            // Guardar sesión
                            sessionManager.saveSession(user.id)
                            
                            currentScreen = if (user.isAdmin()) {
                                Screen.AdminDashboard(user)
                            } else {
                                Screen.UserHome(user)
                            }
                        }
                    },
                    onGoRegister = goRegister
        )
        Screen.Register -> RegisterScreen(
            viewModel = authViewModel,
                    onRegisterSuccess = goLogin,
                    onGoLogin = goLogin
        )
        Screen.Home -> HomeScreen(
            posts = MockData.mockPosts,
                    onGoLogin = goLogin,
                    onGoRegister = goRegister
                )
                else -> {
                    // Pantalla no accesible sin sesión, redirigir a login
                    LaunchedEffect(Unit) {
                currentScreen = Screen.Login
                    }
                }
            }
        }
    }
}

/**
 * Sealed class que define las pantallas de la aplicación.
 * Usa sealed class para pasar datos a las pantallas.
 */
sealed class Screen {
    object Login : Screen()
    object Register : Screen()
    object Home : Screen()
    data class AdminDashboard(val user: com.example.animeverse.data.local.user.UserEntity) : Screen()
    data class UserHome(val user: com.example.animeverse.data.local.user.UserEntity) : Screen()
    data class Settings(val user: com.example.animeverse.data.local.user.UserEntity) : Screen()
    data class ChangePassword(val user: com.example.animeverse.data.local.user.UserEntity) : Screen()
    data class EditProfile(val user: com.example.animeverse.data.local.user.UserEntity) : Screen()
    data class PostDetail(
        val user: com.example.animeverse.data.local.user.UserEntity,
        val postId: Long,
        val authorName: String,
        val title: String,
        val content: String,
        val category: String,
        val imageUri: String?,
        val likes: Int,
        val comments: Int
    ) : Screen()
}


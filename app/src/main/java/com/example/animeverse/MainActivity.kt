package com.example.animeverse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Login) }
    val loginState by authViewModel.loginState.collectAsState()
    val scope = rememberCoroutineScope()
    
    // Estado para lista de usuarios (para admin)
    var allUsers by remember { mutableStateOf<List<com.example.animeverse.data.local.user.UserEntity>>(emptyList()) }
    val context = LocalContext.current
    val database = AppDatabase.getInstance(context)
    
    // Estado del drawer (patrón del profesor)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    
    // Estado del modo oscuro
    var isDarkMode by remember { mutableStateOf(false) }
    
    // Usuario actual según la pantalla
    val currentUser = when (val screen = currentScreen) {
        is Screen.AdminDashboard -> screen.user
        is Screen.UserHome -> screen.user
        is Screen.EditProfile -> screen.user
        else -> null
    }
    
    // Cargar usuarios cuando se necesiten
    LaunchedEffect(currentScreen) {
        if (currentScreen is Screen.AdminDashboard) {
            scope.launch {
                allUsers = database.userDao().getAll()
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
    
    val onLogout: () -> Unit = {
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
                            onEditProfile = {
                                scope.launch { drawerState.close() }
                                goEditProfile()
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
                    AppTopBar(
                        title = when (currentScreen) {
                            is Screen.AdminDashboard -> "Admin Dashboard"
                            is Screen.UserHome -> "AnimeVerse"
                            is Screen.EditProfile -> "Editar Perfil"
                            Screen.Login -> "Iniciar Sesión"
                            Screen.Register -> "Registro"
                            Screen.Home -> "AnimeVerse"
                        },
                        currentUser = currentUser,
                        onOpenDrawer = { scope.launch { drawerState.open() } }
                    )
                }
            ) { _ ->
                when (val screen = currentScreen) {
                    is Screen.AdminDashboard -> AdminDashboard(
                        currentUser = screen.user,
                        allUsers = allUsers,
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
                        }
                    )
                    is Screen.UserHome -> UserHomeScreen(
                        currentUser = screen.user,
                        posts = listOf(
                            MockPost(
                                id = 1,
                                authorName = "María García",
                                title = "¿Cuál es tu anime favorito de 2024?",
                                content = "Hola a todos! Quería saber cuáles han sido sus animes favoritos de este año.",
                                category = "Anime",
                                likes = 15,
                                comments = 8
                            ),
                            MockPost(
                                id = 2,
                                authorName = "Carlos López",
                                title = "Recomendaciones de manga",
                                content = "He leído Death Note y me encantó. ¿Qué otros mangas me recomiendan?",
                                category = "Manga",
                                likes = 12,
                                comments = 5
                            )
                        ),
                        onLogout = onLogout,
                        onViewProfile = goEditProfile
                    )
                    is Screen.EditProfile -> {
                        // Crear ViewModel para editar perfil
                        val editProfileViewModel: EditProfileViewModel = viewModel(
                            factory = EditProfileViewModelFactory(database.userDao())
                        )
                        
                        EditProfileScreenVm(
                            vm = editProfileViewModel,
                            currentUser = screen.user,
                            onBackClick = {
                                // Volver a UserHome con datos actualizados
                                scope.launch {
                                    val updatedUser = database.userDao().getUserById(screen.user.id)
                                    if (updatedUser != null) {
                                        currentScreen = Screen.UserHome(updatedUser)
                                    }
                                }
                            },
                            onProfileUpdated = {
                                // Recargar usuario con datos actualizados
                                scope.launch {
                                    val updatedUser = database.userDao().getUserById(screen.user.id)
                                    if (updatedUser != null) {
                                        currentScreen = Screen.UserHome(updatedUser)
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
    data class EditProfile(val user: com.example.animeverse.data.local.user.UserEntity) : Screen()
}


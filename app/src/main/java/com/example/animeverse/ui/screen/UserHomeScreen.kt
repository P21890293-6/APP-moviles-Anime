package com.example.animeverse.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.animeverse.data.local.user.UserEntity
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

/**
 * Pantalla principal para usuarios públicos.
 * Permite ver posts, crear publicaciones, y navegar por la app.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserHomeScreen(
    currentUser: UserEntity,
    posts: List<MockPost> = emptyList(),
    onLogout: () -> Unit = {},
    onViewProfile: () -> Unit = {},
    onCreatePost: (String, String, Int) -> Unit = { _, _, _ -> },
    onLikePost: (Long) -> Unit = {},
    onCommentPost: (Long) -> Unit = {},
    onReportPost: (Long) -> Unit = {}
) {
    val context = LocalContext.current
    var showCreatePostDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🎌 AnimeVerse",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onViewProfile) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentUser.username.first().uppercase(),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Filled.Logout, "Cerrar sesión")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            if (selectedTab == 0) {
                ExtendedFloatingActionButton(
                    onClick = { showCreatePostDialog = true },
                    icon = { Icon(Icons.Filled.Add, "Crear publicación") },
                    text = { Text("Publicar") }
                )
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Filled.Home, "Inicio") },
                    label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Filled.Explore, "Explorar") },
                    label = { Text("Explorar") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Filled.Person, "Perfil") },
                    label = { Text("Perfil") }
                )
            }
        }
    ) { paddingValues ->
        when (selectedTab) {
            0 -> FeedTab(
                modifier = Modifier.padding(paddingValues),
                currentUser = currentUser,
                posts = posts,
                onLikeClick = onLikePost,
                onCommentClick = onCommentPost,
                onReportClick = onReportPost
            )
            1 -> ExploreTab(modifier = Modifier.padding(paddingValues))
            2 -> ProfileTab(
                modifier = Modifier.padding(paddingValues),
                currentUser = currentUser,
                onLogout = onLogout,
                onEditProfile = onViewProfile
            )
        }
    }
    
    // Dialog para crear nuevo post
    if (showCreatePostDialog) {
        CreatePostDialog(
            onDismiss = { showCreatePostDialog = false },
            onCreatePost = { title, content, themeId ->
                onCreatePost(title, content, themeId)
                Toast.makeText(context, "Publicación creada exitosamente", Toast.LENGTH_SHORT).show()
                showCreatePostDialog = false
            }
        )
    }
}

/**
 * Tab de feed con publicaciones.
 */
@Composable
private fun FeedTab(
    modifier: Modifier = Modifier,
    currentUser: UserEntity,
    posts: List<MockPost>,
    onLikeClick: (Long) -> Unit = {},
    onCommentClick: (Long) -> Unit = {},
    onReportClick: (Long) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Hola, ${currentUser.fullName} 👋",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "¿Qué anime estás viendo hoy?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        if (posts.isEmpty()) {
            item {
                EmptyStateCard()
            }
        } else {
            items(posts) { post ->
                PostCard(
                    post = post,
                    onLikeClick = onLikeClick,
                    onCommentClick = onCommentClick,
                    onReportClick = onReportClick
                )
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(80.dp)) // Space for FAB
        }
    }
}

/**
 * Card de post individual.
 */
@Composable
private fun PostCard(
    post: MockPost,
    onLikeClick: (Long) -> Unit = {},
    onCommentClick: (Long) -> Unit = {},
    onReportClick: (Long) -> Unit = {}
) {
    val context = LocalContext.current
    var showMenu by remember { mutableStateOf(false) }
    
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header del post
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = post.authorName.first().uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = post.authorName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Hace 2 horas • ${post.category}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Filled.MoreVert, "Opciones")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Compartir") },
                            onClick = {
                                showMenu = false
                                Toast.makeText(context, "Compartir publicación", Toast.LENGTH_SHORT).show()
                            },
                            leadingIcon = { Icon(Icons.Filled.Share, null) }
                        )
                        DropdownMenuItem(
                            text = { Text("Guardar") },
                            onClick = {
                                showMenu = false
                                Toast.makeText(context, "Publicación guardada", Toast.LENGTH_SHORT).show()
                            },
                            leadingIcon = { Icon(Icons.Filled.BookmarkBorder, null) }
                        )
                        DropdownMenuItem(
                            text = { Text("Reportar") },
                            onClick = {
                                showMenu = false
                                onReportClick(post.id)
                            },
                            leadingIcon = { Icon(Icons.Filled.Report, null) }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Contenido
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Acciones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilledTonalButton(
                    onClick = { onLikeClick(post.id) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.ThumbUp, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Me gusta (${post.likes})")
                }
                OutlinedButton(
                    onClick = { onCommentClick(post.id) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.Comment, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Comentar (${post.comments})")
                }
            }
        }
    }
}

/**
 * Estado vacío cuando no hay posts.
 */
@Composable
private fun EmptyStateCard() {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.Article,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No hay publicaciones aún",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "¡Sé el primero en compartir algo!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Tab de explorar con categorías.
 */
@Composable
private fun ExploreTab(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Explorar Temas",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        item {
            CategoryCard(
                title = "🎬 Anime",
                description = "Discusiones sobre tus animes favoritos",
                postsCount = 156
            )
        }
        
        item {
            CategoryCard(
                title = "📚 Manga",
                description = "Recomendaciones y reseñas de manga",
                postsCount = 89
            )
        }
        
        item {
            CategoryCard(
                title = "🎮 Gaming",
                description = "Juegos, reviews y gameplays",
                postsCount = 234
            )
        }
        
        item {
            CategoryCard(
                title = "💬 General",
                description = "Conversaciones sobre cultura otaku",
                postsCount = 421
            )
        }
    }
}

/**
 * Card de categoría.
 */
@Composable
private fun CategoryCard(
    title: String,
    description: String,
    postsCount: Int
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = { }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$postsCount publicaciones",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Icon(Icons.Filled.ChevronRight, null)
        }
    }
}

/**
 * Tab de perfil de usuario.
 */
@Composable
private fun ProfileTab(
    modifier: Modifier = Modifier,
    currentUser: UserEntity,
    onLogout: () -> Unit,
    onEditProfile: () -> Unit = {}
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentUser.username.first().uppercase(),
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = currentUser.fullName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "@${currentUser.username}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    AssistChip(
                        onClick = { },
                        label = { Text("Usuario Público") },
                        leadingIcon = {
                            Icon(
                                Icons.Filled.Person,
                                null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    )
                }
            }
        }
        
        item {
            Text(
                text = "Mis Estadísticas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ElevatedCard(modifier = Modifier.weight(1f)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "12",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Posts",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                ElevatedCard(modifier = Modifier.weight(1f)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "48",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Comentarios",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
        
        item {
            Text(
                text = "Configuración",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = onEditProfile  // Conectar con la navegación
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Edit, null)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Editar perfil", modifier = Modifier.weight(1f))
                    Icon(Icons.Filled.ChevronRight, null)
                }
            }
        }
        
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = { }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Settings, null)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Configuración", modifier = Modifier.weight(1f))
                    Icon(Icons.Filled.ChevronRight, null)
                }
            }
        }
        
        item {
            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Icon(Icons.Filled.Logout, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar Sesión")
            }
        }
    }
}

/**
 * Dialog para crear nueva publicación.
 */
@Composable
private fun CreatePostDialog(
    onDismiss: () -> Unit,
    onCreatePost: (String, String, Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedTheme by remember { mutableStateOf(1) } // 1 = Anime por defecto
    var expandedThemeMenu by remember { mutableStateOf(false) }
    
    val themes = mapOf(
        1 to "🎬 Anime",
        2 to "📚 Manga",
        3 to "🎮 Gaming",
        4 to "💬 General"
    )
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Publicación") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Selector de tema
                Text(
                    text = "Categoría",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box {
                    OutlinedButton(
                        onClick = { expandedThemeMenu = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(themes[selectedTheme] ?: "Seleccionar tema")
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(Icons.Filled.ArrowDropDown, null)
                    }
                    DropdownMenu(
                        expanded = expandedThemeMenu,
                        onDismissRequest = { expandedThemeMenu = false }
                    ) {
                        themes.forEach { (themeId, themeName) ->
                            DropdownMenuItem(
                                text = { Text(themeName) },
                                onClick = {
                                    selectedTheme = themeId
                                    expandedThemeMenu = false
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Contenido") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onCreatePost(title, content, selectedTheme) },
                enabled = title.isNotBlank() && content.isNotBlank()
            ) {
                Text("Publicar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

/**
 * Clase de datos para representar posts temporales.
 */
data class MockPost(
    val id: Long,
    val authorName: String,
    val title: String,
    val content: String,
    val category: String,
    val likes: Int,
    val comments: Int
)

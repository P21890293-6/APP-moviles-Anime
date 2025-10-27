package com.example.animeverse.ui.screen

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.animeverse.data.local.user.UserEntity
import java.io.File

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
    onCreatePost: (String, String, Int, String?) -> Unit = { _, _, _, _ -> },
    onLikePost: (Long) -> Unit = {},
    onCommentPost: (Long) -> Unit = {},
    onReportPost: (Long) -> Unit = {},
    onViewPostDetail: (Long) -> Unit = {},
    onOpenDrawer: () -> Unit = {}
) {
    val context = LocalContext.current
    var showCreatePostDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    
    // Filtrar posts según categoría seleccionada
    val filteredPosts = if (selectedCategory != null) {
        posts.filter { it.category == selectedCategory }
    } else {
        posts
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (selectedCategory != null) "Categoría: $selectedCategory" else "🎌 AnimeVerse",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                },
                navigationIcon = {
                    if (selectedCategory != null) {
                        // Mostrar flecha de retroceso cuando hay categoría
                        IconButton(onClick = { 
                            // Limpiar categoría y volver a la pestaña Explorar
                            selectedCategory = null
                            selectedTab = 1
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = Color.White
                            )
                        }
                    } else {
                        // Mostrar menú hamburguesa cuando NO hay categoría
                        IconButton(onClick = onOpenDrawer) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Abrir menú",
                                tint = Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6B4C6D),  // Rosado oscuro/morado
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
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
                }
            )
        },
        floatingActionButton = {
            // Animación del FAB al aparecer/desaparecer
            AnimatedVisibility(
                visible = selectedTab == 0,
                enter = scaleIn(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                ExtendedFloatingActionButton(
                    onClick = { showCreatePostDialog = true },
                    icon = { Icon(Icons.Filled.Add, "Crear publicación") },
                    text = { Text("Publicar") }
                )
            }
        },
        bottomBar = {
            // Ocultar la barra de navegación cuando se selecciona una categoría
            if (selectedCategory == null) {
                NavigationBar(
                    containerColor = Color(0xFF1C1B1F),  // Color oscuro
                    contentColor = Color.White
                ) {
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
        }
    ) { paddingValues ->
        when (selectedTab) {
            0 -> FeedTab(
                modifier = Modifier.padding(paddingValues),
                currentUser = currentUser,
                posts = filteredPosts,
                selectedCategory = selectedCategory,
                onClearFilter = { selectedCategory = null },
                onLikeClick = onLikePost,
                onCommentClick = onCommentPost,
                onReportClick = onReportPost,
                onPostClick = onViewPostDetail
            )
            1 -> ExploreTab(
                modifier = Modifier.padding(paddingValues),
                posts = posts,
                onCategoryClick = { category ->
                    selectedCategory = category
                    selectedTab = 0  // Cambiar a pestaña de inicio
                }
            )
            2 -> ProfileTab(
                modifier = Modifier.padding(paddingValues),
                currentUser = currentUser,
                posts = posts,
                onLogout = onLogout,
                onEditProfile = onViewProfile
            )
        }
    }
    
    // Dialog para crear nuevo post
    if (showCreatePostDialog) {
        CreatePostDialog(
            onDismiss = { showCreatePostDialog = false },
            onCreatePost = { title, content, themeId, imageUri ->
                onCreatePost(title, content, themeId, imageUri)
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
    selectedCategory: String? = null,
    onClearFilter: () -> Unit = {},
    onLikeClick: (Long) -> Unit = {},
    onCommentClick: (Long) -> Unit = {},
    onReportClick: (Long) -> Unit = {},
    onPostClick: (Long) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Mostrar saludo solo cuando NO hay categoría seleccionada
        // Animación del saludo al aparecer/desaparecer
        item {
            AnimatedVisibility(
                visible = selectedCategory == null,
                enter = fadeIn(animationSpec = tween(300)) + expandVertically(),
                exit = fadeOut(animationSpec = tween(200)) + shrinkVertically()
            ) {
                Column {
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
            }
        }
        
        // Animación del encabezado de categoría
        item {
            AnimatedVisibility(
                visible = selectedCategory != null,
                enter = fadeIn(animationSpec = tween(300)) + slideInVertically(
                    initialOffsetY = { -it / 2 }
                ),
                exit = fadeOut(animationSpec = tween(200)) + slideOutVertically(
                    targetOffsetY = { -it / 2 }
                )
            ) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    val categoryIcon = when (selectedCategory) {
                        "Anime" -> "🎬"
                        "Manga" -> "📚"
                        "Gaming" -> "🎮"
                        "General" -> "💬"
                        else -> "📂"
                    }
                    Text(
                        text = "$categoryIcon $selectedCategory",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${posts.size} publicaciones",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
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
                    onReportClick = onReportClick,
                    onPostClick = onPostClick
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
    onReportClick: (Long) -> Unit = {},
    onPostClick: (Long) -> Unit = {}
) {
    val context = LocalContext.current
    var showMenu by remember { mutableStateOf(false) }
    var isLiked by remember { mutableStateOf(false) }
    
    // Animación de escala para el botón de like
    val likeScale by animateFloatAsState(
        targetValue = if (isLiked) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "likeScale"
    )
    
    // Animación de entrada del card
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(300)) + slideInVertically(
            initialOffsetY = { it / 4 },
            animationSpec = tween(400, easing = FastOutSlowInEasing)
        )
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onPostClick(post.id) }
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
            
            // Mostrar imagen si existe
            if (post.imageUri != null) {
                Spacer(modifier = Modifier.height(12.dp))
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(Uri.parse(post.imageUri))
                        .crossfade(true)
                        .build(),
                    contentDescription = "Imagen de ${post.title}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 150.dp, max = 400.dp)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Acciones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilledTonalButton(
                    onClick = {
                        isLiked = !isLiked
                        onLikeClick(post.id)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Filled.ThumbUp, 
                        null, 
                        modifier = Modifier
                            .size(16.dp)
                            .scale(likeScale)
                    )
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
private fun ExploreTab(
    modifier: Modifier = Modifier,
    posts: List<MockPost> = emptyList(),
    onCategoryClick: (String) -> Unit = {}
) {
    // Contar posts por categoría
    val animeCount = posts.count { it.category == "Anime" }
    val mangaCount = posts.count { it.category == "Manga" }
    val gamingCount = posts.count { it.category == "Gaming" }
    val generalCount = posts.count { it.category == "General" }
    
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
                postsCount = animeCount,
                onClick = { onCategoryClick("Anime") }
            )
        }
        
        item {
            CategoryCard(
                title = "📚 Manga",
                description = "Recomendaciones y reseñas de manga",
                postsCount = mangaCount,
                onClick = { onCategoryClick("Manga") }
            )
        }
        
        item {
            CategoryCard(
                title = "🎮 Gaming",
                description = "Juegos, reviews y gameplays",
                postsCount = gamingCount,
                onClick = { onCategoryClick("Gaming") }
            )
        }
        
        item {
            CategoryCard(
                title = "💬 General",
                description = "Conversaciones sobre cultura otaku",
                postsCount = generalCount,
                onClick = { onCategoryClick("General") }
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
    postsCount: Int,
    onClick: () -> Unit = {}
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
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
    posts: List<MockPost> = emptyList(),
    onLogout: () -> Unit,
    onEditProfile: () -> Unit = {}
) {
    // Calcular estadísticas reales del usuario
    val userPostsCount = posts.count { it.authorName == currentUser.fullName }
    val userCommentsCount = posts.filter { it.authorName == currentUser.fullName }
        .sumOf { it.comments }
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
                            text = userPostsCount.toString(),
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
                            text = userCommentsCount.toString(),
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
                onClick = onEditProfile  // Navegar a editar perfil
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
    onCreatePost: (String, String, Int, String?) -> Unit
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedTheme by remember { mutableStateOf(1) } // 1 = Anime por defecto
    var expandedThemeMenu by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<String?>(null) }
    var showImageOptions by remember { mutableStateOf(false) }
    var showCameraPermissionDialog by remember { mutableStateOf(false) }
    var showGalleryPermissionDialog by remember { mutableStateOf(false) }
    
    val themes = mapOf(
        1 to "🎬 Anime",
        2 to "📚 Manga",
        3 to "🎮 Gaming",
        4 to "💬 General"
    )
    
    // Variable para almacenar el URI temporal de la cámara
    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }
    
    // Launcher para captura de imagen
    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempPhotoUri != null) {
            imageUri = tempPhotoUri.toString()
        }
    }
    
    val pickImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { imageUri = it.toString() }
    }
    
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            try {
                // Crear directorio si no existe
                val cacheDir = File(context.cacheDir, "images").apply { 
                    if (!exists()) mkdirs() 
                }
                
                // Crear archivo temporal
                val photoFile = File(cacheDir, "post_${System.currentTimeMillis()}.jpg")
                
                // Crear URI con FileProvider
                val photoUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    photoFile
                )
                
                tempPhotoUri = photoUri
                takePictureLauncher.launch(photoUri)
            } catch (e: Exception) {
                e.printStackTrace()
                android.widget.Toast.makeText(
                    context,
                    "Error al abrir cámara: ${e.message}",
                    android.widget.Toast.LENGTH_LONG
                ).show()
            }
        } else {
            showCameraPermissionDialog = true
        }
    }
    
    val galleryPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            pickImageLauncher.launch("image/*")
        } else {
            showGalleryPermissionDialog = true
        }
    }
    
    // Diálogos de permisos
    if (showCameraPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showCameraPermissionDialog = false },
            title = { Text("Permiso de cámara requerido") },
            text = { Text("Para tomar fotos necesitas otorgar el permiso de cámara.") },
            confirmButton = {
                TextButton(onClick = { showCameraPermissionDialog = false }) {
                    Text("Entendido")
                }
            }
        )
    }
    
    if (showGalleryPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showGalleryPermissionDialog = false },
            title = { Text("Permiso de galería requerido") },
            text = { Text("Para seleccionar fotos necesitas otorgar el permiso de galería.") },
            confirmButton = {
                TextButton(onClick = { showGalleryPermissionDialog = false }) {
                    Text("Entendido")
                }
            }
        )
    }
    
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                // Título
                Text(
                    text = "Nueva Publicación",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
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
                
                // Imagen (opcional)
                Text(
                    text = "Imagen (opcional)",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                if (imageUri != null) {
                    // Mostrar imagen seleccionada
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(Uri.parse(imageUri))
                                .crossfade(true)
                                .build(),
                            contentDescription = "Imagen de la publicación",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        IconButton(
                            onClick = { imageUri = null },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .background(
                                    MaterialTheme.colorScheme.errorContainer,
                                    CircleShape
                                )
                        ) {
                            Icon(
                                Icons.Filled.Close,
                                "Eliminar imagen",
                                tint = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                } else {
                    // Botones para agregar imagen
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                val permission = Manifest.permission.CAMERA
                                when (ContextCompat.checkSelfPermission(context, permission)) {
                                    android.content.pm.PackageManager.PERMISSION_GRANTED -> {
                                        try {
                                            // Crear directorio si no existe
                                            val cacheDir = File(context.cacheDir, "images").apply { 
                                                if (!exists()) mkdirs() 
                                            }
                                            
                                            // Crear archivo temporal
                                            val photoFile = File(cacheDir, "post_${System.currentTimeMillis()}.jpg")
                                            
                                            // Crear URI con FileProvider
                                            val photoUri = FileProvider.getUriForFile(
                                                context,
                                                "${context.packageName}.fileprovider",
                                                photoFile
                                            )
                                            
                                            tempPhotoUri = photoUri
                                            takePictureLauncher.launch(photoUri)
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                            android.widget.Toast.makeText(
                                                context,
                                                "Error al abrir cámara: ${e.message}",
                                                android.widget.Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                                    else -> cameraPermissionLauncher.launch(permission)
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Filled.CameraAlt, null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Cámara", style = MaterialTheme.typography.labelMedium)
                        }
                        
                        OutlinedButton(
                            onClick = {
                                val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    Manifest.permission.READ_MEDIA_IMAGES
                                } else {
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                }
                                when (ContextCompat.checkSelfPermission(context, permission)) {
                                    android.content.pm.PackageManager.PERMISSION_GRANTED -> {
                                        pickImageLauncher.launch("image/*")
                                    }
                                    else -> galleryPermissionLauncher.launch(permission)
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Filled.Image, null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Galería", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Contenido") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
                    )
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Botones de acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }
                    Button(
                        onClick = { onCreatePost(title, content, selectedTheme, imageUri) },
                        enabled = title.isNotBlank() && content.isNotBlank(),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Publicar")
                    }
                }
            }
        }
    }
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
    val imageUri: String? = null,
    val likes: Int,
    val comments: Int
)


package com.example.animeverse.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import coil.compose.AsyncImage
import coil.request.ImageRequest

/**
 * Pantalla de detalle de publicación.
 * Muestra la información completa del post, incluyendo la imagen en tamaño grande.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    postId: Long,
    authorName: String,
    title: String,
    content: String,
    category: String,
    imageUri: String?,
    likes: Int,
    comments: Int,
    onBackClick: () -> Unit = {},
    onLikeClick: () -> Unit = {},
    onCommentClick: () -> Unit = {},
    onShareClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var isLiked by remember { mutableStateOf(false) }
    var currentLikes by remember { mutableStateOf(likes) }
    
    // Animación del like
    val likeScale by animateFloatAsState(
        targetValue = if (isLiked) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "likeScale"
    )
    
    // Animación de entrada
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de publicación") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6B4C6D),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(300)) + slideInVertically(
                initialOffsetY = { it / 4 }
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header del post
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = authorName.first().uppercase(),
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = authorName,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Hace 2 horas",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Categoría
                        FilterChip(
                            selected = false,
                            onClick = {},
                            label = { Text(category) },
                            leadingIcon = {
                                Icon(
                                    imageVector = when (category) {
                                        "Anime" -> Icons.Filled.PlayArrow
                                        "Manga" -> Icons.Filled.MenuBook
                                        "Gaming" -> Icons.Filled.SportsEsports
                                        else -> Icons.Filled.Lightbulb
                                    },
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        )
                    }
                }
                
                // Título y contenido
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = content,
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
                    )
                }
                
                // Imagen (si existe)
                imageUri?.let { uri ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(uri)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Imagen de la publicación",
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 500.dp),
                            contentScale = ContentScale.FillWidth
                        )
                    }
                }
                
                // Botones de acción
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Like button
                        IconButton(
                            onClick = {
                                isLiked = !isLiked
                                currentLikes = if (isLiked) currentLikes + 1 else currentLikes - 1
                                onLikeClick()
                            },
                            modifier = Modifier.scale(likeScale)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                    contentDescription = "Me gusta",
                                    tint = if (isLiked) Color(0xFFE91E63) else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "$currentLikes",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                        
                        // Comment button
                        IconButton(onClick = onCommentClick) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Filled.Comment,
                                    contentDescription = "Comentarios"
                                )
                                Text(
                                    text = "$comments",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                        
                        // Share button
                        IconButton(onClick = onShareClick) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Filled.Share,
                                    contentDescription = "Compartir"
                                )
                                Text(
                                    text = "Compartir",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                    }
                }
                
                // Espacio inferior
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}


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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.animeverse.data.local.user.UserEntity

/**
 * Panel de Administrador con funcionalidades avanzadas.
 * Solo accesible para usuarios con rol ADMIN.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboard(
    currentUser: UserEntity,
    allUsers: List<UserEntity> = emptyList(),
    allPosts: List<com.example.animeverse.data.local.post.PostEntity> = emptyList(),
    allReports: List<com.example.animeverse.data.local.post.PostReportEntity> = emptyList(),
    onLogout: () -> Unit = {},
    onManageUsers: () -> Unit = {},
    onManagePosts: () -> Unit = {},
    onManageReports: () -> Unit = {},
    onViewStats: () -> Unit = {},
    onBanUser: (Long, Boolean) -> Unit = { _, _ -> },  // Callback para banear/desbanear
    onDeleteUser: (Long) -> Unit = {},  // Callback para eliminar usuario
    onDeletePost: (Long) -> Unit = {},  // Callback para eliminar publicación
    onDismissReport: (Long) -> Unit = {},  // Callback para descartar reporte
    onDeleteReportedPost: (Long, Long) -> Unit = { _, _ -> }  // Callback para eliminar post reportado (reportId, postId)
) {
    var selectedTab by remember { mutableStateOf(0) }
    
    // Funciones para cambiar de pestaña
    val navigateToUsers = { selectedTab = 1 }
    val navigateToPosts = { selectedTab = 3 }
    val navigateToStats = { selectedTab = 4 }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Panel de Administrador",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Bienvenido, ${currentUser.fullName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Tabs de navegación
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Panel de Control") },
                    icon = { Icon(Icons.Filled.Dashboard, null) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Usuarios") },
                    icon = { Icon(Icons.Filled.People, null) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Reportes") },
                    icon = { Icon(Icons.Filled.Report, null) }
                )
                Tab(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    text = { Text("Posts") },
                    icon = { Icon(Icons.Filled.Article, null) }
                )
                Tab(
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    text = { Text("Estadísticas") },
                    icon = { Icon(Icons.Filled.BarChart, null) }
                )
            }
            
            // Contenido según tab seleccionado
            when (selectedTab) {
                0 -> DashboardTab(
                    totalUsers = allUsers.size,
                    totalPosts = allPosts.size,
                    onManageUsers = navigateToUsers,  // Navega a la pestaña de usuarios
                    onManagePosts = navigateToPosts,  // Navega a la pestaña de posts
                    onViewStats = navigateToStats  // Navega a la pestaña de estadísticas
                )
                1 -> UsersTab(
                    users = allUsers, 
                    onBanUser = onBanUser,
                    onDeleteUser = onDeleteUser
                )
                2 -> ReportsTab(
                    reports = allReports,
                    onDismissReport = onDismissReport,
                    onDeletePost = onDeleteReportedPost
                )
                3 -> PostsTab(
                    posts = allPosts,
                    onDeletePost = onDeletePost
                )
                4 -> StatsTab(
                    totalUsers = allUsers.size,
                    totalPosts = allPosts.size,
                    users = allUsers,
                    posts = allPosts
                )
            }
        }
    }
}

// Variable auxiliar para guardar usuario a banear
private data class BanDialogState(
    val userId: Long = 0,
    val userName: String = "",
    val currentlyBanned: Boolean = false
)

/**
 * Tab principal del dashboard con estadísticas y acciones rápidas.
 */
@Composable
private fun DashboardTab(
    totalUsers: Int,
    totalPosts: Int,
    onManageUsers: () -> Unit,
    onManagePosts: () -> Unit,
    onViewStats: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "📊 Estadísticas Generales",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        
        // Cards de estadísticas
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Usuarios",
                    value = totalUsers.toString(),
                    icon = Icons.Filled.People,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Posts",
                    value = totalPosts.toString(),
                    icon = Icons.Filled.Article,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Reportes",
                    value = "3",
                    icon = Icons.Filled.Report,
                    modifier = Modifier.weight(1f),
                    color = Color(0xFF8B2C2C)
                )
                StatCard(
                    title = "Comentarios",
                    value = "156",
                    icon = Icons.Filled.Comment,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "⚡ Acciones Rápidas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        
        // Acciones rápidas
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = onManageUsers
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.ManageAccounts,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Gestionar Usuarios",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Ver, editar y banear usuarios",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Icon(Icons.Filled.ChevronRight, null)
                }
            }
        }
        
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = onManagePosts
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Article,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Gestionar Publicaciones",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Moderar y eliminar contenido",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Icon(Icons.Filled.ChevronRight, null)
                }
            }
        }
        
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = onViewStats
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Analytics,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Ver Estadísticas",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Análisis detallado de la plataforma",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Icon(Icons.Filled.ChevronRight, null)
                }
            }
        }
    }
}

/**
 * Card de estadística individual.
 */
@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    color: androidx.compose.ui.graphics.Color = Color(0xFF2C2C2E)
) {
    ElevatedCard(
        modifier = modifier,
        colors = CardDefaults.elevatedCardColors(
            containerColor = color
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

/**
 * Tab de gestión de usuarios.
 */
@Composable
private fun UsersTab(
    users: List<UserEntity>,
    onBanUser: (Long, Boolean) -> Unit,
    onDeleteUser: (Long) -> Unit = {}
) {
    // Estado para dialog de confirmación de baneo
    var showBanDialog by remember { mutableStateOf(false) }
    var banDialogState by remember { mutableStateOf(BanDialogState()) }
    
    // Estado para dialog de ver perfil
    var showProfileDialog by remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf<UserEntity?>(null) }
    
    // Estado para dialog de eliminar
    var showDeleteDialog by remember { mutableStateOf(false) }
    var userToDelete by remember { mutableStateOf<UserEntity?>(null) }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "👥 Gestión de Usuarios",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        items(users) { user ->
            UserCard(
                user = user,
                onBanClick = {
                    banDialogState = BanDialogState(
                        userId = user.id,
                        userName = user.fullName,
                        currentlyBanned = user.banned
                    )
                    showBanDialog = true
                },
                onViewProfile = {
                    selectedUser = user
                    showProfileDialog = true
                },
                onDeleteUser = {
                    userToDelete = user
                    showDeleteDialog = true
                }
            )
        }
        
        if (users.isEmpty()) {
            item {
                Text(
                    text = "No hay usuarios registrados",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
    
    // Dialog de confirmación de baneo
    if (showBanDialog) {
        BanUserDialog(
            userName = banDialogState.userName,
            currentlyBanned = banDialogState.currentlyBanned,
            onConfirm = {
                onBanUser(banDialogState.userId, !banDialogState.currentlyBanned)
                showBanDialog = false
            },
            onDismiss = { showBanDialog = false }
        )
    }
    
    // Dialog de ver perfil
    if (showProfileDialog && selectedUser != null) {
        ViewProfileDialog(
            user = selectedUser!!,
            onDismiss = { showProfileDialog = false }
        )
    }
    
    // Dialog de confirmar eliminación
    if (showDeleteDialog && userToDelete != null) {
        DeleteUserDialog(
            userName = userToDelete!!.fullName,
            onConfirm = {
                onDeleteUser(userToDelete!!.id)
                showDeleteDialog = false
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
}

/**
 * Card individual de usuario.
 */
@Composable
private fun UserCard(
    user: UserEntity,
    onBanClick: () -> Unit = {},
    onViewProfile: () -> Unit = {},
    onDeleteUser: () -> Unit = {}
) {
    var showMenu by remember { mutableStateOf(false) }
    
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.username.first().uppercase(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Info del usuario
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.fullName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "@${user.username}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Badges de rol y estado
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                AssistChip(
                    onClick = { },
                    label = {
                        Text(
                            text = if (user.role == "ADMIN") "Admin" else "Usuario",
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = if (user.role == "ADMIN") 
                                Icons.Filled.Shield 
                            else 
                                Icons.Filled.Person,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (user.role == "ADMIN")
                            MaterialTheme.colorScheme.errorContainer
                        else
                            MaterialTheme.colorScheme.secondaryContainer
                    )
                )
                
                // Badge de baneado si aplica
                if (user.banned) {
                    AssistChip(
                        onClick = { },
                        label = {
                            Text(
                                text = "Baneado",
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Block,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            labelColor = MaterialTheme.colorScheme.onError,
                            leadingIconContentColor = MaterialTheme.colorScheme.onError
                        )
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Menú de opciones
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(Icons.Filled.MoreVert, "Opciones")
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Ver perfil") },
                        onClick = {
                            showMenu = false
                            onViewProfile()
                        },
                        leadingIcon = { Icon(Icons.Filled.Visibility, null) }
                    )
                    if (user.role != "ADMIN") {
                        DropdownMenuItem(
                            text = { Text(if (user.banned) "Desbanear usuario" else "Banear usuario") },
                            onClick = {
                                showMenu = false
                                onBanClick()
                            },
                            leadingIcon = { Icon(Icons.Filled.Block, null) }
                        )
                        DropdownMenuItem(
                            text = { Text("Eliminar") },
                            onClick = {
                                showMenu = false
                                onDeleteUser()
                            },
                            leadingIcon = { Icon(Icons.Filled.Delete, null) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Tab de reportes y moderación.
 */
@Composable
private fun ReportsTab(
    reports: List<com.example.animeverse.data.local.post.PostReportEntity> = emptyList(),
    onDismissReport: (Long) -> Unit = {},
    onDeletePost: (Long, Long) -> Unit = { _, _ -> }
) {
    if (reports.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.Report,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No hay reportes pendientes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Los reportes de usuarios aparecerán aquí",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "🚩 Reportes Pendientes (${reports.size})",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            items(reports) { report ->
                ReportCard(
                    report = report,
                    onDismiss = { onDismissReport(report.id) },
                    onDeletePost = { onDeletePost(report.id, report.postId) }
                )
            }
        }
    }
}

/**
 * Card individual de reporte.
 */
@Composable
private fun ReportCard(
    report: com.example.animeverse.data.local.post.PostReportEntity,
    onDismiss: () -> Unit = {},
    onDeletePost: () -> Unit = {}
) {
    var showMenu by remember { mutableStateOf(false) }
    
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Report,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Reporte",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
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
                            text = { Text("Descartar reporte") },
                            onClick = {
                                showMenu = false
                                onDismiss()
                            },
                            leadingIcon = { Icon(Icons.Filled.CheckCircle, null) }
                        )
                        DropdownMenuItem(
                            text = { Text("Eliminar publicación", color = MaterialTheme.colorScheme.error) },
                            onClick = {
                                showMenu = false
                                onDeletePost()
                            },
                            leadingIcon = { 
                                Icon(
                                    Icons.Filled.Delete, 
                                    null,
                                    tint = MaterialTheme.colorScheme.error
                                ) 
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))
            
            // Info del post reportado
            Text(
                text = "Publicación reportada:",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "\"${report.postTitle}\"",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Por: ${report.postAuthorName}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Info del reporte
            Text(
                text = "Reportado por: ${report.reportedByName}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Razón: ${report.reason}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            val dateFormat = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
            val date = dateFormat.format(java.util.Date(report.reportedAt))
            Text(
                text = "Fecha: $date",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Dialog de confirmación para banear/desbanear usuario.
 */
@Composable
private fun BanUserDialog(
    userName: String,
    currentlyBanned: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (currentlyBanned) "Desbanear Usuario" else "Banear Usuario")
        },
        text = {
            Text(
                if (currentlyBanned)
                    "¿Deseas desbanear a $userName? Podrá volver a acceder a la aplicación."
                else
                    "¿Estás seguro de banear a $userName? No podrá iniciar sesión hasta que lo desbanees."
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentlyBanned)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error
                )
            ) {
                Text(if (currentlyBanned) "Desbanear" else "Banear")
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
 * Dialog para ver el perfil completo del usuario.
 */
@Composable
private fun ViewProfileDialog(
    user: UserEntity,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Filled.Person, "Perfil")
                Text("Perfil de Usuario")
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Nombre completo
                InfoRow("Nombre:", user.fullName)
                HorizontalDivider()
                
                // Username
                InfoRow("Usuario:", "@${user.username}")
                HorizontalDivider()
                
                // Email
                InfoRow("Email:", user.email)
                HorizontalDivider()
                
                // Rol
                InfoRow(
                    "Rol:", 
                    if (user.role == "ADMIN") "Administrador" else "Usuario"
                )
                HorizontalDivider()
                
                // Estado
                InfoRow(
                    "Estado:", 
                    if (user.banned) "🚫 Baneado" else "✅ Activo"
                )
                HorizontalDivider()
                
                // Fecha de registro
                val dateFormat = java.text.SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault())
                val date = dateFormat.format(java.util.Date(user.createdAt))
                InfoRow("Registro:", date)
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}

/**
 * Fila de información para el diálogo de perfil.
 */
@Composable
private fun InfoRow(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal
        )
    }
}

/**
 * Dialog de confirmación para eliminar usuario.
 */
@Composable
private fun DeleteUserDialog(
    userName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Filled.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(48.dp)
            )
        },
        title = {
            Text(
                "⚠️ Eliminar Usuario",
                color = MaterialTheme.colorScheme.error
            )
        },
        text = {
            Text(
                "¿Estás seguro de eliminar a $userName?\n\n" +
                "Esta acción es permanente y eliminará:\n" +
                "• El usuario y su información\n" +
                "• Todas sus publicaciones\n" +
                "• Sus comentarios y calificaciones\n\n" +
                "Esta acción NO se puede deshacer."
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Eliminar")
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
 * Tab de gestión de publicaciones.
 */
@Composable
private fun PostsTab(
    posts: List<com.example.animeverse.data.local.post.PostEntity>,
    onDeletePost: (Long) -> Unit
) {
    // Estado para dialog de eliminar post
    var showDeleteDialog by remember { mutableStateOf(false) }
    var postToDelete by remember { mutableStateOf<com.example.animeverse.data.local.post.PostEntity?>(null) }
    
    // Estado para dialog de ver post completo
    var showViewDialog by remember { mutableStateOf(false) }
    var selectedPost by remember { mutableStateOf<com.example.animeverse.data.local.post.PostEntity?>(null) }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "📝 Gestión de Publicaciones",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        items(posts) { post ->
            PostCard(
                post = post,
                onViewClick = {
                    selectedPost = post
                    showViewDialog = true
                },
                onDeleteClick = {
                    postToDelete = post
                    showDeleteDialog = true
                }
            )
        }
        
        if (posts.isEmpty()) {
            item {
                Text(
                    text = "No hay publicaciones",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
    
    // Dialog de ver post completo
    if (showViewDialog && selectedPost != null) {
        ViewPostDialog(
            post = selectedPost!!,
            onDismiss = { showViewDialog = false }
        )
    }
    
    // Dialog de confirmar eliminación
    if (showDeleteDialog && postToDelete != null) {
        DeletePostDialog(
            postTitle = postToDelete!!.title,
            onConfirm = {
                onDeletePost(postToDelete!!.id)
                showDeleteDialog = false
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
}

/**
 * Card individual de publicación.
 */
@Composable
private fun PostCard(
    post: com.example.animeverse.data.local.post.PostEntity,
    onViewClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    var showMenu by remember { mutableStateOf(false) }
    
    val themeName = when (post.themeId) {
        1 -> "Anime"
        2 -> "Manga"
        3 -> "Gaming"
        else -> "General"
    }
    
    val date = java.text.SimpleDateFormat("dd-MM-yyyy HH:mm", java.util.Locale.getDefault())
        .format(java.util.Date(post.createdAt))
    
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header con autor y menú
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = post.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Por ${post.authorName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        AssistChip(
                            onClick = { },
                            label = {
                                Text(
                                    text = themeName,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            modifier = Modifier.height(24.dp)
                        )
                    }
                }
                
                // Menú de opciones
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Filled.MoreVert, "Opciones")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Ver completo") },
                            onClick = {
                                showMenu = false
                                onViewClick()
                            },
                            leadingIcon = { Icon(Icons.Filled.Visibility, null) }
                        )
                        DropdownMenuItem(
                            text = { Text("Eliminar") },
                            onClick = {
                                showMenu = false
                                onDeleteClick()
                            },
                            leadingIcon = { Icon(Icons.Filled.Delete, null) }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Contenido (preview)
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Stats
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = post.likes.toString(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Comment,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = post.comments.toString(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Dialog para ver el post completo.
 */
@Composable
private fun ViewPostDialog(
    post: com.example.animeverse.data.local.post.PostEntity,
    onDismiss: () -> Unit
) {
    val themeName = when (post.themeId) {
        1 -> "Anime"
        2 -> "Manga"
        3 -> "Gaming"
        else -> "General"
    }
    
    val date = java.text.SimpleDateFormat("dd-MM-yyyy HH:mm", java.util.Locale.getDefault())
        .format(java.util.Date(post.createdAt))
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(post.title)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Por ${post.authorName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    AssistChip(
                        onClick = { },
                        label = {
                            Text(
                                text = themeName,
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        modifier = Modifier.height(24.dp)
                    )
                }
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = post.content,
                    style = MaterialTheme.typography.bodyMedium
                )
                HorizontalDivider()
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Filled.Favorite, null, modifier = Modifier.size(16.dp))
                        Text("${post.likes} likes", style = MaterialTheme.typography.bodySmall)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Filled.Comment, null, modifier = Modifier.size(16.dp))
                        Text("${post.comments} comentarios", style = MaterialTheme.typography.bodySmall)
                    }
                }
                Text(
                    text = "Publicado: $date",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}

/**
 * Dialog de confirmación para eliminar publicación.
 */
@Composable
private fun DeletePostDialog(
    postTitle: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Filled.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(48.dp)
            )
        },
        title = {
            Text(
                "⚠️ Eliminar Publicación",
                color = MaterialTheme.colorScheme.error
            )
        },
        text = {
            Text(
                "¿Estás seguro de eliminar la publicación \"$postTitle\"?\n\n" +
                "Esta acción es permanente y eliminará:\n" +
                "• La publicación y su contenido\n" +
                "• Todos los comentarios asociados\n" +
                "• Las calificaciones recibidas\n\n" +
                "Esta acción NO se puede deshacer."
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Eliminar")
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
 * Tab de estadísticas detalladas.
 */
@Composable
private fun StatsTab(
    totalUsers: Int,
    totalPosts: Int,
    users: List<UserEntity>,
    posts: List<com.example.animeverse.data.local.post.PostEntity>
) {
    // Calcular estadísticas
    val activeUsers = users.count { !it.banned }
    val bannedUsers = users.count { it.banned }
    val adminUsers = users.count { it.role == "ADMIN" }
    
    val postsByTheme = posts.groupBy { it.themeId }.mapValues { it.value.size }
    val totalLikes = posts.sumOf { it.likes }
    val totalComments = posts.sumOf { it.comments }
    
    val mostActiveUser = posts.groupBy { it.authorId }
        .maxByOrNull { it.value.size }
        ?.let { entry ->
            users.find { it.id == entry.key }?.fullName to entry.value.size
        }
    
    val mostPopularPost = posts.maxByOrNull { it.likes }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "📈 Estadísticas Detalladas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        
        // Sección: Estadísticas de Usuarios
        item {
            Text(
                text = "👥 Usuarios",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
        
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DetailStatCard(
                    title = "Total",
                    value = totalUsers.toString(),
                    icon = Icons.Filled.People,
                    modifier = Modifier.weight(1f)
                )
                DetailStatCard(
                    title = "Activos",
                    value = activeUsers.toString(),
                    icon = Icons.Filled.CheckCircle,
                    color = Color(0xFF2C5C2C),
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DetailStatCard(
                    title = "Baneados",
                    value = bannedUsers.toString(),
                    icon = Icons.Filled.Block,
                    color = Color(0xFF8B2C2C),
                    modifier = Modifier.weight(1f)
                )
                DetailStatCard(
                    title = "Admins",
                    value = adminUsers.toString(),
                    icon = Icons.Filled.Shield,
                    color = Color(0xFF3A3A7C),
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        // Sección: Estadísticas de Publicaciones
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "📝 Publicaciones",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
        
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DetailStatCard(
                    title = "Total Posts",
                    value = totalPosts.toString(),
                    icon = Icons.Filled.Article,
                    modifier = Modifier.weight(1f)
                )
                DetailStatCard(
                    title = "Total Likes",
                    value = totalLikes.toString(),
                    icon = Icons.Filled.Favorite,
                    color = Color(0xFF7C3A3A),
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        item {
            DetailStatCard(
                title = "Total Comentarios",
                value = totalComments.toString(),
                icon = Icons.Filled.Comment,
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        // Posts por categoría
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "📊 Posts por Categoría",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
        
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CategoryStatRow("🎌 Anime", postsByTheme[1] ?: 0, totalPosts)
                    CategoryStatRow("📚 Manga", postsByTheme[2] ?: 0, totalPosts)
                    CategoryStatRow("🎮 Gaming", postsByTheme[3] ?: 0, totalPosts)
                    CategoryStatRow("💬 General", postsByTheme[4] ?: 0, totalPosts)
                }
            }
        }
        
        // Usuario más activo
        if (mostActiveUser != null) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "🏆 Usuario Más Activo",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = mostActiveUser.first ?: "Desconocido",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${mostActiveUser.second} publicaciones",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
        
        // Post más popular
        if (mostPopularPost != null) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "❤️ Post Más Popular",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = mostPopularPost.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Por ${mostPopularPost.authorName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(
                                    Icons.Filled.Favorite,
                                    null,
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    "${mostPopularPost.likes} likes",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(
                                    Icons.Filled.Comment,
                                    null,
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    "${mostPopularPost.comments} comentarios",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
        
        // Espacio final
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * Card de estadística detallada.
 */
@Composable
private fun DetailStatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: androidx.compose.ui.graphics.Color = Color(0xFF2C2C2E),
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
        colors = CardDefaults.elevatedCardColors(
            containerColor = color
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

/**
 * Fila de estadística de categoría con barra de progreso.
 */
@Composable
private fun CategoryStatRow(
    category: String,
    count: Int,
    total: Int
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = category,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "$count posts",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = if (total > 0) count.toFloat() / total.toFloat() else 0f,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(MaterialTheme.shapes.small)
        )
    }
}

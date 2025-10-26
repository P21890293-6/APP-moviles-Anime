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

/**
 * Panel de Administrador con funcionalidades avanzadas.
 * Solo accesible para usuarios con rol ADMIN.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboard(
    currentUser: UserEntity,
    allUsers: List<UserEntity> = emptyList(),
    onLogout: () -> Unit = {},
    onManageUsers: () -> Unit = {},
    onManagePosts: () -> Unit = {},
    onManageReports: () -> Unit = {},
    onViewStats: () -> Unit = {},
    onBanUser: (Long, Boolean) -> Unit = { _, _ -> },  // Callback para banear/desbanear
    onDeleteUser: (Long) -> Unit = {}  // Callback para eliminar usuario
) {
    var selectedTab by remember { mutableStateOf(0) }
    
    // Función para cambiar a la pestaña de usuarios
    val navigateToUsers = { selectedTab = 1 }
    
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
                    text = { Text("Dashboard") },
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
            }
            
            // Contenido según tab seleccionado
            when (selectedTab) {
                0 -> DashboardTab(
                    totalUsers = allUsers.size,
                    onManageUsers = navigateToUsers,  // Ahora navega a la pestaña de usuarios
                    onManagePosts = onManagePosts,
                    onViewStats = onViewStats
                )
                1 -> UsersTab(
                    users = allUsers, 
                    onBanUser = onBanUser,
                    onDeleteUser = onDeleteUser
                )
                2 -> ReportsTab()
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
                    value = "24",
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
                    color = MaterialTheme.colorScheme.errorContainer
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
    color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primaryContainer
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
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
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
private fun ReportsTab() {
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
                val date = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                    .format(java.util.Date(user.createdAt))
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


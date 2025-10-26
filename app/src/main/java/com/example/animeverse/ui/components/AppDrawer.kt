package com.example.animeverse.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.animeverse.data.local.user.UserEntity

// Data class para representar cada opción del drawer (patrón del profesor)
data class DrawerItem(
    val label: String,                  // Texto a mostrar
    val icon: ImageVector,              // Ícono del ítem
    val onClick: () -> Unit             // Acción al hacer click
)

@Composable // Componente Drawer para usar en ModalNavigationDrawer
fun AppDrawer(
    currentUser: UserEntity?,           // Usuario actual logueado (null si no hay sesión)
    items: List<DrawerItem>,            // Lista de ítems a mostrar
    onLogout: () -> Unit = {},          // Acción para cerrar sesión
    isDarkMode: Boolean = false,        // Estado del modo oscuro
    onToggleDarkMode: (Boolean) -> Unit = {}, // Callback para cambiar tema
    modifier: Modifier = Modifier       // Modificador opcional
) {
    ModalDrawerSheet(                   // Hoja que contiene el contenido del drawer
        modifier = modifier
    ) {
        // Header del drawer con info del usuario
        if (currentUser != null) {
            // Usuario logueado: mostrar info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentUser.username.first().uppercase(),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(Modifier.height(12.dp))
                
                Text(
                    text = currentUser.fullName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                
                Text(
                    text = "@${currentUser.username}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
                
                Spacer(Modifier.height(8.dp))
                
                // Badge de rol
                AssistChip(
                    onClick = { },
                    label = {
                        Text(
                            text = if (currentUser.role == "ADMIN") "Administrador" else "Usuario",
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = if (currentUser.role == "ADMIN") 
                                Icons.Filled.Shield 
                            else 
                                Icons.Filled.Person,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
            }
        } else {
            // Sin usuario: header simple
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🎌 AnimeVerse",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Comunidad Anime",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }
        
        Spacer(Modifier.height(8.dp))
        
        // Recorremos las opciones y pintamos ítems
        items.forEach { item ->                         // Por cada ítem
            NavigationDrawerItem(                       // Ítem con estados Material
                label = { Text(item.label) },           // Texto visible
                selected = false,                       // Puedes usar currentRoute == ... si quieres marcar
                onClick = item.onClick,                 // Acción al pulsar
                icon = { Icon(item.icon, contentDescription = item.label) }, // Ícono
                modifier = Modifier.padding(horizontal = 12.dp),
                colors = NavigationDrawerItemDefaults.colors()
            )
        }
        
        // Opciones al final del drawer
        Spacer(Modifier.weight(1f))
        
        HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp))
        
        // Opción de modo oscuro
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = if (isDarkMode) Icons.Filled.DarkMode else Icons.Filled.LightMode,
                    contentDescription = "Tema",
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (isDarkMode) "Modo Oscuro" else "Modo Claro",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Switch(
                checked = isDarkMode,
                onCheckedChange = onToggleDarkMode
            )/
        }
        
        // Botón de cerrar sesión (si hay usuario logueado)
        if (currentUser != null) {
            NavigationDrawerItem(
                label = { Text("Cerrar Sesión") },
                selected = false,
                onClick = onLogout,
                icon = { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar Sesión") },
                modifier = Modifier.padding(horizontal = 12.dp),
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                )
            )
        }
        
        Spacer(Modifier.height(16.dp))
    }
}

// Helper para construir la lista de ítems del drawer según el usuario
@Composable
fun drawerItemsForUser(
    currentUser: UserEntity?,
    onHome: () -> Unit,
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    onEditProfile: () -> Unit
): List<DrawerItem> {
    return if (currentUser != null) {
        // Usuario logueado
        if (currentUser.role == "ADMIN") {
            // Items para admin
            listOf(
                DrawerItem("Dashboard", Icons.Filled.Dashboard, onHome),
                DrawerItem("Usuarios", Icons.Filled.People, onHome),
                DrawerItem("Editar Perfil", Icons.Filled.Edit, onEditProfile)
            )
        } else {
            // Items para usuario público
            listOf(
                DrawerItem("Inicio", Icons.Filled.Home, onHome),
                DrawerItem("Explorar", Icons.Filled.Explore, onHome),
                DrawerItem("Mi Perfil", Icons.Filled.Person, onHome),
                DrawerItem("Editar Perfil", Icons.Filled.Edit, onEditProfile)
            )
        }
    } else {
        // Sin usuario: items básicos
        listOf(
            DrawerItem("Inicio", Icons.Filled.Home, onHome),
            DrawerItem("Iniciar Sesión", Icons.AutoMirrored.Filled.Login, onLogin),
            DrawerItem("Registrarse", Icons.Filled.PersonAdd, onRegister)
        )
    }
}


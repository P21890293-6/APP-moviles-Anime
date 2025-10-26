package com.example.animeverse.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.style.TextOverflow
import com.example.animeverse.data.local.user.UserEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable // Composable reutilizable: barra superior
fun AppTopBar(
    title: String = "🎌 AnimeVerse",  // Título por defecto
    currentUser: UserEntity? = null,   // Usuario actual (null si no hay sesión)
    onOpenDrawer: () -> Unit          // Abre el drawer (hamburguesa)
) {
    CenterAlignedTopAppBar(                   // Barra alineada al centro
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        title = {                              // Slot del título
            Text(
                text = title,                  // Título visible
                style = MaterialTheme.typography.titleLarge, // Estilo grande
                maxLines = 1,                  // Una sola línea
                overflow = TextOverflow.Ellipsis // Agrega "..." si no cabe
            )
        },
        navigationIcon = {                     // Ícono a la izquierda (hamburguesa)
            IconButton(onClick = onOpenDrawer) { // Al presionar, abre drawer
                Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menú")
            }
        }
    )
}


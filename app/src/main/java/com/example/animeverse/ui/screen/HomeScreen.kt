package com.example.animeverse.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.animeverse.ui.components.ProfilePhotoCapture

/**
 * Pantalla Home de AnimeVerse con captura de fotos.
 * Usa componente centralizado ProfilePhotoCapture.
 */
@Composable
fun HomeScreen(
    posts: List<Any> = emptyList(),
    onGoLogin: () -> Unit,
    onGoRegister: () -> Unit
) {
    // Contexto
    val context = LocalContext.current
    
    // Estado de la foto
    var photoUriString by remember { mutableStateOf<String?>(null) }
    
    val bg = MaterialTheme.colorScheme.surfaceVariant // Fondo agradable para Home
    
    Box( // Contenedor a pantalla completa
        modifier = Modifier
            .fillMaxSize() // Ocupa todo
            .background(bg) // Aplica fondo
            .padding(16.dp), // Margen interior
        contentAlignment = Alignment.Center // Centra contenido
    ) {
        Column( // Estructura vertical
            horizontalAlignment = Alignment.CenterHorizontally // Centra hijos
        ) {
            // Cabecera como Row
            Row(
                verticalAlignment = Alignment.CenterVertically // Centra vertical
            ) {
                Text( // Título Home
                    text = "AnimeVerse 🎌",
                    style = MaterialTheme.typography.headlineSmall, // Estilo título
                    fontWeight = FontWeight.SemiBold // Seminegrita
                )
                Spacer(Modifier.width(8.dp)) // Separación horizontal
                AssistChip( // Chip decorativo (Material 3)
                    onClick = {}, // Sin acción (demo)
                    label = { Text("Bienvenido") } // Texto chip
                )
            }
            
            Spacer(Modifier.height(20.dp)) // Separación
            
            // Tarjeta con un mini "hero"
            ElevatedCard( // Card elevada para remarcar contenido
                modifier = Modifier.fillMaxWidth() // Ancho completo
            ) {
                Column(
                    modifier = Modifier.padding(16.dp), // Margen interno de la card
                    horizontalAlignment = Alignment.CenterHorizontally // Centrado
                ) {
                    Text(
                        "Comunidad de Anime, Manga y Gaming",
                        style = MaterialTheme.typography.titleMedium, // Estilo medio
                        textAlign = TextAlign.Center // Alineación centrada
                    )
                    Spacer(Modifier.height(12.dp)) // Separación
                    Text(
                        "Captura y comparte tus momentos otaku favoritos.",
                        style = MaterialTheme.typography.bodyMedium // Texto base
                    )
                }
            }
            
            Spacer(Modifier.height(24.dp)) // Separación
            
            // Componente centralizado de captura de foto
            // Siguiendo patrón del profesor: reutilizamos el mismo componente
            ProfilePhotoCapture(
                photoUri = photoUriString,
                onPhotoChange = { newUri -> photoUriString = newUri },
                title = "Captura de foto con cámara del dispositivo",
                modifier = Modifier.padding(16.dp)
            )
            
            Spacer(Modifier.height(16.dp))
            
            // Botones de navegación principales
            Row( // Dos botones en fila
                horizontalArrangement = Arrangement.spacedBy(12.dp) // Espacio entre botones
            ) {
                Button(onClick = onGoLogin) { Text("Ir a Login") } // Navega a Login
                OutlinedButton(onClick = onGoRegister) { Text("Ir a Registro") } // A Registro
            }
        }
    }
}

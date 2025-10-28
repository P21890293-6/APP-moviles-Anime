package com.example.animeverse.ui.screen

// Imports para Layouts y Modificadores (Estos no cambian)
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

// --- IMPORTS DE MATERIAL 3 (LA PARTE MÁS IMPORTANTE) ---
// Todos los componentes de UI deben venir de 'material3'
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text

// Imports para el Runtime de Compose (Estos no cambian)
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

// Imports de UI y Gráficos (Estos no cambian)
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.animeverse.data.local.comentario.ComentarioEntity

import com.example.animeverse.ui.viewmodel.ComentariosViewModel

@Preview(showSystemUi = false)
@Composable
fun ComentarioItem(comentario: ComentarioEntity) {
    // Card usa CardDefaults para la elevación
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Sintaxis M3
    ) {
        Column(modifier = Modifier.padding(16.dp)) { // Aumenté el padding para mejor estética
            Text(
                text = comentario.id_comentario.toString(),
                // Tipografía y colores de M3
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = comentario.comentario,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = comentario.fecha_registro,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.End) // Un buen detalle es alinear la fecha
            )
        }
    }
}


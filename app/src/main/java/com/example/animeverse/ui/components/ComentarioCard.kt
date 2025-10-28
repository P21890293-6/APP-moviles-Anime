package com.example.animeverse.ui.components

// Imports para Layouts y Modifiers
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

// Imports para Funcionalidad del Runtime de Compose
import androidx.compose.runtime.Composable

// Imports para UI y Gráficos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


import com.example.animeverse.data.local.comentario.ComentarioEntity

@Composable
fun ComentarioCard(comentario: ComentarioEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        // CAMBIO 1: La elevación ahora se define así
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.Top) {

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    // CAMBIO 2: Accede a los colores desde 'colorScheme'
                    .background(MaterialTheme.colorScheme.primary)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = comentario.id_comentario.toString(),
                    // CAMBIO 3: Accede a la tipografía y colores de M3
                    style = MaterialTheme.typography.titleMedium, // En M3 es titleMedium, no subtitle1
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = comentario.id_comentario.toString(),
                    style = MaterialTheme.typography.bodyLarge // body1 ahora suele ser bodyLarge
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = comentario.fecha_registro,
                    style = MaterialTheme.typography.bodySmall, // caption ahora suele ser bodySmall
                    color = Color.Gray
                )
            }
        }
    }
}

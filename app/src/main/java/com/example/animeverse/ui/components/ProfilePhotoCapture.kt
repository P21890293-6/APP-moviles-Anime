package com.example.animeverse.ui.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import coil.request.ImageRequest
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Componente centralizado para captura de foto de perfil.
 * Siguiendo el patrón del profesor con permisos y FileProvider.
 */
@Composable
fun ProfilePhotoCapture(
    photoUri: String?,                               // URI de la foto actual (null si no hay)
    onPhotoChange: (String?) -> Unit,                // Callback cuando cambia la foto
    modifier: Modifier = Modifier,                   // Modificador opcional
    title: String = "Foto de perfil (opcional)"      // Título personalizable
) {
    val context = LocalContext.current               // Contexto para acceder a recursos
    
    // Estado local para URI temporal mientras se captura
    var pendingCaptureUri by remember { mutableStateOf<Uri?>(null) }
    
    // Estado para dialog de permisos
    var showPermissionDialog by remember { mutableStateOf(false) }
    var showGalleryPermissionDialog by remember { mutableStateOf(false) }
    
    // Launcher para la cámara (TakePicture) - DEBE IR PRIMERO
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            // Foto capturada exitosamente
            onPhotoChange(pendingCaptureUri?.toString())
            Toast.makeText(context, "Foto de perfil capturada", Toast.LENGTH_SHORT).show()
        } else {
            // Usuario canceló o error
            pendingCaptureUri = null
            Toast.makeText(context, "Captura cancelada", Toast.LENGTH_SHORT).show()
        }
    }
    
    // Launcher para seleccionar imagen de galería - DEBE IR SEGUNDO
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            // Imagen seleccionada exitosamente
            onPhotoChange(uri.toString())
            Toast.makeText(context, "Foto de perfil seleccionada", Toast.LENGTH_SHORT).show()
        } else {
            // Usuario canceló
            Toast.makeText(context, "Selección cancelada", Toast.LENGTH_SHORT).show()
        }
    }
    
    // Launcher para solicitar permiso de cámara - USA takePictureLauncher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permiso concedido, abrir cámara
            val file = createTempImageFile(context)
            val uri = getImageUriForFile(context, file)
            pendingCaptureUri = uri
            takePictureLauncher.launch(uri)
        } else {
            // Permiso denegado
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }
    
    // Launcher para solicitar permiso de galería - USA pickImageLauncher
    val galleryPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permiso concedido, abrir galería
            pickImageLauncher.launch("image/*")
        } else {
            // Permiso denegado
            Toast.makeText(context, "Permiso de galería denegado", Toast.LENGTH_SHORT).show()
        }
    }
    
    // Card contenedor centrado
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color(0xFF2C2C2E)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Spacer(Modifier.height(12.dp))
            
            // Vista previa de la foto (círculo)
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF3A3A3C)),
                contentAlignment = Alignment.Center
            ) {
                if (photoUri.isNullOrEmpty()) {
                    // Sin foto: ícono de persona
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Sin foto",
                        modifier = Modifier.size(60.dp),
                        tint = Color.White.copy(alpha = 0.6f)
                    )
                } else {
                    // Con foto: muestra imagen usando Coil
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(Uri.parse(photoUri))
                            .crossfade(true)
                            .build(),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            
            Spacer(Modifier.height(12.dp))
            
            // Botones de acción centrados
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                // Botón tomar foto
                FilledTonalButton(
                    onClick = {
                        // Verificar permiso de cámara
                        when {
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED -> {
                                // Permiso ya concedido, abrir cámara
                                val file = createTempImageFile(context)
                                val uri = getImageUriForFile(context, file)
                                pendingCaptureUri = uri
                                takePictureLauncher.launch(uri)
                            }
                            else -> {
                                // Solicitar permiso
                                showPermissionDialog = true
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.CameraAlt,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Tomar foto")
                }
                
                // Botón seleccionar de galería
                FilledTonalButton(
                    onClick = {
                        // Determinar qué permiso necesitamos según la versión de Android
                        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            Manifest.permission.READ_MEDIA_IMAGES
                        } else {
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        }
                        
                        // Verificar permiso de galería
                        when {
                            ContextCompat.checkSelfPermission(
                                context,
                                permission
                            ) == PackageManager.PERMISSION_GRANTED -> {
                                // Permiso ya concedido, abrir galería
                                pickImageLauncher.launch("image/*")
                            }
                            else -> {
                                // Solicitar permiso
                                showGalleryPermissionDialog = true
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.PhotoLibrary,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Galería")
                }
            }
            
            // Botón eliminar (solo si hay foto)
            if (!photoUri.isNullOrEmpty()) {
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick = {
                        onPhotoChange(null)
                        Toast.makeText(context, "Foto eliminada", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text("Eliminar foto")
                }
            }
        }
    }
    
    // Dialog para explicar el permiso de cámara
    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text("Permiso de Cámara") },
            text = { 
                Text("AnimeVerse necesita acceso a tu cámara para tomar fotos de perfil. ¿Deseas conceder este permiso?") 
            },
            confirmButton = {
                Button(onClick = {
                    showPermissionDialog = false
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }) {
                    Text("Permitir")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
    
    // Dialog para explicar el permiso de galería
    if (showGalleryPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showGalleryPermissionDialog = false },
            title = { Text("Permiso de Galería") },
            text = { 
                Text("AnimeVerse necesita acceso a tu galería para seleccionar fotos de perfil. ¿Deseas conceder este permiso?") 
            },
            confirmButton = {
                Button(onClick = {
                    showGalleryPermissionDialog = false
                    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        Manifest.permission.READ_MEDIA_IMAGES
                    } else {
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    }
                    galleryPermissionLauncher.launch(permission)
                }) {
                    Text("Permitir")
                }
            },
            dismissButton = {
                TextButton(onClick = { showGalleryPermissionDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

/**
 * Crea un archivo temporal en el cache para guardar la foto.
 * Patrón del profesor: usa cache/images/ como directorio.
 */
private fun createTempImageFile(context: Context): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir = File(context.cacheDir, "images").apply {
        if (!exists()) mkdirs()                      // Crea directorio si no existe
    }
    return File(storageDir, "PROFILE_${timeStamp}.jpg")
}

/**
 * Obtiene URI segura usando FileProvider.
 * Patrón del profesor: usa el authority configurado en AndroidManifest.
 */
private fun getImageUriForFile(context: Context, file: File): Uri {
    val authority = "${context.packageName}.fileprovider"
    return FileProvider.getUriForFile(context, authority, file)
}


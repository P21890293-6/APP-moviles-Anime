package com.example.animeverse.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.animeverse.data.local.user.UserEntity

/**
 * Pantalla para cambiar la contraseña del usuario
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    currentUser: UserEntity,
    onBackClick: () -> Unit = {},
    onPasswordChanged: () -> Unit = {}
) {
    val context = LocalContext.current
    
    // Estados locales
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    var currentPasswordError by remember { mutableStateOf<String?>(null) }
    var newPasswordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    
    var showCurrentPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    
    var isSubmitting by remember { mutableStateOf(false) }
    
    // Validación
    val canSubmit = currentPassword.isNotBlank() && 
                    newPassword.isNotBlank() && 
                    confirmPassword.isNotBlank() &&
                    !isSubmitting
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Cambiar contraseña",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Actualiza tu contraseña",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(Modifier.height(32.dp))
            
            // Campo Contraseña Actual
            OutlinedTextField(
                value = currentPassword,
                onValueChange = { 
                    currentPassword = it
                    currentPasswordError = null
                },
                label = { Text("Contraseña actual") },
                singleLine = true,
                visualTransformation = if (showCurrentPassword) 
                    VisualTransformation.None 
                else 
                    PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showCurrentPassword = !showCurrentPassword }) {
                        Icon(
                            imageVector = if (showCurrentPassword) 
                                Icons.Filled.VisibilityOff 
                            else 
                                Icons.Filled.Visibility,
                            contentDescription = if (showCurrentPassword) 
                                "Ocultar contraseña" 
                            else 
                                "Mostrar contraseña"
                        )
                    }
                },
                isError = currentPasswordError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.fillMaxWidth()
            )
            if (currentPasswordError != null) {
                Text(
                    text = currentPasswordError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 4.dp)
                )
            }
            
            Spacer(Modifier.height(16.dp))
            
            // Campo Nueva Contraseña
            OutlinedTextField(
                value = newPassword,
                onValueChange = { 
                    newPassword = it
                    newPasswordError = null
                },
                label = { Text("Nueva contraseña") },
                singleLine = true,
                visualTransformation = if (showNewPassword) 
                    VisualTransformation.None 
                else 
                    PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showNewPassword = !showNewPassword }) {
                        Icon(
                            imageVector = if (showNewPassword) 
                                Icons.Filled.VisibilityOff 
                            else 
                                Icons.Filled.Visibility,
                            contentDescription = if (showNewPassword) 
                                "Ocultar contraseña" 
                            else 
                                "Mostrar contraseña"
                        )
                    }
                },
                isError = newPasswordError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.fillMaxWidth()
            )
            if (newPasswordError != null) {
                Text(
                    text = newPasswordError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 4.dp)
                )
            }
            
            Spacer(Modifier.height(16.dp))
            
            // Campo Confirmar Nueva Contraseña
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { 
                    confirmPassword = it
                    confirmPasswordError = null
                },
                label = { Text("Confirmar nueva contraseña") },
                singleLine = true,
                visualTransformation = if (showConfirmPassword) 
                    VisualTransformation.None 
                else 
                    PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                        Icon(
                            imageVector = if (showConfirmPassword) 
                                Icons.Filled.VisibilityOff 
                            else 
                                Icons.Filled.Visibility,
                            contentDescription = if (showConfirmPassword) 
                                "Ocultar contraseña" 
                            else 
                                "Mostrar contraseña"
                        )
                    }
                },
                isError = confirmPasswordError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.fillMaxWidth()
            )
            if (confirmPasswordError != null) {
                Text(
                    text = confirmPasswordError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 4.dp)
                )
            }
            
            Spacer(Modifier.height(8.dp))
            
            // Requisitos de contraseña
            Text(
                text = "La contraseña debe tener al menos 6 caracteres",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(Modifier.height(32.dp))
            
            // Botón Guardar
            Button(
                onClick = {
                    // Validaciones
                    var hasError = false
                    
                    if (currentPassword != currentUser.password) {
                        currentPasswordError = "Contraseña actual incorrecta"
                        hasError = true
                    }
                    
                    if (newPassword.length < 6) {
                        newPasswordError = "La contraseña debe tener al menos 6 caracteres"
                        hasError = true
                    }
                    
                    if (newPassword != confirmPassword) {
                        confirmPasswordError = "Las contraseñas no coinciden"
                        hasError = true
                    }
                    
                    if (currentPassword == newPassword) {
                        newPasswordError = "La nueva contraseña debe ser diferente"
                        hasError = true
                    }
                    
                    if (!hasError) {
                        // TODO: Aquí se debería actualizar la contraseña en la base de datos
                        Toast.makeText(
                            context,
                            "Contraseña actualizada exitosamente",
                            Toast.LENGTH_SHORT
                        ).show()
                        onPasswordChanged()
                    }
                },
                enabled = canSubmit,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(18.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Guardando...")
                } else {
                    Text("Guardar cambios")
                }
            }
            
            Spacer(Modifier.height(8.dp))
            
            // Botón Cancelar
            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    }
}


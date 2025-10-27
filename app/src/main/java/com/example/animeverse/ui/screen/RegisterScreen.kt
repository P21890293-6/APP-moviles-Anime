package com.example.animeverse.ui.screen

import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.example.animeverse.ui.viewmodel.AuthViewModel
import com.example.animeverse.ui.components.ProfilePhotoCapture

/**
 * Pantalla de Registro de AnimeVerse.
 * Conectada con SQLite (Room Database) para guardar usuarios.
 * Usa componente centralizado ProfilePhotoCapture siguiendo patrón del profesor.
 */
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit = {},
    onGoLogin: () -> Unit = {}
) {
    val context = LocalContext.current
    
    // Observar estado del ViewModel
    val state by viewModel.registerState.collectAsState()
    
    // Estados locales UI
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    
    // Animación de shake para errores
    var shakeKey by remember { mutableStateOf(0) }
    val shakeOffset by animateFloatAsState(
        targetValue = if (shakeKey % 2 == 0) 0f else 10f,
        animationSpec = repeatable(
            iterations = 3,
            animation = tween(durationMillis = 50),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shakeAnimation"
    )
    
    // Trigger shake cuando hay error
    LaunchedEffect(state.errorMsg) {
        if (state.errorMsg != null) {
            shakeKey++
        }
    }
    
    // Verificar si registro fue exitoso
    LaunchedEffect(state.success) {
        if (state.success) {
            Toast.makeText(context, "Usuario registrado en SQLite", Toast.LENGTH_SHORT).show()
            viewModel.clearRegisterResult()
            onRegisterSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo / Título
            Text(
                text = "🎌",
                style = MaterialTheme.typography.displayMedium
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Crear Cuenta",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Únete a la comunidad de AnimeVerse",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Spacer(Modifier.height(16.dp))
            
            // Componente centralizado de foto de perfil
            // Siguiendo patrón del profesor: componente reutilizable
            ProfilePhotoCapture(
                photoUri = state.photoUri,
                onPhotoChange = viewModel::onRegisterPhotoChange,
                title = "Foto de perfil (opcional)",
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(Modifier.height(16.dp))
            
            // Campo Nombre
            OutlinedTextField(
                value = state.name,
                onValueChange = viewModel::onRegisterNameChange,
                label = { Text("Nombre completo") },
                singleLine = true,
                isError = state.nameError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.fillMaxWidth()
            )
            if (state.nameError != null) {
                Text(
                    text = state.nameError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 4.dp)
                )
            } else {
                Text(
                    text = "Solo letras y espacios permitidos",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 4.dp)
                )
            }
            
            Spacer(Modifier.height(12.dp))
            
            // Campo Email
            OutlinedTextField(
                value = state.email,
                onValueChange = viewModel::onRegisterEmailChange,
                label = { Text("Email") },
                singleLine = true,
                isError = state.emailError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.fillMaxWidth()
            )
            if (state.emailError != null) {
                Text(
                    text = state.emailError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 4.dp)
                )
            }
            
            Spacer(Modifier.height(12.dp))
            
            // Campo Número de Teléfono
            OutlinedTextField(
                value = state.phoneNumber,
                onValueChange = viewModel::onRegisterPhoneNumberChange,
                label = { Text("Número de teléfono") },
                singleLine = true,
                isError = state.phoneNumberError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.fillMaxWidth()
            )
            if (state.phoneNumberError != null) {
                Text(
                    text = state.phoneNumberError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 4.dp)
                )
            } else {
                Text(
                    text = "Solo números (8-15 dígitos)",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 4.dp)
                )
            }
            
            Spacer(Modifier.height(12.dp))
            
            // Campo Contraseña
            OutlinedTextField(
                value = state.password,
                onValueChange = viewModel::onRegisterPasswordChange,
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = if (showPassword) 
                    VisualTransformation.None 
                else 
                    PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) 
                                Icons.Filled.VisibilityOff 
                            else 
                                Icons.Filled.Visibility,
                            contentDescription = if (showPassword) 
                                "Ocultar contraseña" 
                            else 
                                "Mostrar contraseña"
                        )
                    }
                },
                isError = state.passwordError != null,
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
            if (state.passwordError != null) {
                Text(
                    text = state.passwordError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 4.dp)
                )
            } else {
                Text(
                    text = "Mínimo 8 caracteres: mayúscula, minúscula, número y símbolo",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 4.dp)
                )
            }
            
            Spacer(Modifier.height(12.dp))
            
            // Campo Confirmar Contraseña
            OutlinedTextField(
                value = state.confirmPassword,
                onValueChange = viewModel::onRegisterConfirmChange,
                label = { Text("Confirmar contraseña") },
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
                isError = state.confirmError != null,
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
            if (state.confirmError != null) {
                Text(
                    text = state.confirmError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 4.dp)
                )
            }
            
            Spacer(Modifier.height(24.dp))
            
            // Botón Registrar (con animación de shake en error)
            Button(
                onClick = viewModel::submitRegister,
                enabled = state.canSubmit && !state.isSubmitting,
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        translationX = shakeOffset
                    }
            ) {
                if (state.isSubmitting) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(18.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Guardando en SQLite...")
                } else {
                    Text("Registrar")
                }
            }
            
            // Mensaje de error global (con animación de shake)
            if (state.errorMsg != null) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = state.errorMsg!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.graphicsLayer {
                        translationX = shakeOffset
                    }
                )
            }
            
            Spacer(Modifier.height(16.dp))
            
            // Botón Ya tengo cuenta
            OutlinedButton(
                onClick = onGoLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ya tengo cuenta")
            }
            
            Spacer(Modifier.height(16.dp))
        }
    }
}


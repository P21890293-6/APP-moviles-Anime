package com.example.animeverse.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.animeverse.data.local.user.UserEntity
import com.example.animeverse.ui.components.ProfilePhotoCapture
import com.example.animeverse.ui.viewmodel.EditProfileViewModel

//1 Creamos la unión con el viewmodel (patrón del profesor)
@Composable
fun EditProfileScreenVm(
    vm: EditProfileViewModel,
    currentUser: UserEntity,
    onBackClick: () -> Unit,
    onProfileUpdated: () -> Unit
) {
    // Observa estado en tiempo real
    val state by vm.editProfileState.collectAsStateWithLifecycle()
    
    // Cargar datos del usuario actual
    LaunchedEffect(currentUser) {
        vm.loadUserData(currentUser)
    }
    
    // Si se actualizó exitosamente
    if (state.success) {
        val context = LocalContext.current
        LaunchedEffect(state.success) {
            Toast.makeText(context, "Perfil actualizado", Toast.LENGTH_SHORT).show()
            vm.clearResult()
            onProfileUpdated()
        }
    }
    
    // Delegamos UI presentacional
    EditProfileScreen(
        fullName = state.fullName,
        email = state.email,
        photoUri = state.photoUri,
        fullNameError = state.fullNameError,
        emailError = state.emailError,
        canSubmit = state.canSubmit,
        isSubmitting = state.isSubmitting,
        errorMsg = state.errorMsg,
        onFullNameChange = vm::onFullNameChange,
        onEmailChange = vm::onEmailChange,
        onPhotoChange = vm::onPhotoChange,
        onSubmit = vm::submitUpdate,
        onBackClick = onBackClick
    )
}

//2 Ajustamos el private y parámetros (solo UI)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditProfileScreen(
    fullName: String,
    email: String,
    photoUri: String?,
    fullNameError: String?,
    emailError: String?,
    canSubmit: Boolean,
    isSubmitting: Boolean,
    errorMsg: String?,
    onFullNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhotoChange: (String?) -> Unit,
    onSubmit: () -> Unit,
    onBackClick: () -> Unit
) {
    val bg = MaterialTheme.colorScheme.surfaceVariant
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Editar Perfil",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bg)
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(16.dp))
                
                Text(
                    text = "Actualiza tu información",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(Modifier.height(24.dp))
                
                // Componente centralizado de foto de perfil
                ProfilePhotoCapture(
                    photoUri = photoUri,
                    onPhotoChange = onPhotoChange,
                    title = "Foto de perfil",
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(Modifier.height(24.dp))
                
                // Campo Nombre Completo
                OutlinedTextField(
                    value = fullName,
                    onValueChange = onFullNameChange,
                    label = { Text("Nombre completo") },
                    singleLine = true,
                    isError = fullNameError != null,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                if (fullNameError != null) {
                    Text(
                        text = fullNameError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 4.dp)
                    )
                }
                
                Spacer(Modifier.height(16.dp))
                
                // Campo Email
                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = { Text("Email") },
                    singleLine = true,
                    isError = emailError != null,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                if (emailError != null) {
                    Text(
                        text = emailError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 4.dp)
                    )
                }
                
                Spacer(Modifier.height(24.dp))
                
                // Botón Guardar
                Button(
                    onClick = onSubmit,
                    enabled = canSubmit && !isSubmitting,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(18.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Actualizando...")
                    } else {
                        Text("Guardar Cambios")
                    }
                }
                
                // Mensaje de error global
                if (errorMsg != null) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = errorMsg,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }
                
                Spacer(Modifier.height(16.dp))
                
                // Botón Cancelar
                OutlinedButton(
                    onClick = onBackClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar")
                }
                
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}


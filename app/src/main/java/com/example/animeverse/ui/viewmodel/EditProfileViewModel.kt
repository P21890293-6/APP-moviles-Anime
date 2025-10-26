package com.example.animeverse.ui.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeverse.data.local.user.UserDao
import com.example.animeverse.data.local.user.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para editar perfil de usuario.
 * Siguiendo patrón del profesor.
 */
class EditProfileViewModel(
    private val userDao: UserDao
) : ViewModel() {

    // Estado para editar perfil
    private val _editProfileState = MutableStateFlow(EditProfileState())
    val editProfileState: StateFlow<EditProfileState> = _editProfileState.asStateFlow()
    
    // ID del usuario actual
    private var currentUserId: Long = 0L

    // Cargar datos del usuario actual
    fun loadUserData(user: UserEntity) {
        currentUserId = user.id
        _editProfileState.value = EditProfileState(
            fullName = user.fullName,
            email = user.email,
            photoUri = user.avatar,
            canSubmit = true
        )
    }

    fun onFullNameChange(fullName: String) {
        _editProfileState.value = _editProfileState.value.copy(
            fullName = fullName,
            fullNameError = null,
            errorMsg = null
        )
        validateForm()
    }

    fun onEmailChange(email: String) {
        _editProfileState.value = _editProfileState.value.copy(
            email = email,
            emailError = null,
            errorMsg = null
        )
        validateForm()
    }

    fun onPhotoChange(photoUri: String?) {
        _editProfileState.value = _editProfileState.value.copy(photoUri = photoUri)
    }

    private fun validateForm() {
        val state = _editProfileState.value
        val canSubmit = state.fullName.isNotBlank() && state.email.isNotBlank()
        _editProfileState.value = state.copy(canSubmit = canSubmit)
    }

    fun submitUpdate() {
        val state = _editProfileState.value

        // Validaciones
        var hasError = false

        if (state.fullName.isBlank()) {
            _editProfileState.value = state.copy(fullNameError = "El nombre es requerido")
            hasError = true
        } else if (state.fullName.length < 3) {
            _editProfileState.value = state.copy(fullNameError = "Mínimo 3 caracteres")
            hasError = true
        }

        if (state.email.isBlank()) {
            _editProfileState.value = state.copy(emailError = "El email es requerido")
            hasError = true
        } else if (!Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            _editProfileState.value = state.copy(emailError = "Email inválido")
            hasError = true
        }

        if (hasError) return

        // Intentar actualizar en la base de datos
        _editProfileState.value = state.copy(isSubmitting = true, errorMsg = null)

        viewModelScope.launch {
            try {
                // Obtener usuario actual
                val currentUser = userDao.getUserById(currentUserId)
                
                if (currentUser == null) {
                    _editProfileState.value = _editProfileState.value.copy(
                        isSubmitting = false,
                        errorMsg = "Error: usuario no encontrado"
                    )
                    return@launch
                }
                
                // Verificar si el nuevo email ya existe (si cambió)
                if (state.email != currentUser.email) {
                    val existingUser = userDao.getUserByEmail(state.email)
                    if (existingUser != null && existingUser.id != currentUserId) {
                        _editProfileState.value = _editProfileState.value.copy(
                            isSubmitting = false,
                            errorMsg = "Este email ya está en uso"
                        )
                        return@launch
                    }
                }

                // Actualizar usuario
                val updatedUser = currentUser.copy(
                    fullName = state.fullName,
                    email = state.email,
                    avatar = state.photoUri
                )

                // Guardar en la base de datos
                userDao.updateUser(updatedUser)

                // Actualización exitosa
                _editProfileState.value = _editProfileState.value.copy(
                    isSubmitting = false,
                    success = true
                )

            } catch (e: Exception) {
                _editProfileState.value = _editProfileState.value.copy(
                    isSubmitting = false,
                    errorMsg = "Error al actualizar: ${e.message}"
                )
            }
        }
    }

    fun clearResult() {
        _editProfileState.value = EditProfileState()
    }
}

// Data class para el estado
data class EditProfileState(
    val fullName: String = "",
    val email: String = "",
    val photoUri: String? = null,
    val fullNameError: String? = null,
    val emailError: String? = null,
    val canSubmit: Boolean = false,
    val isSubmitting: Boolean = false,
    val errorMsg: String? = null,
    val success: Boolean = false
)


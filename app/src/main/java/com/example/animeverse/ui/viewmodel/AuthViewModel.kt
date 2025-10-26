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
 * ViewModel para autenticación (Login y Registro).
 * Conecta la UI con la base de datos SQLite (Room).
 */
class AuthViewModel(
    private val userDao: UserDao
) : ViewModel() {

    // ========== LOGIN STATE ==========
    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    // ========== REGISTER STATE ==========
    private val _registerState = MutableStateFlow(RegisterState())
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()

    // ========== LOGIN FUNCTIONS ==========

    fun onLoginEmailChange(email: String) {
        _loginState.value = _loginState.value.copy(
            email = email,
            emailError = null,
            errorMsg = null
        )
        validateLoginForm()
    }

    fun onLoginPasswordChange(password: String) {
        _loginState.value = _loginState.value.copy(
            password = password,
            passwordError = null,
            errorMsg = null
        )
        validateLoginForm()
    }

    private fun validateLoginForm() {
        val state = _loginState.value
        val canSubmit = state.email.isNotBlank() && state.password.isNotBlank()
        _loginState.value = state.copy(canSubmit = canSubmit)
    }

    fun submitLogin() {
        val state = _loginState.value

        // Validaciones
        var hasError = false

        if (state.email.isBlank()) {
            _loginState.value = state.copy(emailError = "El email es requerido")
            hasError = true
        } else if (!Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            _loginState.value = state.copy(emailError = "Email inválido")
            hasError = true
        }

        if (state.password.isBlank()) {
            _loginState.value = state.copy(passwordError = "La contraseña es requerida")
            hasError = true
        }

        if (hasError) return

        // Intentar login en la base de datos
        _loginState.value = state.copy(isSubmitting = true, errorMsg = null)

        viewModelScope.launch {
            try {
                // Verificar si hay usuarios en la BD, si no, crear usuarios por defecto
                val userCount = userDao.count()
                if (userCount == 0) {
                    // Crear usuario admin
                    userDao.insertUser(
                        UserEntity(
                            username = "admin",
                            email = "admin@animeverse.com",
                            password = "admin123",
                            fullName = "Administrador",
                            role = "ADMIN"
                        )
                    )
                    
                    // Crear usuarios de ejemplo
                    userDao.insertUser(
                        UserEntity(
                            username = "anime_lover",
                            email = "anime@example.com",
                            password = "123456",
                            fullName = "María García",
                            role = "USER"
                        )
                    )
                    
                    userDao.insertUser(
                        UserEntity(
                            username = "manga_reader",
                            email = "manga@example.com",
                            password = "123456",
                            fullName = "Carlos López",
                            role = "USER"
                        )
                    )
                    
                    userDao.insertUser(
                        UserEntity(
                            username = "gamer_pro",
                            email = "gamer@example.com",
                            password = "123456",
                            fullName = "Ana Rodríguez",
                            role = "USER"
                        )
                    )
                }
                
                val user = userDao.getUserByEmail(state.email)

                if (user == null) {
                    _loginState.value = _loginState.value.copy(
                        isSubmitting = false,
                        errorMsg = "Usuario no encontrado"
                    )
                    return@launch
                }

                if (user.password != state.password) {
                    _loginState.value = _loginState.value.copy(
                        isSubmitting = false,
                        errorMsg = "Contraseña incorrecta"
                    )
                    return@launch
                }
                
                // Validar si está baneado
                if (user.banned) {
                    _loginState.value = _loginState.value.copy(
                        isSubmitting = false,
                        errorMsg = "Tu cuenta ha sido suspendida. Contacta al administrador."
                    )
                    return@launch
                }

                // Login exitoso
                _loginState.value = _loginState.value.copy(
                    isSubmitting = false,
                    success = true,
                    loggedInUser = user
                )

            } catch (e: Exception) {
                _loginState.value = _loginState.value.copy(
                    isSubmitting = false,
                    errorMsg = "Error al iniciar sesión: ${e.message}"
                )
            }
        }
    }

    fun clearLoginResult() {
        _loginState.value = LoginState()
    }

    // ========== REGISTER FUNCTIONS ==========

    fun onRegisterNameChange(name: String) {
        _registerState.value = _registerState.value.copy(
            name = name,
            nameError = null,
            errorMsg = null
        )
        validateRegisterForm()
    }

    fun onRegisterEmailChange(email: String) {
        _registerState.value = _registerState.value.copy(
            email = email,
            emailError = null,
            errorMsg = null
        )
        validateRegisterForm()
    }

    fun onRegisterPasswordChange(password: String) {
        _registerState.value = _registerState.value.copy(
            password = password,
            passwordError = null,
            errorMsg = null
        )
        validateRegisterForm()
    }

    fun onRegisterConfirmChange(confirm: String) {
        _registerState.value = _registerState.value.copy(
            confirmPassword = confirm,
            confirmError = null,
            errorMsg = null
        )
        validateRegisterForm()
    }

    fun onRegisterPhotoChange(photoUri: String?) {
        _registerState.value = _registerState.value.copy(photoUri = photoUri)
    }

    private fun validateRegisterForm() {
        val state = _registerState.value
        val canSubmit = state.name.isNotBlank() &&
                state.email.isNotBlank() &&
                state.password.isNotBlank() &&
                state.confirmPassword.isNotBlank()
        _registerState.value = state.copy(canSubmit = canSubmit)
    }

    fun submitRegister() {
        val state = _registerState.value

        // Validaciones
        var hasError = false

        if (state.name.isBlank()) {
            _registerState.value = state.copy(nameError = "El nombre es requerido")
            hasError = true
        } else if (state.name.length < 3) {
            _registerState.value = state.copy(nameError = "Mínimo 3 caracteres")
            hasError = true
        }

        if (state.email.isBlank()) {
            _registerState.value = state.copy(emailError = "El email es requerido")
            hasError = true
        } else if (!Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            _registerState.value = state.copy(emailError = "Email inválido")
            hasError = true
        }

        if (state.password.isBlank()) {
            _registerState.value = state.copy(passwordError = "La contraseña es requerida")
            hasError = true
        } else if (state.password.length < 6) {
            _registerState.value = state.copy(passwordError = "Mínimo 6 caracteres")
            hasError = true
        }

        if (state.confirmPassword.isBlank()) {
            _registerState.value = state.copy(confirmError = "Confirma tu contraseña")
            hasError = true
        } else if (state.password != state.confirmPassword) {
            _registerState.value = state.copy(confirmError = "Las contraseñas no coinciden")
            hasError = true
        }

        if (hasError) return

        // Intentar registrar en la base de datos
        _registerState.value = state.copy(isSubmitting = true, errorMsg = null)

        viewModelScope.launch {
            try {
                // Verificar si el email ya existe
                val existingUser = userDao.getUserByEmail(state.email)
                if (existingUser != null) {
                    _registerState.value = _registerState.value.copy(
                        isSubmitting = false,
                        errorMsg = "Este email ya está registrado"
                    )
                    return@launch
                }

                // Crear username a partir del email
                val username = state.email.substringBefore("@")

                // Crear nuevo usuario
                val newUser = UserEntity(
                    username = username,
                    email = state.email,
                    password = state.password,  // En producción usar hash
                    fullName = state.name,
                    avatar = state.photoUri,
                    createdAt = System.currentTimeMillis()
                )

                // Guardar en la base de datos (SQLite)
                val userId = userDao.insertUser(newUser)

                // Registro exitoso
                _registerState.value = _registerState.value.copy(
                    isSubmitting = false,
                    success = true,
                    registeredUserId = userId
                )

            } catch (e: Exception) {
                _registerState.value = _registerState.value.copy(
                    isSubmitting = false,
                    errorMsg = "Error al registrar: ${e.message}"
                )
            }
        }
    }

    fun clearRegisterResult() {
        _registerState.value = RegisterState()
    }
}

// ========== DATA CLASSES ==========

data class LoginState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val canSubmit: Boolean = false,
    val isSubmitting: Boolean = false,
    val errorMsg: String? = null,
    val success: Boolean = false,
    val loggedInUser: UserEntity? = null
)

data class RegisterState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val photoUri: String? = null,
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmError: String? = null,
    val canSubmit: Boolean = false,
    val isSubmitting: Boolean = false,
    val errorMsg: String? = null,
    val success: Boolean = false,
    val registeredUserId: Long? = null
)



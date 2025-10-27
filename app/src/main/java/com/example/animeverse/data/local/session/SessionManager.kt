package com.example.animeverse.data.local.session

import android.content.Context
import android.content.SharedPreferences

/**
 * Gestor de sesión para mantener al usuario logueado.
 * Usa SharedPreferences para persistir la sesión.
 */
class SessionManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME, 
        Context.MODE_PRIVATE
    )
    
    companion object {
        private const val PREFS_NAME = "animeverse_session"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }
    
    /**
     * Guarda la sesión del usuario.
     */
    fun saveSession(userId: Long) {
        prefs.edit().apply {
            putLong(KEY_USER_ID, userId)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }
    
    /**
     * Obtiene el ID del usuario logueado.
     * Retorna null si no hay sesión activa.
     */
    fun getUserId(): Long? {
        val isLoggedIn = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        return if (isLoggedIn) {
            prefs.getLong(KEY_USER_ID, -1L).takeIf { it != -1L }
        } else {
            null
        }
    }
    
    /**
     * Verifica si hay una sesión activa.
     */
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    
    /**
     * Cierra la sesión del usuario.
     */
    fun clearSession() {
        prefs.edit().apply {
            remove(KEY_USER_ID)
            putBoolean(KEY_IS_LOGGED_IN, false)
            apply()
        }
    }
}



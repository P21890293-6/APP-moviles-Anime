package com.example.animeverse.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeverse.data.local.comentario.ComentarioEntity
import com.example.animeverse.data.repository.AnimeVerseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ComentariosViewModel(
    private val repository: AnimeVerseRepository
) : ViewModel() {

    // Estado observable que contendrá la lista de comentarios
    private val _comentarios = MutableStateFlow<List<ComentarioEntity>>(emptyList())
    val comentarios: StateFlow<List<ComentarioEntity>> = _comentarios

    // Estado de carga o error (opcional, útil para la UI)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Obtener comentarios por id de publicación
    fun cargarComentarios(idPublicacion: Int) {
        viewModelScope.launch {
            repository.getComentariosByPublicacion(idPublicacion).collect { lista ->
                _comentarios.value = lista
            }
        }
    }

    // Insertar nuevo comentario
    fun agregarComentario(comentario: ComentarioEntity) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.insertComentario(comentario)
            _isLoading.value = false
        }
    }
}
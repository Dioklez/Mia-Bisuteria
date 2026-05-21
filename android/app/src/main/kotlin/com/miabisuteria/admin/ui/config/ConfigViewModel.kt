package com.miabisuteri.admin.ui.config

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miabisuteri.admin.data.repository.ConfigRepository
import com.miabisuteri.admin.data.repository.ProductRepository
import com.miabisuteri.admin.domain.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class ConfigUiState(
    val config: ConfigSite = ConfigSite(),
    val productos: List<Producto> = emptyList(),
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val isUploadingImage: Boolean = false,
    val saved: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ConfigViewModel @Inject constructor(
    private val configRepo: ConfigRepository,
    private val productRepo: ProductRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ConfigUiState())
    val state: StateFlow<ConfigUiState> = _state.asStateFlow()

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val config = runCatching { configRepo.get() }.getOrElse { ConfigSite() }
            val productos = runCatching { productRepo.getAll() }.getOrElse { emptyList() }
            _state.update { it.copy(config = config, productos = productos, isLoading = false) }
        }
    }

    fun saveConfig() {
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            runCatching { configRepo.save(_state.value.config) }
                .onSuccess { _state.update { it.copy(isSaving = false, saved = true) } }
                .onFailure { e -> _state.update { it.copy(isSaving = false, error = e.message) } }
        }
    }

    // ── Destacado ─────────────────────────────────────────────────────────────

    fun setDestacado(producto: Producto) {
        _state.update { s ->
            s.copy(config = s.config.copy(
                destacado = ProductoDestacado(
                    nombre = producto.nombre,
                    precio = producto.precio,
                    img = producto.imgs.firstOrNull() ?: ""
                )
            ))
        }
    }

    // ── Favoritos ─────────────────────────────────────────────────────────────

    fun toggleFavorito(id: Int) {
        val current = _state.value.config.favoritos.toMutableList()
        if (id in current) current.remove(id)
        else if (current.size < 4) current.add(id)
        _state.update { s -> s.copy(config = s.config.copy(favoritos = current)) }
    }

    // ── Colecciones ───────────────────────────────────────────────────────────

    fun updateColeccion(index: Int, col: Coleccion) {
        val list = _state.value.config.colecciones.toMutableList()
        list[index] = col
        _state.update { s -> s.copy(config = s.config.copy(colecciones = list)) }
    }

    // ── Testimonios ───────────────────────────────────────────────────────────

    fun addTestimonio() {
        val list = _state.value.config.testimonios.toMutableList()
        list.add(Testimonio(id = UUID.randomUUID().toString()))
        _state.update { s -> s.copy(config = s.config.copy(testimonios = list)) }
    }

    fun updateTestimonio(index: Int, t: Testimonio) {
        val list = _state.value.config.testimonios.toMutableList()
        list[index] = t
        _state.update { s -> s.copy(config = s.config.copy(testimonios = list)) }
    }

    fun removeTestimonio(index: Int) {
        val list = _state.value.config.testimonios.toMutableList()
        list.removeAt(index)
        _state.update { s -> s.copy(config = s.config.copy(testimonios = list)) }
    }

    // ── Galería ───────────────────────────────────────────────────────────────

    fun uploadGalleryImage(uri: Uri) {
        viewModelScope.launch {
            _state.update { it.copy(isUploadingImage = true) }
            runCatching { configRepo.uploadGalleryImage(uri) }
                .onSuccess { url ->
                    val galeria = _state.value.config.galeria.toMutableList()
                    galeria.add(url)
                    _state.update { s ->
                        s.copy(
                            config = s.config.copy(galeria = galeria),
                            isUploadingImage = false
                        )
                    }
                }
                .onFailure { e ->
                    _state.update { it.copy(isUploadingImage = false, error = e.message) }
                }
        }
    }

    fun removeGalleryImage(url: String) {
        val galeria = _state.value.config.galeria.toMutableList()
        galeria.remove(url)
        _state.update { s -> s.copy(config = s.config.copy(galeria = galeria)) }
    }

    fun dismissSaved() {
        _state.update { it.copy(saved = false) }
    }
}

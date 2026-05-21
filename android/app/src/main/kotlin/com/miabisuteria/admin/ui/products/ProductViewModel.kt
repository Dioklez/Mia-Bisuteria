package com.miabisuteri.admin.ui.products

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miabisuteri.admin.data.repository.ProductRepository
import com.miabisuteri.admin.domain.model.Producto
import com.miabisuteri.admin.util.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProductListState(
    val productos: List<Producto> = emptyList(),
    val filtered: List<Producto> = emptyList(),
    val query: String = "",
    val filterTipo: String? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

data class ProductEditState(
    val producto: Producto = Producto(),
    val isNew: Boolean = true,
    val isSaving: Boolean = false,
    val isUploadingImage: Boolean = false,
    val saved: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repo: ProductRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _listState = MutableStateFlow(ProductListState())
    val listState: StateFlow<ProductListState> = _listState.asStateFlow()

    private val _editState = MutableStateFlow(ProductEditState())
    val editState: StateFlow<ProductEditState> = _editState.asStateFlow()

    private var allProductos: List<Producto> = emptyList()

    fun loadAll() {
        viewModelScope.launch {
            _listState.update { it.copy(isLoading = true) }
            val result = runCatching { repo.getAll() }
            result.onSuccess { list ->
                allProductos = list
                _listState.update { it.copy(productos = list, filtered = list, isLoading = false) }
            }.onFailure { e ->
                _listState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun onSearch(query: String) {
        _listState.update { it.copy(query = query) }
        applyFilter()
    }

    fun onFilterTipo(tipo: String?) {
        _listState.update { it.copy(filterTipo = tipo) }
        applyFilter()
    }

    private fun applyFilter() {
        val state = _listState.value
        val filtered = allProductos.filter { p ->
            (state.query.isEmpty() || p.nombre.contains(state.query, ignoreCase = true)) &&
            (state.filterTipo == null || p.tipo == state.filterTipo)
        }
        _listState.update { it.copy(filtered = filtered) }
    }

    fun loadProduct(id: Int?) {
        viewModelScope.launch {
            // Always fetch fresh list — the edit VM may not have loaded yet
            if (allProductos.isEmpty()) {
                val list = runCatching { repo.getAll() }.getOrElse { emptyList() }
                allProductos = list
            }
            if (id == null) {
                _editState.value = ProductEditState(
                    producto = Producto(id = repo.nextId(allProductos)),
                    isNew = true
                )
            } else {
                val found = allProductos.find { it.id == id }
                _editState.value = ProductEditState(
                    producto = found ?: Producto(id = id),
                    isNew = found == null
                )
            }
        }
    }

    fun onFieldChange(update: (Producto) -> Producto) {
        _editState.update { it.copy(producto = update(it.producto), error = null) }
    }

    fun uploadImage(uri: Uri) {
        viewModelScope.launch {
            _editState.update { it.copy(isUploadingImage = true) }
            runCatching { repo.uploadImage(uri) }
                .onSuccess { url ->
                    _editState.update { s ->
                        s.copy(
                            producto = s.producto.copy(imgs = s.producto.imgs + url),
                            isUploadingImage = false
                        )
                    }
                }
                .onFailure { e ->
                    _editState.update { it.copy(isUploadingImage = false, error = "Error subiendo imagen: ${e.message}") }
                }
        }
    }

    fun removeImage(url: String) {
        _editState.update { s ->
            s.copy(producto = s.producto.copy(imgs = s.producto.imgs - url))
        }
    }

    fun saveProduct() {
        val current = _editState.value.producto
        if (current.nombre.isBlank()) {
            _editState.update { it.copy(error = "El nombre es obligatorio") }
            return
        }

        viewModelScope.launch {
            _editState.update { it.copy(isSaving = true) }
            runCatching {
                // Always fetch fresh to avoid stale empty cache overwriting all products
                val freshList = repo.getAll()
                allProductos = freshList
                if (_editState.value.isNew) {
                    freshList + current
                } else {
                    freshList.map { if (it.id == current.id) current else it }
                }
            }.mapCatching { updated ->
                repo.save(updated)
                updated
            }.onSuccess { updated ->
                allProductos = updated
                _editState.update { it.copy(isSaving = false, saved = true) }
            }.onFailure { e ->
                _editState.update { it.copy(isSaving = false, error = e.message) }
            }
        }
    }

    fun deleteProduct(id: Int) {
        viewModelScope.launch {
            _listState.update { it.copy(isLoading = true) }
            runCatching {
                val freshList = repo.getAll()
                allProductos = freshList
                freshList.filter { it.id != id }
            }.mapCatching { updated ->
                repo.save(updated)
                updated
            }.onSuccess { updated ->
                allProductos = updated
                _listState.update { it.copy(productos = updated, filtered = updated, isLoading = false) }
            }.onFailure { e ->
                _listState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun checkPassword(password: String): Boolean = sessionManager.checkPassword(password)
}

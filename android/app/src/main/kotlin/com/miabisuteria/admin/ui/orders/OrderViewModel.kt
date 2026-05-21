package com.miabisuteri.admin.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miabisuteri.admin.data.repository.OrderRepository
import com.miabisuteri.admin.domain.model.EstadoPedido
import com.miabisuteri.admin.domain.model.Pedido
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OrderListState(
    val pedidos: List<Pedido> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

data class OrderDetailState(
    val pedido: Pedido? = null,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repo: OrderRepository
) : ViewModel() {

    private val _listState = MutableStateFlow(OrderListState())
    val listState: StateFlow<OrderListState> = _listState.asStateFlow()

    private val _detailState = MutableStateFlow(OrderDetailState())
    val detailState: StateFlow<OrderDetailState> = _detailState.asStateFlow()

    init {
        observeOrders()
    }

    private fun observeOrders() {
        viewModelScope.launch {
            repo.observeAll()
                .catch { e -> _listState.update { it.copy(error = e.message, isLoading = false) } }
                .collect { pedidos ->
                    _listState.update { it.copy(pedidos = pedidos, isLoading = false) }
                }
        }
    }

    fun loadDetail(orderId: String) {
        viewModelScope.launch {
            _detailState.update { it.copy(isLoading = true) }
            val pedido = runCatching { repo.get(orderId) }.getOrNull()
            _detailState.update { it.copy(pedido = pedido, isLoading = false) }
        }
    }

    fun updateEstado(orderId: String, estado: EstadoPedido) {
        viewModelScope.launch {
            _detailState.update { it.copy(isSaving = true) }
            runCatching { repo.updateEstado(orderId, estado) }
                .onSuccess {
                    _detailState.update { s ->
                        s.copy(
                            pedido = s.pedido?.copy(estado = estado),
                            isSaving = false
                        )
                    }
                }
                .onFailure { e ->
                    _detailState.update { it.copy(isSaving = false, error = e.message) }
                }
        }
    }

    // Tab filter helpers
    fun pedidosNuevos(pedidos: List<Pedido>) = pedidos.filter {
        it.estado == EstadoPedido.PENDIENTE_PAGO || it.estado == EstadoPedido.PAGO_CONFIRMADO
    }

    fun pedidosEnProceso(pedidos: List<Pedido>) =
        pedidos.filter { it.estado == EstadoPedido.EN_PROCESO }

    fun pedidosListos(pedidos: List<Pedido>) =
        pedidos.filter { it.estado == EstadoPedido.LISTO }

    fun pedidosHistorial(pedidos: List<Pedido>) =
        pedidos.filter { it.estado == EstadoPedido.CANCELADO }
}

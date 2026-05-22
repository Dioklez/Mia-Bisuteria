package com.miabisuteri.admin.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miabisuteri.admin.data.repository.OrderRepository
import com.miabisuteri.admin.data.repository.ProductRepository
import com.miabisuteri.admin.domain.model.EstadoPedido
import com.miabisuteri.admin.domain.model.GitHubRelease
import com.miabisuteri.admin.data.github.GitHubUpdateDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val totalProductos: Int = 0,
    val pedidosNuevos: Int = 0,
    val pedidosEnProceso: Int = 0,
    val ingresosMes: Long = 0L,
    val updateAvailable: GitHubRelease? = null,
    val isDownloading: Boolean = false,
    val downloadId: Long? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val orderRepo: OrderRepository,
    private val productRepo: ProductRepository,
    private val updateSource: GitHubUpdateDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadData()
        checkForUpdate()
    }

    private fun loadData() {
        viewModelScope.launch {
            // Load products count
            val productos = runCatching { productRepo.getAll() }.getOrElse { emptyList() }

            // Observe orders for real-time stats
            orderRepo.observeAll()
                .collect { pedidos ->
                    val nuevos = pedidos.count {
                        it.estado == EstadoPedido.PENDIENTE_PAGO ||
                        it.estado == EstadoPedido.PAGO_CONFIRMADO
                    }
                    val enProceso = pedidos.count { it.estado == EstadoPedido.EN_PROCESO }

                    // Ingresos del mes: pedidos listos o en proceso con costos calculados
                    val now = System.currentTimeMillis()
                    val startOfMonth = getStartOfMonthMs(now)
                    val ingresosMes = pedidos
                        .filter { p ->
                            (p.estado == EstadoPedido.LISTO || p.estado == EstadoPedido.EN_PROCESO || p.estado == EstadoPedido.EN_FABRICACION)
                            && (p.creadoEn?.toDate()?.time ?: 0L) >= startOfMonth
                        }
                        .sumOf { it.costos?.total ?: 0L }

                    _uiState.update {
                        it.copy(
                            totalProductos = productos.size,
                            pedidosNuevos = nuevos,
                            pedidosEnProceso = enProceso,
                            ingresosMes = ingresosMes,
                            isLoading = false
                        )
                    }
                }
        }
    }

    private fun checkForUpdate() {
        viewModelScope.launch {
            val release = runCatching { updateSource.checkForUpdate() }.getOrNull()
            if (release != null) {
                _uiState.update { it.copy(updateAvailable = release) }
            }
        }
    }

    fun dismissUpdate() {
        _uiState.update { it.copy(updateAvailable = null) }
    }

    fun onDownloadStarted(downloadId: Long) {
        // Keeps updateAvailable so the dialog stays visible showing download progress
        _uiState.update { it.copy(isDownloading = true, downloadId = downloadId) }
    }

    fun onDownloadComplete() {
        _uiState.update { it.copy(isDownloading = false, downloadId = null) }
    }

    private fun getStartOfMonthMs(nowMs: Long): Long {
        val cal = java.util.Calendar.getInstance()
        cal.timeInMillis = nowMs
        cal.set(java.util.Calendar.DAY_OF_MONTH, 1)
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
        cal.set(java.util.Calendar.MINUTE, 0)
        cal.set(java.util.Calendar.SECOND, 0)
        cal.set(java.util.Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
}

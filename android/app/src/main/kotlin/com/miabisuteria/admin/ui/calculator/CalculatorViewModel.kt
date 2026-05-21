package com.miabisuteri.admin.ui.calculator

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miabisuteri.admin.data.repository.OrderRepository
import com.miabisuteri.admin.domain.model.CalculoResultado
import com.miabisuteri.admin.domain.model.CostosPedido
import com.miabisuteri.admin.domain.model.Material
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ceil

data class CalculatorUiState(
    val resultado: CalculoResultado = CalculoResultado(),
    val isSaving: Boolean = false,
    val saved: Boolean = false,
    val error: String? = null,
    val valorHora: Double = 2000.0,
    val horasEstimadas: Double = 1.0,
    val costoEnvio: Double = 0.0,
    val margenPct: Int = 40
)

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val orderRepo: OrderRepository
) : ViewModel() {

    // ── State usando SnapshotStateList — sin re-renders destructivos ──────────
    // Cada material tiene su propia entrada en la lista. Agregar/quitar una fila
    // NO resetea el estado de las otras filas (a diferencia del bug en la web con innerHTML).
    val materiales = mutableStateListOf(Material("", 0.0))

    var horasEstimadas = mutableStateOf(1.0)
        private set
    var valorHora = mutableStateOf(2000.0)
        private set
    var costoEnvio = mutableStateOf(0.0)
        private set
    var margenPct = mutableStateOf(40)
        private set

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    // ── Operaciones de materiales ─────────────────────────────────────────────

    fun addMaterial() {
        materiales.add(Material("", 0.0))
        recalculate()
    }

    fun removeMaterial(index: Int) {
        if (materiales.size > 1) {
            materiales.removeAt(index)
            recalculate()
        }
    }

    fun updateMaterialName(index: Int, name: String) {
        materiales[index] = materiales[index].copy(nombre = name)
        // No need to recalculate — name doesn't affect total
    }

    fun updateMaterialCost(index: Int, cost: Double) {
        materiales[index] = materiales[index].copy(costo = cost)
        recalculate()
    }

    // ── Otros campos ──────────────────────────────────────────────────────────

    fun updateHoras(value: Double) {
        horasEstimadas.value = value
        recalculate()
    }

    fun updateValorHora(value: Double) {
        valorHora.value = value
        recalculate()
    }

    fun updateEnvio(value: Double) {
        costoEnvio.value = value
        recalculate()
    }

    fun updateMargen(value: Int) {
        margenPct.value = value
        recalculate()
    }

    // ── Cálculo ───────────────────────────────────────────────────────────────
    //
    // Algoritmo exacto a la calculadora web (calculadora.html):
    //   totalMateriales = sum(material.costo)
    //   costoTiempo     = horas * valorHora
    //   base            = totalMateriales + costoTiempo
    //   margenMonto     = base * (margenPct / 100)
    //   subtotal        = base + margenMonto
    //   total           = ceil((subtotal + envio) / 100) * 100   ← redondeo a $100 arriba

    fun recalculate() {
        val totalMateriales = materiales.sumOf { it.costo }
        val costoTiempo = horasEstimadas.value * valorHora.value
        val base = totalMateriales + costoTiempo
        val margenMonto = base * (margenPct.value / 100.0)
        val subtotal = base + margenMonto
        val total = ceil((subtotal + costoEnvio.value) / 100.0) * 100.0

        _uiState.update {
            it.copy(
                resultado = CalculoResultado(
                    totalMateriales = totalMateriales,
                    costoTiempo = costoTiempo,
                    margenMonto = margenMonto,
                    costoEnvio = costoEnvio.value,
                    total = total
                )
            )
        }
    }

    // ── Guardar en pedido ─────────────────────────────────────────────────────

    fun saveToOrder(orderId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            val resultado = _uiState.value.resultado
            val costos = CostosPedido(
                materiales = resultado.totalMateriales,
                tiempoHs = horasEstimadas.value,
                margenPct = margenPct.value,
                subtotal = (resultado.totalMateriales + resultado.costoTiempo + resultado.margenMonto).toLong(),
                envio = costoEnvio.value,
                total = resultado.total.toLong()
            )
            runCatching { orderRepo.updateCostos(orderId, costos) }
                .onSuccess { _uiState.update { it.copy(isSaving = false, saved = true) } }
                .onFailure { e -> _uiState.update { it.copy(isSaving = false, error = e.message) } }
        }
    }
}

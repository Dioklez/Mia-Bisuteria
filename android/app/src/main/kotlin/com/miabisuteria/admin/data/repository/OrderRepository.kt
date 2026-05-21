package com.miabisuteri.admin.data.repository

import com.miabisuteri.admin.data.firebase.FirestoreDataSource
import com.miabisuteri.admin.domain.model.CostosPedido
import com.miabisuteri.admin.domain.model.EstadoPedido
import com.miabisuteri.admin.domain.model.Pedido
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val firestore: FirestoreDataSource
) {
    fun observeAll(): Flow<List<Pedido>> = firestore.observePedidos()

    suspend fun get(id: String): Pedido? = firestore.getPedido(id)

    suspend fun updateEstado(id: String, estado: EstadoPedido) =
        firestore.updateEstadoPedido(id, estado)

    suspend fun updateCostos(id: String, costos: CostosPedido) =
        firestore.updateCostosPedido(id, costos)
}

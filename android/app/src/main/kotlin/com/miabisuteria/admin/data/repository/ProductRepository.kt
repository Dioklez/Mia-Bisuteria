package com.miabisuteri.admin.data.repository

import android.net.Uri
import com.miabisuteri.admin.data.firebase.FirestoreDataSource
import com.miabisuteri.admin.data.firebase.StorageDataSource
import com.miabisuteri.admin.domain.model.Producto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val firestore: FirestoreDataSource,
    private val storage: StorageDataSource
) {
    suspend fun getAll(): List<Producto> = firestore.getProductos()

    suspend fun save(productos: List<Producto>) = firestore.saveProductos(productos)

    suspend fun uploadImage(uri: Uri): String = storage.uploadProductImage(uri)

    suspend fun deleteImage(url: String) = storage.deleteImage(url)

    fun nextId(productos: List<Producto>): Int =
        (productos.maxOfOrNull { it.id } ?: -1) + 1
}

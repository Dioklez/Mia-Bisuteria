package com.miabisuteri.admin.data.firebase

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageDataSource @Inject constructor(
    private val storage: FirebaseStorage
) {
    suspend fun uploadProductImage(uri: Uri): String {
        val filename = "images/productos/${UUID.randomUUID()}.jpg"
        val ref = storage.reference.child(filename)
        ref.putFile(uri).await()
        return ref.downloadUrl.await().toString()
    }

    suspend fun uploadGalleryImage(uri: Uri): String {
        val filename = "images/galeria/${UUID.randomUUID()}.jpg"
        val ref = storage.reference.child(filename)
        ref.putFile(uri).await()
        return ref.downloadUrl.await().toString()
    }

    suspend fun deleteImage(url: String) {
        runCatching {
            storage.getReferenceFromUrl(url).delete().await()
        }
    }
}

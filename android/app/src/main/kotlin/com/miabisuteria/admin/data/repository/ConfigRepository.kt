package com.miabisuteri.admin.data.repository

import android.net.Uri
import com.miabisuteri.admin.data.firebase.FirestoreDataSource
import com.miabisuteri.admin.data.firebase.StorageDataSource
import com.miabisuteri.admin.domain.model.ConfigSite
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigRepository @Inject constructor(
    private val firestore: FirestoreDataSource,
    private val storage: StorageDataSource
) {
    suspend fun get(): ConfigSite = firestore.getConfigSite()

    suspend fun save(config: ConfigSite) = firestore.saveConfigSite(config)

    suspend fun uploadGalleryImage(uri: Uri): String = storage.uploadGalleryImage(uri)

    suspend fun deleteImage(url: String) = storage.deleteImage(url)
}

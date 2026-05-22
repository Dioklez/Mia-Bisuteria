package com.miabisuteri.admin

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import coil.Coil
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.google.firebase.messaging.FirebaseMessaging
import com.miabisuteri.admin.data.push.MiaFcmService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MiaAdminApp : Application() {

    override fun onCreate() {
        super.onCreate()
        setupCoil()
        createNotificationChannels()
        subscribeFcmTopics()
        registerFcmToken()
    }

    private fun setupCoil() {
        val imageLoader = ImageLoader.Builder(this)
            .crossfade(300)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(100L * 1024 * 1024)
                    .build()
            }
            .build()
        Coil.setImageLoader(imageLoader)
    }

    private fun subscribeFcmTopics() {
        // Receive a push whenever a new order lands in Firestore
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_ORDERS)
    }

    private fun registerFcmToken() {
        // Ensure the current token is saved to Firestore even if onNewToken wasn't called
        // (e.g., after a fresh install where the token already exists)
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            MiaFcmService.saveTokenToFirestore(this, token)
        }
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ordersChannel = NotificationChannel(
                CHANNEL_ORDERS,
                getString(R.string.channel_orders_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = getString(R.string.channel_orders_desc)
            }

            val nm = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(ordersChannel)
        }
    }

    companion object {
        const val CHANNEL_ORDERS = "orders_channel"
        const val TOPIC_ORDERS = "admin_orders"
    }
}

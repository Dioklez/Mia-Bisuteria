package com.miabisuteri.admin

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MiaAdminApp : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
        subscribeFcmTopics()
    }

    private fun subscribeFcmTopics() {
        // Receive a push whenever a new order lands in Firestore
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_ORDERS)
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

package com.miabisuteri.admin.data.push

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.miabisuteri.admin.MainActivity
import com.miabisuteri.admin.MiaAdminApp
import com.miabisuteri.admin.R

class MiaFcmService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title
            ?: message.data["title"]
            ?: "Nuevo pedido"
        val body = message.notification?.body
            ?: message.data["body"]
            ?: "Tenés un nuevo pedido en Mía Bisutería"

        showNotification(title, body)
    }

    override fun onNewToken(token: String) {
        // Token refresh — in a production app you'd save this to Firestore
        // so the web panel can send targeted pushes to this device.
        // For now, topic-based messaging handles delivery.
    }

    private fun showNotification(title: String, body: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, MiaAdminApp.CHANNEL_ORDERS)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val nm = getSystemService(NotificationManager::class.java)
        nm.notify(System.currentTimeMillis().toInt(), notification)
    }
}

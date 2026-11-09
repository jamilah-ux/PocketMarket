package com.example.pocketmarket.ui.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.pocketmarket.R

object NotificationHelper {

    private const val CHANNEL_ID = "order_status_channel"
    private const val CHANNEL_NAME = "Order Status Updates"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for PocketMarket order processing"
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun showOrderPlacedNotification(context: Context, orderId: String) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Order Confirmed!")
            .setContentText("Your PocketMarket order #$orderId has been placed successfully.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(orderId.hashCode(), builder.build())
    }

    fun shareProduct(context: Context, productTitle: String, productPrice: Double) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Check out $productTitle for $$productPrice on PocketMarket!")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share Product")
        context.startActivity(shareIntent)
    }
}
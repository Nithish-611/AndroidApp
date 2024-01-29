package com.example.famone.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationUtil {
    companion object{
        private const val NOTIFICATION_CHANNEL_ID = "reminder_id"
        private const val NOTIFICATION_CHANNEL_NAME = "My Reminders"
        fun createNotificationChannel(context: Context){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelId = NOTIFICATION_CHANNEL_ID
                val channelName = NOTIFICATION_CHANNEL_NAME
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(channelId, channelName, importance)

                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }

        fun triggerDummyNotification(context: Context){
            val notificationId = 1 // Use a unique ID for each notification

            val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
            val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_menu_share) // Replace with desired icon
                .setContentTitle("My Notification")
                .setContentText("This is the content of the notification.")
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            // Show the notification
            val notificationManager = NotificationManagerCompat.from(context)
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                return
            }
            notificationManager.notify(notificationId, notification)
        }
    }
}
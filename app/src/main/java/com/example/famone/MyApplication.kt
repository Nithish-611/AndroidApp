package com.example.famone

import android.app.Application
import com.example.famone.utils.NotificationUtil

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NotificationUtil.createNotificationChannel(this)
    }
}

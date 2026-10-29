package me.shawaf.themuslimapp.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp
import me.shawaf.themuslimapp.features.prayer.azan.AzanReceiver
import timber.log.Timber
import timber.log.Timber.Forest.plant


@HiltAndroidApp
class TheMuslimApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // This will initialize Timber
        if (me.shawaf.themuslimapp.BuildConfig.DEBUG) {
            plant(Timber.DebugTree())
        }

        createAzanNotificationChannel()
    }

    private fun createAzanNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                AzanReceiver.CHANNEL_ID,
                "Azan & Prayer Times",
                NotificationManager.IMPORTANCE_HIGH,
            ).apply {
                description = "Notifications at prayer times"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }
}
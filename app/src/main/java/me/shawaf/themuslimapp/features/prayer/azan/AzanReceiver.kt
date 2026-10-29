package me.shawaf.themuslimapp.features.prayer.azan

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import me.shawaf.themuslimapp.R

/** Fired by AlarmManager at each prayer time; posts the azan notification. */
class AzanReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val prayerName = intent.getStringExtra(EXTRA_PRAYER) ?: "Prayer"

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_info)
            .setContentTitle("$prayerName")
            .setContentText("It's time for $prayerName prayer · حَيَّ عَلَى الصَّلَاة")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(prayerName.hashCode(), notification)
        } catch (e: SecurityException) {
            // POST_NOTIFICATIONS not granted; nothing to do.
        }
    }

    companion object {
        const val CHANNEL_ID = "azan_channel"
        const val EXTRA_PRAYER = "extra_prayer"
    }
}

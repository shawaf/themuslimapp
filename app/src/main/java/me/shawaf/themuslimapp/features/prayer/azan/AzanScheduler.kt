package me.shawaf.themuslimapp.features.prayer.azan

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import me.shawaf.themuslimapp.data.local.model.PrayerSlot
import me.shawaf.themuslimapp.data.local.prefers.PrayerSettings
import javax.inject.Inject

/** Schedules (and cancels) exact alarms that trigger [AzanReceiver] at prayer times. */
class AzanScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    /** Re-applies alarms for today's prayers based on the current settings. */
    fun reschedule(slots: List<PrayerSlot>, settings: PrayerSettings) {
        slots.filter { it.canAzan }.forEach { slot ->
            val pendingIntent = buildPendingIntent(slot)
            if (settings.isAzanOn(slot.key) && slot.epochMillis > System.currentTimeMillis()) {
                scheduleExact(slot.epochMillis, pendingIntent)
            } else {
                alarmManager.cancel(pendingIntent)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun scheduleExact(timeMillis: Long, pendingIntent: PendingIntent) {
        try {
            val canExact = Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||
                alarmManager.canScheduleExactAlarms()
            if (canExact) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP, timeMillis, pendingIntent,
                )
            } else {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP, timeMillis, pendingIntent,
                )
            }
        } catch (e: SecurityException) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeMillis, pendingIntent)
        }
    }

    private fun buildPendingIntent(slot: PrayerSlot): PendingIntent {
        val intent = Intent(context, AzanReceiver::class.java).apply {
            putExtra(AzanReceiver.EXTRA_PRAYER, slot.nameEn)
        }
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        return PendingIntent.getBroadcast(context, slot.key.hashCode(), intent, flags)
    }
}

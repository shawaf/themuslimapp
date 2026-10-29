package me.shawaf.themuslimapp.features.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

/** Hosts [PrayerWidget] and schedules its periodic refresh while it is on screen. */
class PrayerWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = PrayerWidget()

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        PrayerWidgetWorker.schedule(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        PrayerWidgetWorker.cancel(context)
    }
}

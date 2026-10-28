package me.shawaf.themuslimapp.features.prayer.domain.model

import me.shawaf.themuslimapp.data.local.model.PrayerSlot
import me.shawaf.themuslimapp.data.local.prefers.PrayerSettings
import me.shawaf.themuslimapp.features.prayer.data.GeoPoint

/** Everything the prayer screen needs: resolved location, settings and today's times. */
data class PrayerData(
    val location: GeoPoint,
    val settings: PrayerSettings,
    val slots: List<PrayerSlot>,
    val usingDefaultLocation: Boolean,
)

package me.shawaf.themuslimapp.features.prayer.domain.repository

import me.shawaf.themuslimapp.features.prayer.domain.model.PrayerData

interface PrayerRepository {
    /** Resolves location, computes today's times, (re)schedules azan alarms. */
    suspend fun loadPrayerData(): PrayerData

    /** Toggles azan for one prayer, persists it, reschedules, and returns fresh data. */
    suspend fun setAzanEnabled(prayerKey: String, enabled: Boolean): PrayerData
}

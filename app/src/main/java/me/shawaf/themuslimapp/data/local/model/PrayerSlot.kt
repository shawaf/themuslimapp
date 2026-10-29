package me.shawaf.themuslimapp.data.local.model

/** A single computed prayer (or sunrise) time for a given day. */
data class PrayerSlot(
    val key: String,        // FAJR, SUNRISE, DHUHR, ASR, MAGHRIB, ISHA
    val nameEn: String,
    val nameAr: String,
    val epochMillis: Long,
    val canAzan: Boolean = true,
)

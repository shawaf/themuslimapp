package me.shawaf.themuslimapp.features.prayer.presentation.viewmodel

sealed class PrayerIntent {
    data object Load : PrayerIntent()
    data class ToggleAzan(val prayerKey: String) : PrayerIntent()
}

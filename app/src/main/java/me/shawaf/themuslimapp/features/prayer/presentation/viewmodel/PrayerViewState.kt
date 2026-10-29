package me.shawaf.themuslimapp.features.prayer.presentation.viewmodel

import androidx.compose.runtime.Immutable

@Immutable
data class PrayerSlotUi(
    val key: String,
    val nameEn: String,
    val nameAr: String,
    val timeText: String,
    val isNext: Boolean,
    val canAzan: Boolean,
    val azanOn: Boolean,
)

@Immutable
data class PrayerViewState(
    val dateLabel: String = "",
    val locationLabel: String = "",
    val usingDefaultLocation: Boolean = false,
    val calcMethodLabel: String = "",
    val slots: List<PrayerSlotUi> = emptyList(),
    val nextNameEn: String = "",
    val nextTimeText: String = "",
    val countdown: String = "",
    val isLoading: Boolean = true,
)

package me.shawaf.themuslimapp.features.home.presentation.viewmodel

import androidx.compose.runtime.Immutable

/** A single prayer entry shown on the home timeline. */
@Immutable
data class PrayerTimeUi(
    val name: String,
    val time: String,
    val isNext: Boolean = false,
)

/**
 * UI state for the home dashboard. For Phase 1 the values are placeholder data
 * supplied by the view model; later phases replace them with real prayer-time
 * and location sources.
 */
@Immutable
data class HomeViewState(
    val greetingName: String = "",
    val location: String = "",
    val hijriDate: String = "",
    val nextPrayerName: String = "",
    val nextPrayerTime: String = "",
    val countdown: String = "",
    val sunProgress: Float = 0f,
    val sunriseTime: String = "",
    val sunsetTime: String = "",
    val prayers: List<PrayerTimeUi> = emptyList(),
    val duaArabic: String = "",
    val duaTranslation: String = "",
    val isAzanPlaying: Boolean = false,
)

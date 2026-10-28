package me.shawaf.themuslimapp.features.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * Home dashboard view model (MVI), matching the pattern used by CounterViewModel.
 *
 * Phase 1 serves static placeholder data so the shell is navigable and the
 * layout is reviewable. Phases 2+ inject real prayer-time / location use cases
 * and replace [loadPlaceholderDashboard].
 */
@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _viewState = MutableStateFlow(HomeViewState())
    val viewState: StateFlow<HomeViewState> = _viewState

    init {
        handleIntent(HomeIntent.LoadDashboard)
    }

    fun handleIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.LoadDashboard -> loadPlaceholderDashboard()
            HomeIntent.ToggleAzan -> {
                _viewState.value =
                    _viewState.value.copy(isAzanPlaying = !_viewState.value.isAzanPlaying)
            }
        }
    }

    private fun loadPlaceholderDashboard() {
        _viewState.value = HomeViewState(
            greetingName = "",
            location = "Cairo",
            hijriDate = "١٤ شوال ١٤٤٧",
            nextPrayerName = "Asr",
            nextPrayerTime = "3:42 PM",
            countdown = "in 1h 20m",
            // Fraction of daylight elapsed (sunrise -> now -> sunset).
            sunProgress = 0.79f,
            sunriseTime = "6:02 AM",
            sunsetTime = "6:12 PM",
            prayers = listOf(
                PrayerTimeUi("Fajr", "5:02"),
                PrayerTimeUi("Dhuhr", "12:10"),
                PrayerTimeUi("Asr", "3:42", isNext = true),
                PrayerTimeUi("Maghrib", "5:58"),
                PrayerTimeUi("Isha", "7:20"),
            ),
            duaArabic = "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً",
            duaTranslation = "\"Our Lord, grant us good in this world…\"",
            isAzanPlaying = false,
        )
    }
}

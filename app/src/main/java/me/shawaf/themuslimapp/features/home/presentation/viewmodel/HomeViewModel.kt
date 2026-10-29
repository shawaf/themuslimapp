package me.shawaf.themuslimapp.features.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.shawaf.themuslimapp.features.calendar.domain.HijriCalendarCalculator
import me.shawaf.themuslimapp.features.prayer.domain.usecase.GetPrayerDataUseCase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * Home dashboard view model. Pulls live prayer times (via the prayer feature)
 * and the real Hijri date; the sun-arc progress runs sunrise → now → sunset.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPrayerDataUseCase: GetPrayerDataUseCase,
    private val hijriCalendarCalculator: HijriCalendarCalculator,
) : ViewModel() {

    private val _viewState = MutableStateFlow(HomeViewState())
    val viewState: StateFlow<HomeViewState> = _viewState

    private val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    private val shortFormat = SimpleDateFormat("h:mm", Locale.getDefault())

    init {
        handleIntent(HomeIntent.LoadDashboard)
    }

    fun handleIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.LoadDashboard -> viewModelScope.launch { loadDashboard() }
            HomeIntent.ToggleAzan -> {
                _viewState.value = _viewState.value.copy(isAzanPlaying = !_viewState.value.isAzanPlaying)
            }
        }
    }

    private suspend fun loadDashboard() {
        val data = getPrayerDataUseCase()
        val now = System.currentTimeMillis()
        val slots = data.slots

        val sunrise = slots.firstOrNull { it.key == "SUNRISE" }?.epochMillis
        val sunset = slots.firstOrNull { it.key == "MAGHRIB" }?.epochMillis
        val next = slots.filter { it.canAzan }.firstOrNull { it.epochMillis > now }

        val progress = if (sunrise != null && sunset != null && sunset > sunrise) {
            ((now - sunrise).toFloat() / (sunset - sunrise)).coerceIn(0f, 1f)
        } else {
            0f
        }

        val prayers = slots.filter { it.canAzan }.map {
            PrayerTimeUi(it.nameEn, shortFormat.format(Date(it.epochMillis)), it.key == next?.key)
        }

        _viewState.value = HomeViewState(
            location = data.location.label.ifEmpty { "My location" },
            hijriDate = hijriCalendarCalculator.hijriTodayArabic(),
            nextPrayerName = next?.nameEn.orEmpty(),
            nextPrayerTime = next?.let { timeFormat.format(Date(it.epochMillis)) }.orEmpty(),
            countdown = next?.let { formatCountdown(it.epochMillis - now) }.orEmpty(),
            sunProgress = progress,
            sunriseTime = sunrise?.let { timeFormat.format(Date(it)) }.orEmpty(),
            sunsetTime = sunset?.let { timeFormat.format(Date(it)) }.orEmpty(),
            prayers = prayers,
            duaArabic = "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً",
            duaTranslation = "\"Our Lord, grant us good in this world…\"",
            isAzanPlaying = _viewState.value.isAzanPlaying,
        )
    }

    private fun formatCountdown(diffMillis: Long): String {
        if (diffMillis <= 0) return ""
        val totalMinutes = diffMillis / 60000
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return if (hours > 0) "in ${hours}h ${minutes}m" else "in ${minutes}m"
    }
}

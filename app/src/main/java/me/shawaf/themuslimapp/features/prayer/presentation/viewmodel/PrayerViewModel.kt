package me.shawaf.themuslimapp.features.prayer.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.shawaf.themuslimapp.features.prayer.domain.model.PrayerData
import me.shawaf.themuslimapp.features.prayer.domain.usecase.GetPrayerDataUseCase
import me.shawaf.themuslimapp.features.prayer.domain.usecase.SetAzanEnabledUseCase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PrayerViewModel @Inject constructor(
    private val getPrayerDataUseCase: GetPrayerDataUseCase,
    private val setAzanEnabledUseCase: SetAzanEnabledUseCase,
) : ViewModel() {

    private val _viewState = MutableStateFlow(PrayerViewState())
    val viewState: StateFlow<PrayerViewState> = _viewState

    private val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())

    init {
        handleIntent(PrayerIntent.Load)
    }

    fun handleIntent(intent: PrayerIntent) {
        when (intent) {
            PrayerIntent.Load -> viewModelScope.launch {
                _viewState.value = mapToState(getPrayerDataUseCase())
            }
            is PrayerIntent.ToggleAzan -> viewModelScope.launch {
                val currentlyOn = _viewState.value.slots
                    .firstOrNull { it.key == intent.prayerKey }?.azanOn ?: true
                _viewState.value = mapToState(
                    setAzanEnabledUseCase(intent.prayerKey, !currentlyOn),
                )
            }
        }
    }

    private fun mapToState(data: PrayerData): PrayerViewState {
        val now = System.currentTimeMillis()
        val next = data.slots.filter { it.canAzan }.firstOrNull { it.epochMillis > now }

        val uiSlots = data.slots.map { slot ->
            PrayerSlotUi(
                key = slot.key,
                nameEn = slot.nameEn,
                nameAr = slot.nameAr,
                timeText = timeFormat.format(Date(slot.epochMillis)),
                isNext = slot.key == next?.key,
                canAzan = slot.canAzan,
                azanOn = data.settings.isAzanOn(slot.key),
            )
        }

        val locationLabel = data.location.label.ifEmpty {
            String.format(Locale.US, "%.3f, %.3f", data.location.latitude, data.location.longitude)
        }

        return PrayerViewState(
            locationLabel = locationLabel,
            usingDefaultLocation = data.usingDefaultLocation,
            calcMethodLabel = prettyMethod(data.settings.calcMethod),
            slots = uiSlots,
            nextNameEn = next?.nameEn.orEmpty(),
            nextTimeText = next?.let { timeFormat.format(Date(it.epochMillis)) }.orEmpty(),
            countdown = next?.let { formatCountdown(it.epochMillis - now) }.orEmpty(),
            isLoading = false,
        )
    }

    private fun formatCountdown(diffMillis: Long): String {
        if (diffMillis <= 0) return ""
        val totalMinutes = diffMillis / 60000
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return if (hours > 0) "in ${hours}h ${minutes}m" else "in ${minutes}m"
    }

    private fun prettyMethod(raw: String): String =
        raw.split("_").joinToString(" ") { part ->
            part.lowercase().replaceFirstChar { it.uppercase() }
        }
}

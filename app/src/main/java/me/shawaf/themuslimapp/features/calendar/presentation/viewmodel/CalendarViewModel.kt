package me.shawaf.themuslimapp.features.calendar.presentation.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.shawaf.themuslimapp.features.calendar.domain.HijriCalendarCalculator
import me.shawaf.themuslimapp.features.calendar.domain.model.CalendarMonth
import me.shawaf.themuslimapp.features.calendar.domain.model.IslamicEvent
import javax.inject.Inject

@Immutable
data class CalendarViewState(
    val month: CalendarMonth? = null,
    val events: List<IslamicEvent> = emptyList(),
    val isLoading: Boolean = true,
)

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val calculator: HijriCalendarCalculator,
) : ViewModel() {

    private val _viewState = MutableStateFlow(CalendarViewState())
    val viewState: StateFlow<CalendarViewState> = _viewState

    init {
        viewModelScope.launch {
            _viewState.value = CalendarViewState(
                month = calculator.currentMonth(),
                events = calculator.currentYearEvents(),
                isLoading = false,
            )
        }
    }
}

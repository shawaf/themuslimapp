package me.shawaf.themuslimapp.features.qibla.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.shawaf.themuslimapp.features.qibla.domain.model.QiblaData
import me.shawaf.themuslimapp.features.qibla.domain.usecase.GetQiblaUseCase
import java.util.Locale
import kotlin.math.roundToInt
import javax.inject.Inject

@HiltViewModel
class QiblaViewModel @Inject constructor(
    private val getQiblaUseCase: GetQiblaUseCase,
) : ViewModel() {

    private val _viewState = MutableStateFlow(QiblaViewState())
    val viewState: StateFlow<QiblaViewState> = _viewState

    init {
        handleIntent(QiblaIntent.Load)
    }

    fun handleIntent(intent: QiblaIntent) {
        when (intent) {
            QiblaIntent.Load -> viewModelScope.launch {
                _viewState.value = mapToState(getQiblaUseCase())
            }
        }
    }

    private fun mapToState(data: QiblaData): QiblaViewState {
        val label = data.location.label.ifEmpty {
            String.format(Locale.US, "%.3f, %.3f", data.location.latitude, data.location.longitude)
        }
        return QiblaViewState(
            bearing = data.bearing.toFloat(),
            distanceKm = data.distanceKm.roundToInt(),
            locationLabel = label,
            usingDefaultLocation = data.usingDefaultLocation,
            isLoading = false,
        )
    }
}

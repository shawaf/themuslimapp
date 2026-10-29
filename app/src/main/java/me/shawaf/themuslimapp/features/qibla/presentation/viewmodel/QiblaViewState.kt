package me.shawaf.themuslimapp.features.qibla.presentation.viewmodel

import androidx.compose.runtime.Immutable

@Immutable
data class QiblaViewState(
    val bearing: Float = 0f,
    val distanceKm: Int = 0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val locationLabel: String = "",
    val usingDefaultLocation: Boolean = false,
    val isLoading: Boolean = true,
)

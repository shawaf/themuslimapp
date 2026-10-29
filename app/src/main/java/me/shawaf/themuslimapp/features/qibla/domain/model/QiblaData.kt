package me.shawaf.themuslimapp.features.qibla.domain.model

import me.shawaf.themuslimapp.features.prayer.data.GeoPoint

data class QiblaData(
    val bearing: Double,
    val distanceKm: Double,
    val location: GeoPoint,
    val usingDefaultLocation: Boolean,
)

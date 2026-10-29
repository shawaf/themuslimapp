package me.shawaf.themuslimapp.features.qibla.domain

import com.batoulapps.adhan.Coordinates
import com.batoulapps.adhan.Qibla
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/** Computes the Qibla bearing and the great-circle distance to the Kaaba. */
class QiblaCalculator @Inject constructor() {

    /** Bearing to the Qibla in degrees clockwise from true north. */
    fun bearingToQibla(latitude: Double, longitude: Double): Double =
        Qibla(Coordinates(latitude, longitude)).direction

    /** Great-circle distance to Makkah in kilometres. */
    fun distanceToMakkahKm(latitude: Double, longitude: Double): Double {
        val earthRadiusKm = 6371.0
        val dLat = Math.toRadians(KAABA_LAT - latitude)
        val dLng = Math.toRadians(KAABA_LNG - longitude)
        val a = sin(dLat / 2).pow(2) +
            cos(Math.toRadians(latitude)) * cos(Math.toRadians(KAABA_LAT)) * sin(dLng / 2).pow(2)
        return earthRadiusKm * 2 * atan2(sqrt(a), sqrt(1 - a))
    }

    companion object {
        private const val KAABA_LAT = 21.4225
        private const val KAABA_LNG = 39.8262
    }
}

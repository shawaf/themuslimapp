package me.shawaf.themuslimapp.features.qibla.data.repository

import me.shawaf.themuslimapp.features.prayer.data.DEFAULT_LOCATION
import me.shawaf.themuslimapp.features.prayer.data.LocationProvider
import me.shawaf.themuslimapp.features.qibla.domain.QiblaCalculator
import me.shawaf.themuslimapp.features.qibla.domain.model.QiblaData
import me.shawaf.themuslimapp.features.qibla.domain.repository.QiblaRepository
import javax.inject.Inject

class QiblaRepositoryImp @Inject constructor(
    private val locationProvider: LocationProvider,
    private val calculator: QiblaCalculator,
) : QiblaRepository {

    override suspend fun loadQibla(): QiblaData {
        val resolved = locationProvider.lastKnownLocation()
        val location = resolved ?: DEFAULT_LOCATION
        return QiblaData(
            bearing = calculator.bearingToQibla(location.latitude, location.longitude),
            distanceKm = calculator.distanceToMakkahKm(location.latitude, location.longitude),
            location = location,
            usingDefaultLocation = resolved == null,
        )
    }
}

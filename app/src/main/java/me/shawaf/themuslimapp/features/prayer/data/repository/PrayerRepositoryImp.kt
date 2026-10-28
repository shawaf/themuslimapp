package me.shawaf.themuslimapp.features.prayer.data.repository

import me.shawaf.themuslimapp.data.local.prefers.SharedPreferencesManager
import me.shawaf.themuslimapp.features.prayer.azan.AzanScheduler
import me.shawaf.themuslimapp.features.prayer.data.DEFAULT_LOCATION
import me.shawaf.themuslimapp.features.prayer.data.LocationProvider
import me.shawaf.themuslimapp.features.prayer.domain.PrayerTimesCalculator
import me.shawaf.themuslimapp.features.prayer.domain.model.PrayerData
import me.shawaf.themuslimapp.features.prayer.domain.repository.PrayerRepository
import javax.inject.Inject

class PrayerRepositoryImp @Inject constructor(
    private val locationProvider: LocationProvider,
    private val calculator: PrayerTimesCalculator,
    private val prefs: SharedPreferencesManager,
    private val azanScheduler: AzanScheduler,
) : PrayerRepository {

    override suspend fun loadPrayerData(): PrayerData {
        val resolved = locationProvider.lastKnownLocation()
        val location = resolved ?: DEFAULT_LOCATION
        val settings = prefs.getPrayerSettings()
        val slots = calculator.computeForDay(
            latitude = location.latitude,
            longitude = location.longitude,
            calcMethod = settings.calcMethod,
            madhab = settings.madhab,
        )
        azanScheduler.reschedule(slots, settings)
        return PrayerData(
            location = location,
            settings = settings,
            slots = slots,
            usingDefaultLocation = resolved == null,
        )
    }

    override suspend fun setAzanEnabled(prayerKey: String, enabled: Boolean): PrayerData {
        val current = prefs.getPrayerSettings()
        val updated = current.copy(
            azanEnabled = current.azanEnabled.toMutableMap().apply { put(prayerKey, enabled) },
        )
        prefs.savePrayerSettings(updated)
        return loadPrayerData()
    }
}

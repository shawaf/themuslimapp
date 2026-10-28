package me.shawaf.themuslimapp.features.prayer.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import me.shawaf.themuslimapp.data.local.prefers.SharedPreferencesManager
import me.shawaf.themuslimapp.features.prayer.azan.AzanScheduler
import me.shawaf.themuslimapp.features.prayer.data.LocationProvider
import me.shawaf.themuslimapp.features.prayer.data.repository.PrayerRepositoryImp
import me.shawaf.themuslimapp.features.prayer.domain.PrayerTimesCalculator
import me.shawaf.themuslimapp.features.prayer.domain.repository.PrayerRepository

@Module
@InstallIn(ViewModelComponent::class)
class PrayerModule {

    @Provides
    fun providePrayerRepository(
        locationProvider: LocationProvider,
        calculator: PrayerTimesCalculator,
        prefs: SharedPreferencesManager,
        azanScheduler: AzanScheduler,
    ): PrayerRepository = PrayerRepositoryImp(locationProvider, calculator, prefs, azanScheduler)
}

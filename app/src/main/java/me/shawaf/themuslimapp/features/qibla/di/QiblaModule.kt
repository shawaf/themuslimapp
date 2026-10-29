package me.shawaf.themuslimapp.features.qibla.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import me.shawaf.themuslimapp.features.prayer.data.LocationProvider
import me.shawaf.themuslimapp.features.qibla.data.repository.QiblaRepositoryImp
import me.shawaf.themuslimapp.features.qibla.domain.QiblaCalculator
import me.shawaf.themuslimapp.features.qibla.domain.repository.QiblaRepository

@Module
@InstallIn(ViewModelComponent::class)
class QiblaModule {

    @Provides
    fun provideQiblaRepository(
        locationProvider: LocationProvider,
        calculator: QiblaCalculator,
    ): QiblaRepository = QiblaRepositoryImp(locationProvider, calculator)
}

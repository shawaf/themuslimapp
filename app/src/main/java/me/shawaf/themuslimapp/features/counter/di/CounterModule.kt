package me.shawaf.themuslimapp.features.counter.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import me.shawaf.themuslimapp.data.local.dp.AppDataBase
import me.shawaf.themuslimapp.data.local.dp.dao.ZekrDao
import me.shawaf.themuslimapp.features.counter.data.repository.CounterRepositoryImp
import me.shawaf.themuslimapp.features.counter.domain.repository.CounterRepository
import me.shawaf.themuslimapp.utils.AzkarUtils

@Module
@InstallIn(ViewModelComponent::class)
class CounterModule {

    @Provides
    fun provideCounterRepository(zekrDao: ZekrDao, azkarUtils: AzkarUtils): CounterRepository =
        CounterRepositoryImp(zekrDao, azkarUtils)
}
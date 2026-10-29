package me.shawaf.themuslimapp.features.hadith.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import me.shawaf.themuslimapp.features.hadith.data.repository.HadithRepositoryImp
import me.shawaf.themuslimapp.features.hadith.domain.repository.HadithRepository
import me.shawaf.themuslimapp.utils.HadithUtils

@Module
@InstallIn(ViewModelComponent::class)
class HadithModule {

    @Provides
    fun provideHadithRepository(hadithUtils: HadithUtils): HadithRepository =
        HadithRepositoryImp(hadithUtils)
}

package me.shawaf.themuslimapp.features.duas.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import me.shawaf.themuslimapp.features.duas.data.repository.DuasRepositoryImp
import me.shawaf.themuslimapp.features.duas.domain.repository.DuasRepository
import me.shawaf.themuslimapp.utils.DuaUtils

@Module
@InstallIn(ViewModelComponent::class)
class DuasModule {

    @Provides
    fun provideDuasRepository(duaUtils: DuaUtils): DuasRepository =
        DuasRepositoryImp(duaUtils)
}

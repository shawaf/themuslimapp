package me.shawaf.themuslimapp.features.duas.data.repository

import me.shawaf.themuslimapp.data.local.model.DuaCategory
import me.shawaf.themuslimapp.features.duas.domain.repository.DuasRepository
import me.shawaf.themuslimapp.utils.DuaUtils
import javax.inject.Inject

class DuasRepositoryImp @Inject constructor(
    private val duaUtils: DuaUtils,
) : DuasRepository {

    override suspend fun getDuaCategories(): List<DuaCategory> = duaUtils.loadDuaCategories()
}

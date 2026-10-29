package me.shawaf.themuslimapp.features.hadith.data.repository

import me.shawaf.themuslimapp.data.local.model.HadithCollection
import me.shawaf.themuslimapp.features.hadith.domain.repository.HadithRepository
import me.shawaf.themuslimapp.utils.HadithUtils
import javax.inject.Inject

class HadithRepositoryImp @Inject constructor(
    private val hadithUtils: HadithUtils,
) : HadithRepository {
    override suspend fun getCollections(): List<HadithCollection> = hadithUtils.loadCollections()
}

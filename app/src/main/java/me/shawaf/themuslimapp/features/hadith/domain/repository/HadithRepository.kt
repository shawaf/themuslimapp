package me.shawaf.themuslimapp.features.hadith.domain.repository

import me.shawaf.themuslimapp.data.local.model.HadithCollection

interface HadithRepository {
    suspend fun getCollections(): List<HadithCollection>
}

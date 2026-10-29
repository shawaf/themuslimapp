package me.shawaf.themuslimapp.features.qibla.domain.repository

import me.shawaf.themuslimapp.features.qibla.domain.model.QiblaData

interface QiblaRepository {
    suspend fun loadQibla(): QiblaData
}

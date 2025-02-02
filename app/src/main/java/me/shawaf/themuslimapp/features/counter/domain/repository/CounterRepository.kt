package me.shawaf.themuslimapp.features.counter.domain.repository

import kotlinx.coroutines.flow.Flow
import me.shawaf.themuslimapp.data.local.dp.entity.ZekrEntity
import me.shawaf.themuslimapp.data.local.model.ZekrModel

interface CounterRepository {
    suspend fun updateOrInsertZekr(zekrEntity: ZekrEntity)

    suspend fun getSavedZekrList(): Flow<List<ZekrEntity>>

    suspend fun getJsonZekrList(): List<ZekrModel>
}
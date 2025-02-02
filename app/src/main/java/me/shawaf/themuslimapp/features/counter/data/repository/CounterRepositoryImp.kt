package me.shawaf.themuslimapp.features.counter.data.repository

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import me.shawaf.themuslimapp.data.local.dp.dao.ZekrDao
import me.shawaf.themuslimapp.data.local.dp.entity.ZekrEntity
import me.shawaf.themuslimapp.data.local.model.ZekrModel
import me.shawaf.themuslimapp.data.local.prefers.ConfigModel
import me.shawaf.themuslimapp.features.counter.domain.repository.CounterRepository
import me.shawaf.themuslimapp.utils.AzkarUtils
import javax.inject.Inject

class CounterRepositoryImp @Inject constructor(
    private val zekrDao: ZekrDao, private val azkarUtils: AzkarUtils,
) : CounterRepository {

    override suspend fun updateOrInsertZekr(zekrEntity: ZekrEntity) {
        zekrDao.insertZekr(zekrEntity)
    }

    override suspend fun getSavedZekrList(): Flow<List<ZekrEntity>> {
        return zekrDao.getAllZekr()
    }

    override suspend fun getJsonZekrList(): List<ZekrModel> {
        return azkarUtils.loadAzkar()
    }
}
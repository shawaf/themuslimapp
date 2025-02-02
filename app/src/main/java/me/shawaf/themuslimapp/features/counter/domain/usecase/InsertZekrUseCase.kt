package me.shawaf.themuslimapp.features.counter.domain.usecase

import me.shawaf.themuslimapp.data.local.dp.entity.ZekrEntity
import me.shawaf.themuslimapp.features.counter.domain.repository.CounterRepository
import javax.inject.Inject

class InsertZekrUseCase @Inject constructor(
    private val counterRepository: CounterRepository
) {
    suspend operator fun invoke(zekrEntity: ZekrEntity) {
        counterRepository.updateOrInsertZekr(zekrEntity)
    }
}
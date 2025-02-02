package me.shawaf.themuslimapp.features.counter.domain.usecase

import me.shawaf.themuslimapp.features.counter.domain.repository.CounterRepository
import javax.inject.Inject

class GetSavedZekrListUseCase @Inject constructor(
    private val counterRepository: CounterRepository
){
    suspend operator fun invoke() = counterRepository.getSavedZekrList()
}
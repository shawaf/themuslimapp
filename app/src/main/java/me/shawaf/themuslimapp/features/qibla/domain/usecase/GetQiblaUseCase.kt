package me.shawaf.themuslimapp.features.qibla.domain.usecase

import me.shawaf.themuslimapp.features.qibla.domain.repository.QiblaRepository
import javax.inject.Inject

class GetQiblaUseCase @Inject constructor(
    private val qiblaRepository: QiblaRepository,
) {
    suspend operator fun invoke() = qiblaRepository.loadQibla()
}

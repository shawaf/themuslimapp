package me.shawaf.themuslimapp.features.prayer.domain.usecase

import me.shawaf.themuslimapp.features.prayer.domain.repository.PrayerRepository
import javax.inject.Inject

class GetPrayerDataUseCase @Inject constructor(
    private val prayerRepository: PrayerRepository,
) {
    suspend operator fun invoke() = prayerRepository.loadPrayerData()
}

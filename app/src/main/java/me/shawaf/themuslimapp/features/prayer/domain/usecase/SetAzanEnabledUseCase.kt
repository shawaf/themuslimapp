package me.shawaf.themuslimapp.features.prayer.domain.usecase

import me.shawaf.themuslimapp.features.prayer.domain.repository.PrayerRepository
import javax.inject.Inject

class SetAzanEnabledUseCase @Inject constructor(
    private val prayerRepository: PrayerRepository,
) {
    suspend operator fun invoke(prayerKey: String, enabled: Boolean) =
        prayerRepository.setAzanEnabled(prayerKey, enabled)
}

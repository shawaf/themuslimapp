package me.shawaf.themuslimapp.features.hadith.domain.usecase

import me.shawaf.themuslimapp.features.hadith.domain.repository.HadithRepository
import javax.inject.Inject

class GetHadithCollectionsUseCase @Inject constructor(
    private val hadithRepository: HadithRepository,
) {
    suspend operator fun invoke() = hadithRepository.getCollections()
}

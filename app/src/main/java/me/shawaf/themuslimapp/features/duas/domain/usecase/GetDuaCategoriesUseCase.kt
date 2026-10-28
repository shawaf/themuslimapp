package me.shawaf.themuslimapp.features.duas.domain.usecase

import me.shawaf.themuslimapp.features.duas.domain.repository.DuasRepository
import javax.inject.Inject

class GetDuaCategoriesUseCase @Inject constructor(
    private val duasRepository: DuasRepository,
) {
    suspend operator fun invoke() = duasRepository.getDuaCategories()
}

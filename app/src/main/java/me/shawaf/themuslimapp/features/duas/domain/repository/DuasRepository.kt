package me.shawaf.themuslimapp.features.duas.domain.repository

import me.shawaf.themuslimapp.data.local.model.DuaCategory

interface DuasRepository {
    suspend fun getDuaCategories(): List<DuaCategory>
}

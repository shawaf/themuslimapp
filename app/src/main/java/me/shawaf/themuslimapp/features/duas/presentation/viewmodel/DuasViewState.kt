package me.shawaf.themuslimapp.features.duas.presentation.viewmodel

import androidx.compose.runtime.Immutable
import me.shawaf.themuslimapp.data.local.model.DuaCategory
import me.shawaf.themuslimapp.data.local.model.DuaItem

@Immutable
data class DuasViewState(
    val categories: List<DuaCategory> = emptyList(),
    val selectedCategoryId: String = "",
    val searchQuery: String = "",
    val isLoading: Boolean = true,
) {
    /** Duas of the selected category filtered by the current search query. */
    val visibleDuas: List<DuaItem>
        get() {
            val category = categories.firstOrNull { it.id == selectedCategoryId }
                ?: categories.firstOrNull()
            val duas = category?.duas.orEmpty()
            if (searchQuery.isBlank()) return duas
            val q = searchQuery.trim()
            return duas.filter {
                it.ar.contains(q, ignoreCase = true) ||
                    it.en.contains(q, ignoreCase = true) ||
                    it.translit.contains(q, ignoreCase = true)
            }
        }
}

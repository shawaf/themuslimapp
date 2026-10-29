package me.shawaf.themuslimapp.features.duas.presentation.viewmodel

import androidx.compose.runtime.Immutable
import me.shawaf.themuslimapp.data.local.model.AdhkarCategory
import me.shawaf.themuslimapp.data.local.model.AdhkarItem

@Immutable
data class DuasViewState(
    val adhkar: List<AdhkarItem> = emptyList(),
    val categories: List<AdhkarCategory> = emptyList(),
    val selectedCategoryId: String = ALL,
    val searchQuery: String = "",
    val isLoading: Boolean = true,
) {
    /** Adhkar of the selected category filtered by the current search query. */
    val visible: List<AdhkarItem>
        get() {
            val byCategory = if (selectedCategoryId == ALL) adhkar
            else adhkar.filter { it.categoryId == selectedCategoryId }
            val q = searchQuery.trim()
            if (q.isBlank()) return byCategory
            return byCategory.filter {
                it.ar.contains(q, ignoreCase = true) ||
                    it.en.contains(q, ignoreCase = true) ||
                    it.translit.contains(q, ignoreCase = true)
            }
        }

    companion object {
        const val ALL = "all"
    }
}

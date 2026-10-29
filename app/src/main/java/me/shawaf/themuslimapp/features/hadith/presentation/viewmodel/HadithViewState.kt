package me.shawaf.themuslimapp.features.hadith.presentation.viewmodel

import androidx.compose.runtime.Immutable
import me.shawaf.themuslimapp.data.local.model.HadithCollection
import me.shawaf.themuslimapp.data.local.model.HadithItem

@Immutable
data class HadithViewState(
    val collections: List<HadithCollection> = emptyList(),
    val selectedCollectionId: String = "",
    val isLoading: Boolean = true,
) {
    val selectedCollection: HadithCollection?
        get() = collections.firstOrNull { it.id == selectedCollectionId } ?: collections.firstOrNull()

    val hadiths: List<HadithItem>
        get() = selectedCollection?.hadiths.orEmpty()
}

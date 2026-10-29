package me.shawaf.themuslimapp.features.hadith.presentation.viewmodel

sealed class HadithIntent {
    data object LoadHadith : HadithIntent()
    data class SelectCollection(val collectionId: String) : HadithIntent()
}

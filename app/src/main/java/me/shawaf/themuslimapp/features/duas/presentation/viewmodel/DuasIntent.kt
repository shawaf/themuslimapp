package me.shawaf.themuslimapp.features.duas.presentation.viewmodel

sealed class DuasIntent {
    data object LoadDuas : DuasIntent()
    data class SelectCategory(val categoryId: String) : DuasIntent()
    data class Search(val query: String) : DuasIntent()
}

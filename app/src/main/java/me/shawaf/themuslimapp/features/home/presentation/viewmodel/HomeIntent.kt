package me.shawaf.themuslimapp.features.home.presentation.viewmodel

sealed class HomeIntent {
    data object LoadDashboard : HomeIntent()
    data object ToggleAzan : HomeIntent()
}

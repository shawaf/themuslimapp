package me.shawaf.themuslimapp.features.qibla.presentation.viewmodel

sealed class QiblaIntent {
    data object Load : QiblaIntent()
}

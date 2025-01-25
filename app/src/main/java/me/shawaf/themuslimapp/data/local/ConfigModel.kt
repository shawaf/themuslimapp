package me.shawaf.themuslimapp.data.local

data class ConfigModel(
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val selectedThemeIndex: Int = 0
)

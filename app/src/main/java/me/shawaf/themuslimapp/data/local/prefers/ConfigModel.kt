package me.shawaf.themuslimapp.data.local.prefers

data class ConfigModel(
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val selectedThemeIndex: Int = 0,
    /** "system" | "light" | "dark" */
    val themeMode: String = "system",
)

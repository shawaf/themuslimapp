package me.shawaf.themuslimapp.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

// Define multiple themes
val PurpleTheme = darkColorScheme(
    primary = Color(0xFFBB86FC),
    secondary = Color(0xFF03DAC6),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E)
)

val BlueTheme = darkColorScheme(
    primary = Color(0xFF2962FF),
    secondary = Color(0xFF03A9F4),
    background = Color(0xFF0D1117),
    surface = Color(0xFF1A1E27)
)

val GreenTheme = darkColorScheme(
    primary = Color(0xFF4CAF50),
    secondary = Color(0xFF8BC34A),
    background = Color(0xFF263238),
    surface = Color(0xFF37474F)
)

val PinkTheme = darkColorScheme(
    primary = Color(0xFFE91E63),
    secondary = Color(0xFF9C27B0),
    background = Color(0xFF121212),
    surface = Color(0xFF1F1B24)
)

val GrayTheme = darkColorScheme(
    primary = Color(0xFF90A4AE),
    secondary = Color(0xFFB0BEC5),
    background = Color(0xFF202124),
    surface = Color(0xFF2C2C2C)
)

val OrangeTheme = darkColorScheme(
    primary = Color(0xFFFF5722),
    secondary = Color(0xFFFFC107),
    background = Color(0xFF121212),
    surface = Color(0xFF1F1B16)
)

val TealTheme = darkColorScheme(
    primary = Color(0xFF0288D1),
    secondary = Color(0xFF26C6DA),
    background = Color(0xFF0A0F1F),
    surface = Color(0xFF0F1A2B)
)

// Map themes for easy access
val ColorThemes = mapOf(
    "Gray" to GrayTheme,
    "Purple" to PurpleTheme,
    "Blue" to BlueTheme,
    "Green" to GreenTheme,
    "Pink" to PinkTheme,
    "Orange" to OrangeTheme,
    "Teal" to TealTheme
)
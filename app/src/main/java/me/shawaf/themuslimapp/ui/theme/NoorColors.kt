package me.shawaf.themuslimapp.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * The "Noor" brand palette. Emerald and gold are brand constants (identical in
 * light and dark); the semantic surface/text colors are defined per-scheme in
 * [Theme] and consumed through MaterialTheme.colorScheme so the screens adapt to
 * light/dark automatically.
 */
object NoorColors {
    // Brand (constant across themes)
    val Emerald = Color(0xFF0F5C4C)
    val EmeraldDark = Color(0xFF0A3D33)
    val Gold = Color(0xFFC8A25A)
    val GoldLight = Color(0xFFE7CE9A)
    val Success = Color(0xFF128A52)

    // Light scheme semantic values
    val Ivory = Color(0xFFF4F1E9)
    val IvoryDim = Color(0xFFEDE8DC)
    val Surface = Color(0xFFFFFFFF)
    val Ink = Color(0xFF16231E)
    val Muted = Color(0xFF6B7A72)
    val SurfaceTint = Color(0xFFE9F0EC)
    val Line = Color(0xFFECE7DA)

    // Dark scheme semantic values
    val DarkBackground = Color(0xFF0E1714)
    val DarkSurface = Color(0xFF17241F)
    val DarkInk = Color(0xFFEEF1EC)
    val DarkMuted = Color(0xFF9DB0A6)
    val DarkSurfaceTint = Color(0xFF21332C)
    val DarkLine = Color(0xFF2A3A33)
}

package me.shawaf.themuslimapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val NoorLightScheme = lightColorScheme(
    primary = NoorColors.Emerald,
    onPrimary = Color.White,
    secondary = NoorColors.Gold,
    onSecondary = NoorColors.EmeraldDark,
    background = NoorColors.Ivory,
    onBackground = NoorColors.Ink,
    surface = NoorColors.Surface,
    onSurface = NoorColors.Ink,
    surfaceVariant = NoorColors.SurfaceTint,
    onSurfaceVariant = NoorColors.Muted,
    outline = NoorColors.Line,
)

private val NoorDarkScheme = darkColorScheme(
    primary = NoorColors.Emerald,
    onPrimary = Color.White,
    secondary = NoorColors.Gold,
    onSecondary = NoorColors.EmeraldDark,
    background = NoorColors.DarkBackground,
    onBackground = NoorColors.DarkInk,
    surface = NoorColors.DarkSurface,
    onSurface = NoorColors.DarkInk,
    surfaceVariant = NoorColors.DarkSurfaceTint,
    onSurfaceVariant = NoorColors.DarkMuted,
    outline = NoorColors.DarkLine,
)

@Composable
fun TheMuslimAppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) NoorDarkScheme else NoorLightScheme,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}

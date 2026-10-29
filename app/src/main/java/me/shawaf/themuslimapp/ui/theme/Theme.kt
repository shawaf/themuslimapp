package me.shawaf.themuslimapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/** The single "Noor" light color scheme — no theme switching. */
private val NoorColorScheme = lightColorScheme(
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

@Composable
fun TheMuslimAppTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = NoorColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}

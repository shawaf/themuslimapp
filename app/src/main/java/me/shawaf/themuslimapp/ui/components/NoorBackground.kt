package me.shawaf.themuslimapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import me.shawaf.themuslimapp.ui.theme.NoorColors

/**
 * App-wide ivory background with a very subtle arabesque motif in the top area,
 * echoing the pattern from the design.
 */
@Composable
fun NoorBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NoorColors.Ivory),
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val tile = 46f * density
            val color = NoorColors.Emerald.copy(alpha = 0.04f)
            val stroke = Stroke(width = 1f)
            val maxY = size.height * 0.42f
            var row = 0
            var y = -tile
            while (y < maxY) {
                var x = if (row % 2 == 0) -tile else -tile / 2f
                while (x < size.width + tile) {
                    val cx = x + tile / 2f
                    val cy = y + tile / 2f
                    val diamond = Path().apply {
                        moveTo(cx, y)
                        lineTo(x + tile, cy)
                        lineTo(cx, y + tile)
                        lineTo(x, cy)
                        close()
                    }
                    drawPath(diamond, color, style = stroke)
                    x += tile
                }
                y += tile
                row++
            }
        }
        content()
    }
}

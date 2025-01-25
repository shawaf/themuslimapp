package me.shawaf.themuslimapp.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

@Composable
fun ActionButtonWithState(
    iconSrc: Int, contentDesc: String, state: Boolean, onClickButton: () -> Unit
) {
    val backgroundColor = if (state) MaterialTheme.colorScheme.primary else Color.DarkGray
    IconButton(onClick = onClickButton) {
        Icon(painter = painterResource(iconSrc),
            contentDescription = contentDesc,
            tint = Color.White,
            modifier = Modifier.drawBehind {
                drawCircle(
                    color = backgroundColor, radius = this.size.maxDimension
                )
            })
    }
}
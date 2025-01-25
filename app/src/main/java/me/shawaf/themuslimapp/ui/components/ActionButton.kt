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
fun ActionButton(iconSrc: Int, contentDesc: String, onClickButton: () -> Unit) {
    val backgroundColor = MaterialTheme.colorScheme.primary
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
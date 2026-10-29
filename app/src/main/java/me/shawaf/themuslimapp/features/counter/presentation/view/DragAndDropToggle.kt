package me.shawaf.themuslimapp.features.counter.presentation.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import me.shawaf.themuslimapp.ui.theme.NoorColors

@Composable
fun DragAndDropToggle(
    trackWidth: Dp = 270.dp,
    trackHeight: Dp = 72.dp,
    circleDiameter: Dp = 60.dp,
    onComplete: () -> Unit,
) {
    val pad = 6.dp
    var offsetX by remember { mutableStateOf(0f) }
    val maxOffset = with(LocalDensity.current) { trackWidth.toPx() - circleDiameter.toPx() - (2 * pad.toPx()) }
    val maxOffsetForDragging = maxOffset * 0.75f
    val animatableOffsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .width(trackWidth)
            .height(trackHeight)
            .clip(RoundedCornerShape(50.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(pad)
            .pointerInput(Unit) {
                detectDragGestures(onDragEnd = {
                    if (offsetX >= maxOffsetForDragging) {
                        onComplete()
                    }
                    scope.launch {
                        animatableOffsetX.animateTo(0f, animationSpec = tween(300))
                    }
                    offsetX = 0f
                }, onDrag = { change, dragAmount ->
                    change.consume()
                    scope.launch {
                        offsetX = (offsetX + dragAmount.x).coerceIn(0f, maxOffset)
                        animatableOffsetX.snapTo(offsetX)
                    }
                })
            },
    ) {
        Text(
            text = "Slide to count  ⟩⟩",
            color = Color.White.copy(alpha = 0.65f),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.Center),
        )
        Box(
            modifier = Modifier
                .size(circleDiameter)
                .offset {
                    val verticalOffset = ((trackHeight - circleDiameter - (pad * 2)) / 2).toPx().toInt()
                    IntOffset(animatableOffsetX.value.toInt(), verticalOffset)
                }
                .shadow(6.dp, CircleShape)
                .background(NoorColors.Gold, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Count",
                tint = NoorColors.EmeraldDark,
                modifier = Modifier.size(28.dp),
            )
        }
    }
}

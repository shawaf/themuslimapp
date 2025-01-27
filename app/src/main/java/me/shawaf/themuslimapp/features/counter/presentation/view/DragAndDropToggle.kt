package me.shawaf.themuslimapp.features.counter.presentation.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun DragAndDropToggle(
    trackWidth: Dp = 240.dp,
    trackHeight: Dp = 90.dp,
    circleDiameter: Dp = 80.dp,
    trackColor: Color = MaterialTheme.colorScheme.secondary,
    circleColor: Color = MaterialTheme.colorScheme.primary,
    onComplete: () -> Unit
) {
    var offsetX by remember { mutableStateOf(0f) }
    val maxOffset = with(LocalDensity.current) { trackWidth.toPx() - circleDiameter.toPx() }
    val maxOffsetForDragging = with(LocalDensity.current) { trackWidth.toPx() - (2*circleDiameter.toPx()) }
    val animatableOffsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .width(trackWidth)
            .height(trackHeight)
            .shadow(elevation = 10.dp, spotColor = Color.White, shape = RoundedCornerShape(50.dp))
            .background(trackColor, RoundedCornerShape(50.dp))
            .pointerInput(Unit) {
                detectDragGestures(onDragEnd = {
                    // Check if dragged to the right end
                    if (offsetX >= maxOffsetForDragging) {
                        onComplete()
                    }
                    // Animate back to starting position
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
            }
    ) {
        Box(
            modifier = Modifier
                .size(circleDiameter)
                .offset {
                    val verticalOffset = ((trackHeight - circleDiameter) / 2)
                        .toPx()
                        .toInt()
                    IntOffset(animatableOffsetX.value.toInt(), verticalOffset)
                }
                .background(circleColor, CircleShape)
        )
    }
}
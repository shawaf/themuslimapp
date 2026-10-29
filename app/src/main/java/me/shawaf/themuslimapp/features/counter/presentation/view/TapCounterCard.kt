package me.shawaf.themuslimapp.features.counter.presentation.view

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.shawaf.themuslimapp.R
import me.shawaf.themuslimapp.ui.theme.AmiriFontFamily
import me.shawaf.themuslimapp.ui.theme.NoorColors

/**
 * The Sebha counter surface: a full-screen card you tap to count. A segmented
 * progress bar across the top tracks every dhikr card (completed, current fill,
 * upcoming). The dhikr sits in the middle of the card, with the current count /
 * target and the "tap to count" hint as small text inside the card. Tapping, and
 * switching to the next dhikr, are animated.
 */
@Composable
fun TapCounterCard(
    zekrText: String,
    count: Int,
    target: Int,
    totalCards: Int,
    currentIndex: Int,
    completed: Boolean = false,
    onTap: () -> Unit,
) {
    val fraction = if (target > 0) (count.coerceAtMost(target).toFloat() / target) else 0f

    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "tapScale",
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp, vertical = 6.dp),
    ) {
        // Segmented progress — one segment per dhikr card, on top of the card.
        SegmentedProgress(
            total = totalCards,
            currentIndex = currentIndex,
            currentFraction = fraction,
        )

        Spacer(Modifier.height(16.dp))

        // Full-screen tappable card. Dhikr in the middle, count + hint inside.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .shadow(10.dp, RoundedCornerShape(28.dp), clip = false)
                .clip(RoundedCornerShape(28.dp))
                .background(MaterialTheme.colorScheme.surface)
                .clickable(interactionSource = interaction, indication = null, onClick = onTap)
                .padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                AnimatedContent(
                    targetState = zekrText,
                    transitionSpec = {
                        (slideInVertically { it / 3 } + fadeIn(tween(350))) togetherWith
                            (slideOutVertically { -it / 3 } + fadeOut(tween(250)))
                    },
                    label = "zekrSwitch",
                ) { text ->
                    Text(
                        text = text,
                        textAlign = TextAlign.Center,
                        fontFamily = AmiriFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        lineHeight = 44.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            // Count / target — small, inside the card, with a pop on each tick.
            Row(verticalAlignment = Alignment.CenterVertically) {
                AnimatedContent(
                    targetState = count,
                    transitionSpec = {
                        (scaleIn(initialScale = 0.7f, animationSpec = tween(200)) + fadeIn(tween(200))) togetherWith
                            (scaleOut(targetScale = 1.2f, animationSpec = tween(140)) + fadeOut(tween(140)))
                    },
                    label = "countPop",
                ) { c ->
                    Text(
                        text = "$c",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                Text(
                    text = " / $target",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(Modifier.height(4.dp))

            // Hint / completion state — small, inside the card.
            if (completed) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(
                        Icons.Default.Check, contentDescription = null,
                        tint = NoorColors.Emerald, modifier = Modifier.size(14.dp),
                    )
                    Text(
                        text = stringResource(R.string.completed),
                        fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NoorColors.Emerald,
                    )
                }
            } else {
                Text(
                    text = stringResource(R.string.tap_to_count),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun SegmentedProgress(total: Int, currentIndex: Int, currentFraction: Float) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        val count = total.coerceAtLeast(1)
        repeat(count) { i ->
            val target = when {
                i < currentIndex -> 1f
                i == currentIndex -> currentFraction
                else -> 0f
            }
            val fill by animateFloatAsState(target, tween(300), label = "seg$i")
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fill)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(4.dp))
                        .background(Brush.horizontalGradient(listOf(NoorColors.Gold, NoorColors.GoldLight))),
                )
            }
        }
    }
}

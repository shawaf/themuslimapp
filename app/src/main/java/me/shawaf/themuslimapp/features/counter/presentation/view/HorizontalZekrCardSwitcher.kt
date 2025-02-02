package me.shawaf.themuslimapp.features.counter.presentation.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import me.shawaf.themuslimapp.data.local.dp.entity.ZekrEntity
import kotlinx.coroutines.flow.distinctUntilChanged
import me.shawaf.themuslimapp.data.local.model.ZekrModel
import me.shawaf.themuslimapp.ui.theme.cardElevation

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun HorizontalCardSwitcher(
    zekrList: List<ZekrModel>, nextItem: Boolean, onSwitchCard: (ZekrModel) -> Unit
) {
    val cardWidth = 300.dp
    val cardSpacing = 16.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val horizontalPadding = (screenWidth - cardWidth - cardSpacing - cardSpacing) / 2
    val lazyListState = rememberLazyListState()

    // Track the focused item
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemIndex }.distinctUntilChanged() // Avoid multiple triggers for the same index
            .collect { index ->
                onSwitchCard(zekrList[index])
            }
    }

    // Switch to next Card when nextItem is true
    LaunchedEffect(nextItem) {
        if (nextItem) {
            val nextIndex =
                (lazyListState.firstVisibleItemIndex + 1).coerceAtMost(zekrList.size - 1)
            // Scroll to the next item
            lazyListState.animateScrollToItem(nextIndex)
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        LazyRow(
            state = lazyListState,
            contentPadding = PaddingValues(horizontal = horizontalPadding),
            horizontalArrangement = Arrangement.spacedBy(cardSpacing),
            userScrollEnabled = false
        ) {
            items(zekrList) { card ->
                Card(
                    modifier = Modifier
                        .width(cardWidth)
                        .height(300.dp),
                    shape = MaterialTheme.shapes.medium,
                    elevation = CardDefaults.cardElevation(cardElevation),
                ) {
                    ZekrView(zekrModel = card)
                }
            }
        }
    }
}
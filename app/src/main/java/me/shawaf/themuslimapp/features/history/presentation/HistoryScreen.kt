package me.shawaf.themuslimapp.features.history.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import me.shawaf.themuslimapp.R
import me.shawaf.themuslimapp.features.history.presentation.model.HistoryFilter
import me.shawaf.themuslimapp.features.history.presentation.view.HistoryDayItem
import me.shawaf.themuslimapp.features.history.presentation.view.HistoryZekrItem
import me.shawaf.themuslimapp.features.history.utils.HistoryUtils
import me.shawaf.themuslimapp.ui.components.TitleAppScaffold
import me.shawaf.themuslimapp.ui.theme.bigScreenPadding
import me.shawaf.themuslimapp.ui.theme.cardSpacing
import me.shawaf.themuslimapp.ui.theme.horizontalPadding

@Composable
fun HistoryScreen(historyViewModel: HistoryViewModel, onNavigateBack: () -> Unit) {

    val itemsList by historyViewModel.savedZekrList.collectAsState()
    val filter by historyViewModel.selectedFilter

    TitleAppScaffold(
        title = LocalContext.current.getString(R.string.history_title),
        onBackPressed = onNavigateBack
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Column(
                modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = bigScreenPadding)
                        .height(40.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { historyViewModel.changeSelectedFilter(HistoryFilter.BY_DAY) },
                            colors = ButtonDefaults.buttonColors(if (filter == HistoryFilter.BY_DAY) MaterialTheme.colorScheme.secondary else Color.Black),
                            modifier = Modifier.weight(1F)
                        ) {
                            Text(
                                text = LocalContext.current.getString(R.string.by_day),
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }

//                        VerticalDivider(color = Color.White, thickness = .5.dp)

                        Button(
                            onClick = { historyViewModel.changeSelectedFilter(HistoryFilter.BY_ZEKR) },
                            colors = ButtonDefaults.buttonColors(if (filter == HistoryFilter.BY_ZEKR) MaterialTheme.colorScheme.secondary else Color.Black),
                            modifier = Modifier.weight(1F)
                        ) {
                            Text(
                                text = LocalContext.current.getString(R.string.by_zekr),
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(cardSpacing),
                    modifier = Modifier.padding(horizontal = horizontalPadding)
                ) {
                    if (filter == HistoryFilter.BY_DAY) {
                        (items(HistoryUtils.extractZekrByDay(itemsList).reversed()) { item ->
                            HistoryDayItem(item)
                        })
                    } else {
                        (items(HistoryUtils.extractZekrByType(itemsList).reversed()) { item ->
                            HistoryZekrItem(item)
                        })
                    }

                }
            }
        }
    }
}
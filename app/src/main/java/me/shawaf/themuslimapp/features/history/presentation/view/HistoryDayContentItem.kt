package me.shawaf.themuslimapp.features.history.presentation.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import me.shawaf.themuslimapp.features.history.presentation.model.ZekrCount

@Composable
fun HistoryDayContentItem(zekrCountItem: ZekrCount, modifier: Modifier){
    Text(
        text = "${zekrCountItem.zekr.take(20)} : ${zekrCountItem.count}",
        modifier = modifier,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Start,
        maxLines = 10,
    )
}
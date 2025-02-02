package me.shawaf.themuslimapp.features.history.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.shawaf.themuslimapp.features.history.presentation.model.HistoryDayUIModel
import me.shawaf.themuslimapp.ui.theme.cardElevation
import me.shawaf.themuslimapp.ui.theme.cardTextPadding

@Composable
fun HistoryDayItem(historyDayModel: HistoryDayUIModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(cardElevation),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(cardTextPadding),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = historyDayModel.day,
                style = MaterialTheme.typography.titleSmall,
                color = Color.White,
                textAlign = TextAlign.Start,
                maxLines = 10

            )
            val zekrCountList = historyDayModel.zekrCountList
            for (i in zekrCountList.indices step 2) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().height(30.dp)
                ) {
                    HistoryDayContentItem(zekrCountList[i], modifier = Modifier.weight(1F))
                    if(i+1 < zekrCountList.size) {
                        HistoryDayContentItem(zekrCountList[i+1],modifier = Modifier.weight(1F))
                    }
                }
            }
        }
    }
}
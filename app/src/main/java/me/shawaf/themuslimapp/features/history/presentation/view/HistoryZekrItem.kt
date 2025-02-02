package me.shawaf.themuslimapp.features.history.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import me.shawaf.themuslimapp.R
import me.shawaf.themuslimapp.features.history.presentation.model.HistoryZekrUIModel
import me.shawaf.themuslimapp.ui.theme.cardElevation
import me.shawaf.themuslimapp.ui.theme.cardTextPadding

@Composable
fun HistoryZekrItem(historyZekrUIModel: HistoryZekrUIModel) {
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
                text = historyZekrUIModel.zekr,
                style = MaterialTheme.typography.titleSmall,
                color = Color.White,
                textAlign = TextAlign.Start,
                maxLines = 10

            )

            Text(
                text = LocalContext.current.getString(
                    R.string.count_number, "${historyZekrUIModel.count}"
                ),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Start,
                maxLines = 10

            )
        }
    }
}
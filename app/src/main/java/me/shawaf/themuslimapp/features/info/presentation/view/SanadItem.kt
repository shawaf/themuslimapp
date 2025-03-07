package me.shawaf.themuslimapp.features.info.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.sp
import me.shawaf.themuslimapp.data.local.dp.entity.ZekrEntity
import me.shawaf.themuslimapp.data.local.model.ZekrModel
import me.shawaf.themuslimapp.ui.theme.Typography
import me.shawaf.themuslimapp.ui.theme.cardElevation
import me.shawaf.themuslimapp.ui.theme.cardTextPadding

@Composable
fun SanadItem(zekrModel: ZekrModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(cardElevation),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(cardTextPadding),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = zekrModel.zekr,
                textAlign = TextAlign.Start,
                style = Typography.titleSmall,
                color = Color.White,
                maxLines = 10,
            )

            Text(
                text = "السند : ${zekrModel.sanad}",
                textAlign = TextAlign.Start,
                style = Typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
                maxLines = 10,
            )

        }
    }
}
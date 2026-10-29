package me.shawaf.themuslimapp.features.counter.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import me.shawaf.themuslimapp.data.local.model.ZekrModel
import me.shawaf.themuslimapp.ui.theme.AmiriFontFamily
import me.shawaf.themuslimapp.ui.theme.NoorColors
import me.shawaf.themuslimapp.ui.theme.cardTextPadding

@Composable
fun ZekrView(zekrModel: ZekrModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(cardTextPadding),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = zekrModel.zekr,
            textAlign = TextAlign.Center,
            fontFamily = AmiriFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            lineHeight = 42.sp,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 10,
        )
        Text(
            text = "عدد المرات : ${zekrModel.count}",
            textAlign = TextAlign.Center,
            fontFamily = AmiriFontFamily,
            fontSize = 16.sp,
            color = NoorColors.Gold,
        )
    }
}

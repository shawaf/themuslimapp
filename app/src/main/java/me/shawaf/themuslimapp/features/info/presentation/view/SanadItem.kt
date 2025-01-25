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
import me.shawaf.themuslimapp.features.main.data.local.entity.ZekrEntity
import me.shawaf.themuslimapp.ui.theme.Typography

@Composable
fun SanadItem(zekrEntity: ZekrEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = zekrEntity.zekr,
                textAlign = TextAlign.Start,
                style = Typography.titleMedium,
                color = Color.White,
                maxLines = 10,
            )

            Text(
                text = "السند : ${zekrEntity.sanad}",
                textAlign = TextAlign.Start,
                fontSize = 14.sp,
                style = Typography.titleSmall,
                color = MaterialTheme.colorScheme.secondary,
                maxLines = 10,
            )

        }
    }
}
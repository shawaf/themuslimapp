package me.shawaf.themuslimapp.features.main.presentation.view

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.shawaf.themuslimapp.features.main.data.local.entity.ZekrEntity
import me.shawaf.themuslimapp.ui.theme.Typography

@Composable
fun ZekrItem(zekrEntity: ZekrEntity) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = zekrEntity.zekr,
            textAlign = TextAlign.Center,
            style = Typography.titleMedium,
            color = Color.White,
            maxLines = 10,
        )

        Text(
            text = " عدد المرات : ${zekrEntity.count}",
            textAlign = TextAlign.Left,
            fontSize = 14.sp,
            style = Typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary,
        )

    }

}
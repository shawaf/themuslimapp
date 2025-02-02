package me.shawaf.themuslimapp.features.info.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import me.shawaf.themuslimapp.R
import me.shawaf.themuslimapp.data.local.prefers.ConfigModel
import me.shawaf.themuslimapp.features.info.presentation.view.SanadItem
import me.shawaf.themuslimapp.ui.components.AppScaffold
import me.shawaf.themuslimapp.ui.components.TitleAppScaffold
import me.shawaf.themuslimapp.ui.theme.cardSpacing
import me.shawaf.themuslimapp.ui.theme.horizontalPadding
import me.shawaf.themuslimapp.utils.AzkarUtils

@Composable
fun InfoScreen(viewModel: InfoViewModel, onNavigateBack: () -> Unit) {
    TitleAppScaffold(
        title = LocalContext.current.getString(R.string.info_title), onBackPressed = onNavigateBack
    ) {
        val zekrList by viewModel.savedZekrList.collectAsState()

        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(cardSpacing),
                modifier = Modifier.padding(horizontal = horizontalPadding)
            ) {
                (items(zekrList) { item ->
                    SanadItem(item)
                })
            }
        }
    }
}
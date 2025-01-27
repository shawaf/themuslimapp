package me.shawaf.themuslimapp.features.info.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import me.shawaf.themuslimapp.R
import me.shawaf.themuslimapp.data.local.prefers.ConfigModel
import me.shawaf.themuslimapp.features.info.presentation.view.SanadItem
import me.shawaf.themuslimapp.ui.components.AppScaffold
import me.shawaf.themuslimapp.utils.AzkarUtils

@Composable
fun InfoScreen(onNavigateBack: () -> Unit) {
    AppScaffold(
        LocalContext.current.getString(R.string.sanad),
        configModel = ConfigModel(),
        withBottomBar = false,
        onBackPressed = onNavigateBack,
        onSwitchTheme = null,
        onNavigateToInfo = null,
        onNavigateToHistory = null,
        onToggleSoundEnabledState = null,
        onToggleVibrationEnabledState = null
    ) {
        val context = LocalContext.current
        val cardSpacing = 16.dp
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(cardSpacing),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                (items(AzkarUtils.loadAzkar(context)) { item ->
                    SanadItem(item)
                })
            }
        }
    }
}
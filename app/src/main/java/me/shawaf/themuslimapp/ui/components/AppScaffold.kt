package me.shawaf.themuslimapp.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import me.shawaf.themuslimapp.data.local.prefers.ConfigModel
import me.shawaf.themuslimapp.ui.theme.Typography

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    title: String,
    configModel: ConfigModel,
    withBottomBar: Boolean,
    onBackPressed: (() -> Unit)?,
    onSwitchTheme: (() -> Unit)?,
    onNavigateToInfo: (() -> Unit)?,
    onNavigateToHistory: (() -> Unit)?,
    onToggleSoundEnabledState: (() -> Unit)?,
    onToggleVibrationEnabledState: (() -> Unit)?,
    content: @Composable () -> Unit,
) {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = Color.White,
        ), title = { Text(text = title, style = Typography.titleLarge) }, navigationIcon = {
            if (onBackPressed != null) {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "back"
                    )
                }
            } else null
        })
    }, bottomBar = {
        if (withBottomBar) ActionBottomBar(
            configModel,
            onSwitchTheme,
            onNavigateToInfo,
            onNavigateToHistory,
            onToggleSoundEnabledState,
            onToggleVibrationEnabledState
        ) else null
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .systemBarsPadding()
        ) {
            content()
        }
    }
}
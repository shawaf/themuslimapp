package me.shawaf.themuslimapp.features.counter.presentation.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import me.shawaf.themuslimapp.R
import me.shawaf.themuslimapp.features.counter.presentation.viewmodel.CounterIntent
import me.shawaf.themuslimapp.features.counter.presentation.viewmodel.CounterViewModel
import me.shawaf.themuslimapp.ui.components.ActionButton
import me.shawaf.themuslimapp.ui.components.ActionButtonWithState
import me.shawaf.themuslimapp.ui.components.AppScaffold
import me.shawaf.themuslimapp.utils.SoundUtils
import me.shawaf.themuslimapp.utils.VibrationUtils

@SuppressLint("SuspiciousIndentation")
@Composable
fun CounterScreen(
    counterViewModel: CounterViewModel,
    onBack: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToInfo: () -> Unit
) {
    val counterViewState by counterViewModel.viewState.collectAsState()

    AppScaffold(LocalContext.current.getString(R.string.salat_khatm),
        configModel = counterViewState.configModel,
        onBackPressed = onBack,
        withBottomBar = false,
        onSwitchTheme = null,
        onNavigateToInfo = onNavigateToInfo,
        onNavigateToHistory = onNavigateToHistory,
        onToggleSoundEnabledState = { counterViewModel.handleIntent(CounterIntent.ToggleSoundState) },
        onToggleVibrationEnabledState = { counterViewModel.handleIntent(CounterIntent.ToggleVibrationState) },
        actions = {
            ActionButtonWithState(
                iconSrc = R.drawable.ic_sound_on,
                contentDesc = "Sound",
                state = counterViewState.configModel.soundEnabled
            ) { counterViewModel.handleIntent(CounterIntent.ToggleSoundState) }
            ActionButtonWithState(
                iconSrc = R.drawable.ic_vibration,
                contentDesc = "Vibration",
                state = counterViewState.configModel.vibrationEnabled
            ) { counterViewModel.handleIntent(CounterIntent.ToggleVibrationState) }
            ActionButton(iconSrc = R.drawable.ic_chart_bar, contentDesc = "History") { onNavigateToHistory() }
            ActionButton(iconSrc = R.drawable.ic_info, contentDesc = "Info") { onNavigateToInfo() }
        }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            val context = LocalContext.current
            val current = counterViewState.currentItem

            if (current != null) {
                TapCounterCard(
                    zekrText = current.zekr,
                    count = counterViewState.counter,
                    target = current.count,
                    totalCards = counterViewState.items.size,
                    currentIndex = counterViewState.currentIndex,
                ) {
                    // Save the dhikr being counted, then count + advance atomically in the ViewModel.
                    counterViewModel.handleIntent(CounterIntent.SaveZekrUpdate)
                    counterViewModel.handleIntent(CounterIntent.Count)

                    //Play zekr sound if enabled
                    if (counterViewState.configModel.soundEnabled) {
                        SoundUtils.playClickSound(context)
                    }

                    //Play zekr vibration if enabled
                    if (counterViewState.configModel.vibrationEnabled) {
                        VibrationUtils.vibrate(context, false)
                    }
                }
            }
        }
    }
}

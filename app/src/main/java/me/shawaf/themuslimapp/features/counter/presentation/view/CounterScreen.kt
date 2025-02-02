package me.shawaf.themuslimapp.features.counter.presentation.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.shawaf.themuslimapp.R
import me.shawaf.themuslimapp.features.counter.presentation.viewmodel.CounterIntent
import me.shawaf.themuslimapp.features.counter.presentation.viewmodel.CounterViewModel
import me.shawaf.themuslimapp.ui.components.AppScaffold
import me.shawaf.themuslimapp.ui.theme.Typography
import me.shawaf.themuslimapp.utils.SoundUtils
import me.shawaf.themuslimapp.utils.VibrationUtils

@SuppressLint("SuspiciousIndentation")
@Composable
fun CounterScreen(
    counterViewModel: CounterViewModel,
    onSwitchTheme: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToInfo: () -> Unit
) {
    var currentZekrCount by remember { mutableIntStateOf(0) }
    var nextCardIndex by remember { mutableStateOf(0) }
    val lastNextCardIndexValue = 0
    val counterViewState by counterViewModel.viewState.collectAsState()

    AppScaffold(LocalContext.current.getString(R.string.salat_khatm),
        configModel = counterViewState.configModel,
        onBackPressed = null,
        withBottomBar = true,
        onSwitchTheme = onSwitchTheme,
        onNavigateToInfo = onNavigateToInfo,
        onNavigateToHistory = onNavigateToHistory,
        onToggleSoundEnabledState = { counterViewModel.handleIntent(CounterIntent.ToggleSoundState) },
        onToggleVibrationEnabledState = { counterViewModel.handleIntent(CounterIntent.ToggleVibrationState) }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            val context = LocalContext.current
            val azkarList = counterViewState.zekrList
            if (azkarList.isNotEmpty()) HorizontalCardSwitcher(
                azkarList, nextCardIndex
            ) { zekrEntity ->
                counterViewModel.handleIntent(CounterIntent.SetCurrentZekrEntity(zekrEntity))
                currentZekrCount = zekrEntity.count
            }
            Text(
                text = "${counterViewState.counter}",
                style = Typography.displayLarge,
                fontSize = 80.sp,
                color = MaterialTheme.colorScheme.primary,
            )
            DragAndDropToggle {
                //Increase Counter
                counterViewModel.handleIntent(CounterIntent.IncreaseCounter)

                //Add new zekr to database
                counterViewModel.handleIntent(CounterIntent.SaveZekrUpdate)

                //Switch Cards if card/zekr count finish
                val counter = counterViewState.counter + 1
                nextCardIndex = if (counter >= currentZekrCount) {
                    // Reset Counter with delay, When Zekr Times Finished
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(300)
                        counterViewModel.handleIntent(CounterIntent.ResetCounter)
                    }
                    if (nextCardIndex >= azkarList.lastIndex) 0
                    else nextCardIndex + 1
                } else nextCardIndex

                //Play Zekr Sound if it was Enabled
                if (counterViewState.configModel.soundEnabled) {
                    SoundUtils.playClickSound(context)
                }

                //Play Zekr Vibration if it was Enabled
                if (counterViewState.configModel.vibrationEnabled) {
                    VibrationUtils.vibrate(context, false)
                }
            }
        }
    }
}
package me.shawaf.themuslimapp.features.main.presentation.screen

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
import me.shawaf.themuslimapp.R
import me.shawaf.themuslimapp.features.main.presentation.view.DragAndDropToggle
import me.shawaf.themuslimapp.features.main.presentation.view.HorizontalCardSwitcher
import me.shawaf.themuslimapp.features.main.presentation.viewmodel.MainViewModel
import me.shawaf.themuslimapp.ui.components.AppScaffold
import me.shawaf.themuslimapp.ui.theme.Typography
import me.shawaf.themuslimapp.utils.SoundUtils
import me.shawaf.themuslimapp.utils.VibrationUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@SuppressLint("SuspiciousIndentation")
@Composable
fun MainScreen(
    mainViewModel: MainViewModel,
    onSwitchTheme: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToInfo: () -> Unit
) {
    var counter by remember { mutableIntStateOf(0) }
    var currentZekrCounter by remember { mutableIntStateOf(0) }
    var switchToNextZekrCard by remember { mutableStateOf(false) }
    val configModel by mainViewModel.configModel.collectAsState()

    AppScaffold(
        LocalContext.current.getString(R.string.fajr_khatm),
        configModel = configModel,
        onBackPressed = null,
        withBottomBar = true,
        onSwitchTheme = onSwitchTheme,
        onNavigateToInfo = onNavigateToInfo,
        onNavigateToHistory = onNavigateToHistory,
        onToggleSoundEnabledState = {mainViewModel.toggleSoundState()},
        onToggleVibrationEnabledState = {mainViewModel.toggleVibrationState()}
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            val context = LocalContext.current
            val azkarList = mainViewModel.loadAzkarFromJson(context)
            HorizontalCardSwitcher(azkarList, switchToNextZekrCard) { zekrEntity ->
                Timber.e("Current Zekr Card : ${zekrEntity.zekr}")
                currentZekrCounter = zekrEntity.count
                switchToNextZekrCard = false // Reset after switching
            }
            Text(
                text = "$counter",
                style = Typography.displayLarge,
                fontSize = 80.sp,
                color = MaterialTheme.colorScheme.primary,
            )
            DragAndDropToggle {
                counter++
                switchToNextZekrCard = if (counter >= currentZekrCounter) {
                    // Reset Counter with delay, When Zekr Times Finished
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(300)
                        counter = 0
                    }
                    true
                } else false

                    if (configModel.soundEnabled) {
                    SoundUtils.playClickSound(context)
                }
                if (configModel.vibrationEnabled) {
                    VibrationUtils.vibrate(context, switchToNextZekrCard)
                }
            }
        }
    }
}
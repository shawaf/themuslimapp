package me.shawaf.themuslimapp.features.main.presentation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import me.shawaf.themuslimapp.features.main.presentation.viewmodel.MainViewModel

@SuppressLint("SuspiciousIndentation")
@Composable
fun MainScreen(
    mainViewModel: MainViewModel, onNavigateToHistory: () -> Unit, onNavigateToInfo: () -> Unit
) {

//    AppScaffold(
//        LocalContext.current.getString(R.string.salat_khatm),
//        configModel = ConfigModel(),
//        onBackPressed = null,
//        withBottomBar = true,
//        onSwitchTheme = onSwitchTheme,
//        onNavigateToInfo = onNavigateToInfo,
//        onNavigateToHistory = onNavigateToHistory,
//        onToggleSoundEnabledState = {mainViewModel.toggleSoundState()},
//        onToggleVibrationEnabledState = {mainViewModel.toggleVibrationState()}
//    ) {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.SpaceBetween,
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//        ) {
//            val context = LocalContext.current
//            val azkarList = mainViewModel.loadAzkarFromJson()
//            HorizontalCardSwitcher(azkarList, switchToNextZekrCard) { zekrEntity ->
//                Timber.e("Current Zekr Card : ${zekrEntity.zekr}")
//                currentZekrCounter = zekrEntity.count
//                switchToNextZekrCard = false // Reset after switching
//            }
//            Text(
//                text = "$counter",
//                style = Typography.displayLarge,
//                fontSize = 80.sp,
//                color = MaterialTheme.colorScheme.primary,
//            )
//            DragAndDropToggle {
//                counter++
//                switchToNextZekrCard = if (counter >= currentZekrCounter) {
//                    // Reset Counter with delay, When Zekr Times Finished
//                    CoroutineScope(Dispatchers.Main).launch {
//                        delay(300)
//                        counter = 0
//                    }
//                    true
//                } else false
//
//                    if (configModel.soundEnabled) {
//                    SoundUtils.playClickSound(context)
//                }
//                if (configModel.vibrationEnabled) {
//                    VibrationUtils.vibrate(context, switchToNextZekrCard)
//                }
//            }
//        }
//        //@CustomComment:description=HorizontalCardSwitcher(azkarList, switchToNextZekrCard) { zekrEntity -> imber.e("Current Zekr Card : ${zekrEntity.zekr}") currentZekrCounter = zekrEntity.count switchToNextZekrCard = false // Reset after switching };image=/Users/mohamedelshawaf/Downloads/diagram-export-1-22-2025-6_21_00-PM.png
//}
}
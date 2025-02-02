package me.shawaf.themuslimapp.features

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import me.shawaf.themuslimapp.ui.viewmodel.ThemeViewModel
import me.shawaf.themuslimapp.ui.components.AppGradientBackground
import me.shawaf.themuslimapp.ui.theme.TheMuslimAppTheme
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val currentTheme = themeViewModel.currentTheme.value
            TheMuslimAppTheme(colorScheme = currentTheme) {
                AppGradientBackground {
                    TheMuslimApp(themeViewModel)
                }
            }
        }
    }
}
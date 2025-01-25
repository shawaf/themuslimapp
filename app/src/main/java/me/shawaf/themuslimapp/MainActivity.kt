package me.shawaf.themuslimapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import me.shawaf.themuslimapp.features.root.TheMuslimApp
import me.shawaf.themuslimapp.features.root.ThemeViewModel
import me.shawaf.themuslimapp.ui.components.AppGradientBackground
import me.shawaf.themuslimapp.ui.theme.TheMuslimAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val currentTheme = themeViewModel.currentTheme.value
            TheMuslimAppTheme(colorScheme = currentTheme) {
                AppGradientBackground {
                    TheMuslimApp(themeViewModel)
                }
            }
        }
    }
}
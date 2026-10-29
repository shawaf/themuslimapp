package me.shawaf.themuslimapp.features

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import me.shawaf.themuslimapp.ui.components.NoorBackground
import me.shawaf.themuslimapp.ui.theme.TheMuslimAppTheme
import me.shawaf.themuslimapp.ui.viewmodel.ThemeViewModel
import me.shawaf.themuslimapp.utils.LocaleHelper

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context) {
        val language = LocaleHelper.savedLanguage(newBase)
        super.attachBaseContext(LocaleHelper.wrap(newBase, language))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val themeMode by themeViewModel.themeMode.collectAsState()
            val darkTheme = when (themeMode) {
                "dark" -> true
                "light" -> false
                else -> isSystemInDarkTheme()
            }
            TheMuslimAppTheme(darkTheme = darkTheme) {
                NoorBackground {
                    TheMuslimApp(themeViewModel = themeViewModel)
                }
            }
        }
    }
}

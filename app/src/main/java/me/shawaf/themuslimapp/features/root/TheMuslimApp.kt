package me.shawaf.themuslimapp.features.root

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.shawaf.themuslimapp.features.main.presentation.screen.MainScreen
import me.shawaf.themuslimapp.features.main.presentation.viewmodel.MainViewModel
import me.shawaf.themuslimapp.features.history.presentation.HistoryScreen
import me.shawaf.themuslimapp.features.info.presentation.screen.InfoScreen

@Composable
fun TheMuslimApp(themeViewModel: ThemeViewModel) {
    val navController: NavHostController = rememberNavController()
    val mainViewModel: MainViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = "counter") {
        composable("counter") {
            MainScreen(
                mainViewModel,
                onSwitchTheme = { themeViewModel.switchToNextTheme() }, // Trigger theme change
                onNavigateToHistory = { navController.navigate("history") },
                onNavigateToInfo = { navController.navigate("info") },
            )
        }
        composable("history") {
            HistoryScreen(onSwitchTheme = { themeViewModel.switchToNextTheme() }, // Trigger theme change
                onNavigateBack = { navController.popBackStack() })
        }

        composable("info") {
            InfoScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}
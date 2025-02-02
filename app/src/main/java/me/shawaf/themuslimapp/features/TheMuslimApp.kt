package me.shawaf.themuslimapp.features

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.shawaf.themuslimapp.features.counter.presentation.view.CounterScreen
import me.shawaf.themuslimapp.features.counter.presentation.viewmodel.CounterViewModel
import me.shawaf.themuslimapp.features.main.presentation.MainScreen
import me.shawaf.themuslimapp.features.main.presentation.viewmodel.MainViewModel
import me.shawaf.themuslimapp.features.history.presentation.HistoryScreen
import me.shawaf.themuslimapp.features.history.presentation.HistoryViewModel
import me.shawaf.themuslimapp.features.info.presentation.InfoScreen
import me.shawaf.themuslimapp.features.info.presentation.InfoViewModel
import me.shawaf.themuslimapp.ui.viewmodel.ThemeViewModel

@Composable
fun TheMuslimApp(themeViewModel: ThemeViewModel) {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "counter") {
        composable("counter") {
            val counterViewModel: CounterViewModel = hiltViewModel()
            CounterScreen(
                counterViewModel = counterViewModel,
                onSwitchTheme = { themeViewModel.switchToNextTheme() }, // Trigger theme change
                onNavigateToHistory = { navController.navigate("history") },
                onNavigateToInfo = { navController.navigate("info") },
            )
        }
        composable("history") {
            val historyViewModel: HistoryViewModel = hiltViewModel()
            HistoryScreen(historyViewModel,onNavigateBack = { navController.popBackStack() })
        }

        composable("info") {
            val infoViewModel: InfoViewModel = hiltViewModel()
            InfoScreen(viewModel = infoViewModel, onNavigateBack = { navController.popBackStack() })
        }
    }
}
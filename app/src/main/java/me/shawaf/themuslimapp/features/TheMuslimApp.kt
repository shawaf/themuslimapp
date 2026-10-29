package me.shawaf.themuslimapp.features

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import me.shawaf.themuslimapp.features.counter.presentation.view.CounterScreen
import me.shawaf.themuslimapp.features.counter.presentation.viewmodel.CounterViewModel
import me.shawaf.themuslimapp.features.duas.presentation.DuasScreen
import me.shawaf.themuslimapp.features.duas.presentation.viewmodel.DuasViewModel
import me.shawaf.themuslimapp.features.prayer.presentation.PrayerScreen
import me.shawaf.themuslimapp.features.prayer.presentation.viewmodel.PrayerViewModel
import me.shawaf.themuslimapp.features.qibla.presentation.QiblaScreen
import me.shawaf.themuslimapp.features.qibla.presentation.viewmodel.QiblaViewModel
import me.shawaf.themuslimapp.features.hadith.presentation.HadithScreen
import me.shawaf.themuslimapp.features.hadith.presentation.viewmodel.HadithViewModel
import me.shawaf.themuslimapp.features.calendar.presentation.CalendarScreen
import me.shawaf.themuslimapp.features.calendar.presentation.viewmodel.CalendarViewModel
import me.shawaf.themuslimapp.features.history.presentation.HistoryScreen
import me.shawaf.themuslimapp.features.history.presentation.HistoryViewModel
import me.shawaf.themuslimapp.features.home.presentation.HomeScreen
import me.shawaf.themuslimapp.features.home.presentation.viewmodel.HomeViewModel
import me.shawaf.themuslimapp.features.info.presentation.InfoScreen
import me.shawaf.themuslimapp.features.info.presentation.InfoViewModel
import me.shawaf.themuslimapp.ui.components.ComingSoonScreen
import me.shawaf.themuslimapp.ui.components.NoorBottomBar
import me.shawaf.themuslimapp.ui.navigation.NoorRoutes
import me.shawaf.themuslimapp.ui.navigation.bottomBarRoutes

@Composable
fun TheMuslimApp() {
    val navController: NavHostController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            if (currentRoute in bottomBarRoutes) {
                NoorBottomBar(
                    currentRoute = currentRoute,
                    onTabSelected = { route -> navController.navigateToTab(route) },
                )
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NoorRoutes.HOME,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(NoorRoutes.HOME) {
                val homeViewModel: HomeViewModel = hiltViewModel()
                HomeScreen(
                    homeViewModel = homeViewModel,
                    onNavigate = { route -> navController.navigateToTab(route) },
                )
            }

            composable(NoorRoutes.PRAYER) {
                val prayerViewModel: PrayerViewModel = hiltViewModel()
                PrayerScreen(prayerViewModel = prayerViewModel)
            }
            composable(NoorRoutes.QIBLA) {
                val qiblaViewModel: QiblaViewModel = hiltViewModel()
                QiblaScreen(qiblaViewModel = qiblaViewModel)
            }
            composable(NoorRoutes.DUAS) {
                val duasViewModel: DuasViewModel = hiltViewModel()
                DuasScreen(duasViewModel = duasViewModel)
            }

            composable(NoorRoutes.HADITH) {
                val hadithViewModel: HadithViewModel = hiltViewModel()
                HadithScreen(hadithViewModel = hadithViewModel, onBack = { navController.popBackStack() })
            }
            composable(NoorRoutes.CALENDAR) {
                val calendarViewModel: CalendarViewModel = hiltViewModel()
                CalendarScreen(calendarViewModel = calendarViewModel, onBack = { navController.popBackStack() })
            }

            composable(NoorRoutes.SEBHA) {
                val counterViewModel: CounterViewModel = hiltViewModel()
                CounterScreen(
                    counterViewModel = counterViewModel,
                    onNavigateToHistory = { navController.navigate(NoorRoutes.HISTORY) },
                    onNavigateToInfo = { navController.navigate(NoorRoutes.INFO) },
                )
            }

            composable(NoorRoutes.HISTORY) {
                val historyViewModel: HistoryViewModel = hiltViewModel()
                HistoryScreen(historyViewModel, onNavigateBack = { navController.popBackStack() })
            }

            composable(NoorRoutes.INFO) {
                val infoViewModel: InfoViewModel = hiltViewModel()
                InfoScreen(viewModel = infoViewModel, onNavigateBack = { navController.popBackStack() })
            }
        }
    }
}

/** Navigate to a primary tab with standard single-top / state-saving behaviour. */
private fun NavHostController.navigateToTab(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

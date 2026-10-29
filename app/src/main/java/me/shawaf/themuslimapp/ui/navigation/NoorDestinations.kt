package me.shawaf.themuslimapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Route constants for every screen in the app. The counter (Sebha) screen keeps
 * its original "counter" route so existing navigation keeps working.
 */
object NoorRoutes {
    const val HOME = "home"
    const val PRAYER = "prayer"
    const val QIBLA = "qibla"
    const val SEBHA = "counter"
    const val DUAS = "duas"
    const val HADITH = "hadith"
    const val CALENDAR = "calendar"
    const val HISTORY = "history"
    const val INFO = "info"
    const val SETTINGS = "settings"
}

/** A tab shown in the bottom navigation bar. */
data class NoorTab(
    val route: String,
    val label: String,
    val icon: ImageVector,
)

/** The five primary tabs in the bottom navigation bar. */
val bottomNavTabs = listOf(
    NoorTab(NoorRoutes.HOME, "Home", Icons.Default.Home),
    NoorTab(NoorRoutes.PRAYER, "Prayer", Icons.Default.Notifications),
    NoorTab(NoorRoutes.QIBLA, "Qibla", Icons.Default.Place),
    NoorTab(NoorRoutes.SEBHA, "Sebha", Icons.Default.Refresh),
    NoorTab(NoorRoutes.DUAS, "Duas", Icons.Default.FavoriteBorder),
)

/** Routes that should display the bottom navigation bar. */
val bottomBarRoutes = bottomNavTabs.map { it.route }.toSet()

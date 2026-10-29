package me.shawaf.themuslimapp.ui.navigation

import me.shawaf.themuslimapp.R

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
    val iconRes: Int,
)

/** The five primary tabs in the bottom navigation bar. */
val bottomNavTabs = listOf(
    NoorTab(NoorRoutes.HOME, "Home", R.drawable.ic_feat_home),
    NoorTab(NoorRoutes.PRAYER, "Prayer", R.drawable.ic_feat_prayer),
    NoorTab(NoorRoutes.QIBLA, "Qibla", R.drawable.ic_feat_qibla),
    NoorTab(NoorRoutes.SEBHA, "Sebha", R.drawable.ic_feat_sebha),
    NoorTab(NoorRoutes.DUAS, "Duas", R.drawable.ic_feat_duas),
)

/** Routes that should display the bottom navigation bar. */
val bottomBarRoutes = bottomNavTabs.map { it.route }.toSet()

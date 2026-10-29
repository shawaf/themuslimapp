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

    /** Counter route — either a whole category or a single selected dhikr. */
    const val COUNTER_PATTERN = "counter?category={category}&ar={ar}&en={en}&count={count}"

    /** Count through every dhikr in a category. */
    fun counterCategory(categoryId: String) = "counter?category=${android.net.Uri.encode(categoryId)}"

    /** Count a single specific dhikr (e.g. from search). */
    fun counter(ar: String, en: String, count: Int): String {
        val arEnc = android.net.Uri.encode(ar)
        val enEnc = android.net.Uri.encode(en)
        return "counter?ar=$arEnc&en=$enEnc&count=$count"
    }
}

/** A tab shown in the bottom navigation bar. */
data class NoorTab(
    val route: String,
    val labelRes: Int,
    val iconRes: Int,
)

/** The five primary tabs in the bottom navigation bar. */
val bottomNavTabs = listOf(
    NoorTab(NoorRoutes.HOME, R.string.nav_home, R.drawable.ic_feat_home),
    NoorTab(NoorRoutes.PRAYER, R.string.nav_prayer, R.drawable.ic_feat_prayer),
    NoorTab(NoorRoutes.QIBLA, R.string.nav_qibla, R.drawable.ic_feat_qibla),
    NoorTab(NoorRoutes.DUAS, R.string.nav_azkar, R.drawable.ic_feat_sebha),
    NoorTab(NoorRoutes.SETTINGS, R.string.nav_settings, R.drawable.ic_feat_settings),
)

/** Routes that should display the bottom navigation bar. */
val bottomBarRoutes = bottomNavTabs.map { it.route }.toSet()

package me.shawaf.themuslimapp.data.local.prefers

/**
 * User preferences for prayer-time calculation and azan notifications.
 *
 * @param calcMethod adhan2 CalculationMethod enum name.
 * @param madhab adhan2 Madhab enum name (SHAFI or HANAFI — affects Asr).
 * @param azanEnabled per-prayer notification toggle, keyed by prayer key
 *        (FAJR, DHUHR, ASR, MAGHRIB, ISHA).
 */
data class PrayerSettings(
    val calcMethod: String = "MUSLIM_WORLD_LEAGUE",
    val madhab: String = "SHAFI",
    val azanEnabled: Map<String, Boolean> = mapOf(
        "FAJR" to true,
        "DHUHR" to true,
        "ASR" to true,
        "MAGHRIB" to true,
        "ISHA" to true,
    ),
) {
    fun isAzanOn(key: String): Boolean = azanEnabled[key] ?: true
}

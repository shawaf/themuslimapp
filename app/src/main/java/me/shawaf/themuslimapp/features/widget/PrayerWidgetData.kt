package me.shawaf.themuslimapp.features.widget

import android.content.Context
import com.google.gson.Gson
import me.shawaf.themuslimapp.R
import me.shawaf.themuslimapp.data.local.prefers.KEY_PRAYER_SETTINGS
import me.shawaf.themuslimapp.data.local.prefers.PrayerSettings
import me.shawaf.themuslimapp.features.prayer.data.DEFAULT_LOCATION
import me.shawaf.themuslimapp.features.prayer.data.LocationProvider
import me.shawaf.themuslimapp.features.prayer.domain.PrayerTimesCalculator
import me.shawaf.themuslimapp.utils.LocaleHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** One prayer row as rendered by the widget. */
data class WidgetPrayer(
    val name: String,
    val time: String,
    val isNext: Boolean,
)

/** Everything the Glance widget needs to render a single update. */
data class PrayerWidgetState(
    val isArabic: Boolean,
    val locationLabel: String,
    val nextPrayerName: String,
    val countdown: String,
    val prayers: List<WidgetPrayer>,
)

/**
 * Builds the widget state by reusing the app's prayer data layer directly
 * (the adhan-based [PrayerTimesCalculator] + the existing [LocationProvider]
 * and the saved [PrayerSettings]), without running the full DI/azan chain.
 *
 * Mirrors how [me.shawaf.themuslimapp.features.home.presentation.viewmodel.HomeViewModel]
 * picks the next prayer, formats times and localizes names/countdown.
 */
object PrayerWidgetData {

    suspend fun load(context: Context): PrayerWidgetState {
        // Respect the saved app language (Arabic default, RTL) just like the app.
        val language = LocaleHelper.savedLanguage(context)
        val localized = LocaleHelper.wrap(context, language)
        val isArabic = language == LocaleHelper.ARABIC

        val settings = readPrayerSettings(localized)
        val resolved = LocationProvider(localized).lastKnownLocation()
        val location = resolved ?: DEFAULT_LOCATION

        val slots = PrayerTimesCalculator().computeForDay(
            latitude = location.latitude,
            longitude = location.longitude,
            calcMethod = settings.calcMethod,
            madhab = settings.madhab,
        )

        val now = System.currentTimeMillis()
        val timeFormat = SimpleDateFormat("h:mm", Locale.getDefault())

        // Only the 5 daily prayers (skip sunrise), same as the home dashboard.
        val daily = slots.filter { it.canAzan }
        val upcoming = daily.firstOrNull { it.epochMillis > now }

        // The prayer to highlight: next one today, or Fajr once the day has
        // rolled past Isha (the highlighted row shows today's Fajr time).
        val next = upcoming ?: daily.firstOrNull()

        // The instant the countdown targets. After Isha, that is TOMORROW's Fajr
        // so the countdown stays correct across the day rollover.
        val nextEpoch = upcoming?.epochMillis ?: run {
            val tomorrow = PrayerTimesCalculator().computeForDay(
                latitude = location.latitude,
                longitude = location.longitude,
                calcMethod = settings.calcMethod,
                madhab = settings.madhab,
                dayMillis = now + 24L * 60 * 60 * 1000,
            )
            tomorrow.firstOrNull { it.key == "FAJR" }?.epochMillis
        }

        val prayers = daily.map { slot ->
            WidgetPrayer(
                name = if (isArabic) slot.nameAr else slot.nameEn,
                time = localizeDigits(timeFormat.format(Date(slot.epochMillis)), isArabic),
                isNext = slot.key == next?.key,
            )
        }

        val countdown = nextEpoch?.let {
            val diff = it - now
            formatCountdown(if (diff > 0) diff else 0L, isArabic)
        }.orEmpty()

        return PrayerWidgetState(
            isArabic = isArabic,
            locationLabel = location.label.ifEmpty { if (isArabic) "موقعي" else "My location" },
            nextPrayerName = next?.let { if (isArabic) it.nameAr else it.nameEn }.orEmpty(),
            countdown = countdown,
            prayers = prayers,
        )
    }

    private fun readPrayerSettings(context: Context): PrayerSettings {
        val prefs = context.getSharedPreferences(
            context.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE,
        )
        val saved = prefs.getString(KEY_PRAYER_SETTINGS, null)
        return if (!saved.isNullOrEmpty()) {
            runCatching { Gson().fromJson(saved, PrayerSettings::class.java) }
                .getOrDefault(PrayerSettings())
        } else {
            PrayerSettings()
        }
    }

    /** "in 2h 10m" / "بعد ٢س ١٠د" — mirrors HomeViewModel, with Arabic-Indic digits. */
    private fun formatCountdown(diffMillis: Long, isArabic: Boolean): String {
        val totalMinutes = diffMillis / 60000
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return if (isArabic) {
            val h = localizeDigits(hours.toString(), true)
            val m = localizeDigits(minutes.toString(), true)
            if (hours > 0) "بعد ${h}س ${m}د" else "بعد ${m}د"
        } else {
            if (hours > 0) "in ${hours}h ${minutes}m" else "in ${minutes}m"
        }
    }

    /** Converts Western digits to Arabic-Indic (٠١٢…) when rendering in Arabic. */
    private fun localizeDigits(value: String, isArabic: Boolean): String {
        if (!isArabic) return value
        val arabicDigits = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
        return buildString {
            for (c in value) append(if (c in '0'..'9') arabicDigits[c - '0'] else c)
        }
    }
}

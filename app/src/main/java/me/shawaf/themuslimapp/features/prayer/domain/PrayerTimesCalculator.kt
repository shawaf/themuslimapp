package me.shawaf.themuslimapp.features.prayer.domain

import com.batoulapps.adhan.CalculationMethod
import com.batoulapps.adhan.Coordinates
import com.batoulapps.adhan.Madhab
import com.batoulapps.adhan.PrayerTimes
import com.batoulapps.adhan.data.DateComponents
import me.shawaf.themuslimapp.data.local.model.PrayerSlot
import java.util.Calendar
import javax.inject.Inject

/** Computes the day's prayer times for a location using the adhan (Java) library. */
class PrayerTimesCalculator @Inject constructor() {

    fun computeForDay(
        latitude: Double,
        longitude: Double,
        calcMethod: String,
        madhab: String,
        dayMillis: Long = System.currentTimeMillis(),
    ): List<PrayerSlot> {
        val coordinates = Coordinates(latitude, longitude)

        val cal = Calendar.getInstance().apply { timeInMillis = dayMillis }
        val date = DateComponents(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.DAY_OF_MONTH),
        )

        val method = runCatching { CalculationMethod.valueOf(calcMethod) }
            .getOrDefault(CalculationMethod.MUSLIM_WORLD_LEAGUE)
        val selectedMadhab = runCatching { Madhab.valueOf(madhab) }
            .getOrDefault(Madhab.SHAFI)

        val params = method.parameters.apply { this.madhab = selectedMadhab }
        val times = PrayerTimes(coordinates, date, params)

        return listOf(
            PrayerSlot("FAJR", "Fajr", "الفجر", times.fajr.time),
            PrayerSlot("SUNRISE", "Sunrise", "الشروق", times.sunrise.time, canAzan = false),
            PrayerSlot("DHUHR", "Dhuhr", "الظهر", times.dhuhr.time),
            PrayerSlot("ASR", "Asr", "العصر", times.asr.time),
            PrayerSlot("MAGHRIB", "Maghrib", "المغرب", times.maghrib.time),
            PrayerSlot("ISHA", "Isha", "العشاء", times.isha.time),
        )
    }
}

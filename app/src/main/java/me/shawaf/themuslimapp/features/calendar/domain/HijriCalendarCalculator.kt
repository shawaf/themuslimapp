package me.shawaf.themuslimapp.features.calendar.domain

import me.shawaf.themuslimapp.features.calendar.domain.model.CalendarDay
import me.shawaf.themuslimapp.features.calendar.domain.model.CalendarMonth
import me.shawaf.themuslimapp.features.calendar.domain.model.IslamicEvent
import java.time.LocalDate
import java.time.chrono.HijrahDate
import java.time.format.TextStyle
import java.time.temporal.ChronoField
import java.util.Locale
import javax.inject.Inject

private data class EventDef(val name: String, val m: Int, val d: Int, val desc: String)

/** Produces the current Hijri month grid and upcoming Islamic events via java.time. */
class HijriCalendarCalculator @Inject constructor() {

    private val monthsEn = listOf(
        "Muharram", "Safar", "Rabi al-Awwal", "Rabi al-Thani", "Jumada al-Awwal",
        "Jumada al-Thani", "Rajab", "Sha'ban", "Ramadan", "Shawwal", "Dhul-Qa'dah", "Dhul-Hijjah",
    )
    private val monthsAr = listOf(
        "مُحَرَّم", "صَفَر", "رَبِيع الأول", "رَبِيع الآخر", "جُمَادَى الأولى",
        "جُمَادَى الآخرة", "رَجَب", "شَعْبَان", "رَمَضَان", "شَوَّال", "ذُو القَعْدَة", "ذُو الحِجَّة",
    )

    fun currentMonth(): CalendarMonth {
        val today = HijrahDate.now()
        val month = today.get(ChronoField.MONTH_OF_YEAR)
        val year = today.get(ChronoField.YEAR)
        val todayDay = today.get(ChronoField.DAY_OF_MONTH)
        val first = today.with(ChronoField.DAY_OF_MONTH, 1L)
        val leadingBlanks = first.get(ChronoField.DAY_OF_WEEK) % 7 // Sunday-first columns
        val length = today.lengthOfMonth()

        val days = buildList {
            repeat(leadingBlanks) { add(CalendarDay(null, false)) }
            for (d in 1..length) add(CalendarDay(d, d == todayDay))
        }

        val greg = LocalDate.ofEpochDay(today.toEpochDay())
        val gregLabel = "${greg.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)} ${greg.year}"

        return CalendarMonth(monthsEn[month - 1], monthsAr[month - 1], year, gregLabel, days)
    }

    /** Today's Hijri date as Arabic text, e.g. "١٤ شوال ١٤٤٧". */
    fun hijriTodayArabic(): String {
        val today = HijrahDate.now()
        val d = today.get(ChronoField.DAY_OF_MONTH)
        val m = today.get(ChronoField.MONTH_OF_YEAR)
        val y = today.get(ChronoField.YEAR)
        return "${toArabicDigits(d)} ${monthsAr[m - 1]} ${toArabicDigits(y)}"
    }

    /** Today's Hijri date in English, e.g. "14 Shawwal". */
    fun hijriTodayEnglish(): String {
        val today = HijrahDate.now()
        val d = today.get(ChronoField.DAY_OF_MONTH)
        val m = today.get(ChronoField.MONTH_OF_YEAR)
        return "$d ${monthsEn[m - 1]}"
    }

    private fun toArabicDigits(n: Int): String {
        val ar = "٠١٢٣٤٥٦٧٨٩"
        return n.toString().map { if (it.isDigit()) ar[it - '0'] else it }.joinToString("")
    }

    /** All notable events of the CURRENT Hijri year (past and upcoming), in date order. */
    fun currentYearEvents(): List<IslamicEvent> {
        val today = HijrahDate.now()
        val hYear = today.get(ChronoField.YEAR)
        val todayEpoch = today.toEpochDay()

        // name, hijri month (1-based), hijri day, description — a full year of notable days
        val defs = listOf(
            EventDef("Islamic New Year", 1, 1, "Start of Muharram"),
            EventDef("Day of Ashura", 1, 10, "Recommended fasting"),
            EventDef("Mawlid an-Nabi", 3, 12, "Birth of the Prophet ﷺ"),
            EventDef("Isra & Mi'raj", 7, 27, "The Night Journey"),
            EventDef("Mid-Sha'ban", 8, 15, "Laylat al-Bara'ah"),
            EventDef("First of Ramadan", 9, 1, "Start of fasting"),
            EventDef("Laylat al-Qadr", 9, 27, "The Night of Decree"),
            EventDef("Eid al-Fitr", 10, 1, "Festival of breaking the fast"),
            EventDef("First of Dhul-Hijjah", 12, 1, "The ten blessed days"),
            EventDef("Day of Arafah", 12, 9, "Recommended fasting"),
            EventDef("Eid al-Adha", 12, 10, "Festival of Sacrifice"),
            EventDef("Days of Tashreeq", 12, 11, "11th–13th Dhul-Hijjah"),
        )

        val events = defs.mapNotNull { def ->
            val hd = runCatching { HijrahDate.of(hYear, def.m, def.d) }.getOrNull() ?: return@mapNotNull null
            val epoch = hd.toEpochDay()
            IslamicEvent(
                nameEn = def.name,
                description = def.desc,
                hijriDay = def.d,
                hijriMonthEn = monthsEn[def.m - 1],
                daysAway = (epoch - todayEpoch).toInt(),
            )
        }.sortedBy { it.daysAway }

        val nextIndex = events.indexOfFirst { it.daysAway >= 0 }
        return events.mapIndexed { i, e -> e.copy(isNext = i == nextIndex) }
    }
}

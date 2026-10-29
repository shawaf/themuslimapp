package me.shawaf.themuslimapp.features.calendar.domain.model

/** A single cell in the month grid; [day] is null for leading blanks. */
data class CalendarDay(val day: Int?, val isToday: Boolean)

data class CalendarMonth(
    val hijriMonthEn: String,
    val hijriMonthAr: String,
    val hijriYear: Int,
    val gregorianLabel: String,
    val days: List<CalendarDay>,
)

data class IslamicEvent(
    val nameEn: String,
    val gregDay: Int,
    val gregMonthShort: String,
    val hijriLabel: String,
    val daysAway: Int,
)

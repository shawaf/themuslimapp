package me.shawaf.themuslimapp.features.history.presentation.model

data class HistoryDayUIModel(
    val day: String, val zekrCountList: List<ZekrCount>
)

data class ZekrCount(
    val zekr: String, val count: Int
)

enum class HistoryFilter {
    BY_DAY, BY_ZEKR
}
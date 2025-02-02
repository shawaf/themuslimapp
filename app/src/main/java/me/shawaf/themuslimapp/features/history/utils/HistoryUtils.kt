package me.shawaf.themuslimapp.features.history.utils

import me.shawaf.themuslimapp.data.local.dp.entity.ZekrEntity
import me.shawaf.themuslimapp.features.history.presentation.model.HistoryDayUIModel
import me.shawaf.themuslimapp.features.history.presentation.model.HistoryZekrUIModel
import me.shawaf.themuslimapp.features.history.presentation.model.ZekrCount
import me.shawaf.themuslimapp.utils.DateUtils

object HistoryUtils {

    fun extractZekrByDay(zekrEntities: List<ZekrEntity>): List<HistoryDayUIModel> {
        // Group ZekrEntity by the formatted day (based on createdAt)
        val groupedByDay = zekrEntities.groupBy {
            DateUtils.getFormattedDateInArabic(it.createdAt)  // Format the createdAt as the day
        }

        // Map the grouped data into HistoryDayUIModel
        return groupedByDay.map { (day, zekrs) ->
            // Group the ZekrEntity items by their zekr value
            val zekrCountList = zekrs.groupBy { it.zekr }.map { (zekr, zekrList) ->
                    ZekrCount(zekr, zekrList.size)  // Sum the counts of the same zekr
                }

            // Create the UI model for each day
            HistoryDayUIModel(day, zekrCountList)
        }
    }

    fun extractZekrByType(zekrEntities: List<ZekrEntity>): List<HistoryZekrUIModel> {
        // Group ZekrEntity by the formatted day (based on createdAt)
        val groupedByDay = zekrEntities.groupBy {
            it.zekr  // Format the createdAt as the day
        }

        // Map the grouped data into HistoryDayUIModel
        return groupedByDay.map { (zekr, list) ->
            // Create the UI model for each day
            HistoryZekrUIModel(zekr, list.size)
        }
    }
}
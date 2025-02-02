package me.shawaf.themuslimapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {

    fun getFormattedDateInArabic(timeInMillSecs: Long): String {
        // Create a SimpleDateFormat object with Arabic locale
        val arabicLocale = Locale("ar", "SA")  // Arabic (Saudi Arabia)
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", arabicLocale)

        // Format the current time to a date
        return dateFormat.format(Date(timeInMillSecs))
    }

}
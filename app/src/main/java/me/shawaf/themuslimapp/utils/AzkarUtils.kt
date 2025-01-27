package me.shawaf.themuslimapp.utils

import android.content.Context
import com.google.gson.Gson
import me.shawaf.themuslimapp.R
import me.shawaf.themuslimapp.data.local.dp.entity.AzkarWrapper
import me.shawaf.themuslimapp.data.local.dp.entity.ZekrEntity

object AzkarUtils {

    private fun parseJsonFromRaw(context: Context, rawResId: Int): AzkarWrapper {
        // Read JSON file from raw folder
        val inputStream = context.resources.openRawResource(rawResId)
        val jsonContent = inputStream.bufferedReader().use { it.readText() }

        // Parse the JSON
        return Gson().fromJson(jsonContent, AzkarWrapper::class.java)
    }

    fun loadAzkar(context: Context): List<ZekrEntity> {
        val azkarWrapper = parseJsonFromRaw(context, R.raw.zekr)
        // Print each Zekr object
        return azkarWrapper.azkar
    }

}
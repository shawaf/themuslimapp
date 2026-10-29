package me.shawaf.themuslimapp.utils

import android.content.Context
import com.google.gson.Gson
import me.shawaf.themuslimapp.R
import me.shawaf.themuslimapp.data.local.model.HadithCollection
import me.shawaf.themuslimapp.data.local.model.HadithWrapper

/** Loads the bundled hadith dataset from res/raw, mirroring [DuaUtils]. */
class HadithUtils(
    private val context: Context,
) {
    private fun parseJsonFromRaw(rawResId: Int): HadithWrapper {
        val inputStream = context.resources.openRawResource(rawResId)
        val jsonContent = inputStream.bufferedReader().use { it.readText() }
        return Gson().fromJson(jsonContent, HadithWrapper::class.java)
    }

    fun loadCollections(): List<HadithCollection> =
        parseJsonFromRaw(R.raw.hadith).collections
}

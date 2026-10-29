package me.shawaf.themuslimapp.utils

import android.content.Context
import com.google.gson.Gson
import me.shawaf.themuslimapp.R
import me.shawaf.themuslimapp.data.local.model.DuaCategory
import me.shawaf.themuslimapp.data.local.model.DuasWrapper

/** Loads the bundled duas dataset from res/raw, mirroring [AzkarUtils]. */
class DuaUtils(
    private val context: Context,
) {

    private fun parseJsonFromRaw(rawResId: Int): DuasWrapper {
        val inputStream = context.resources.openRawResource(rawResId)
        val jsonContent = inputStream.bufferedReader().use { it.readText() }
        return Gson().fromJson(jsonContent, DuasWrapper::class.java)
    }

    fun loadDuaCategories(): List<DuaCategory> {
        return parseJsonFromRaw(R.raw.duas).categories
    }
}

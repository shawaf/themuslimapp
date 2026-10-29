package me.shawaf.themuslimapp.data.local.model

import com.google.gson.annotations.SerializedName

/** A single supplication / dhikr entry loaded from res/raw/duas.json. */
data class DuaItem(
    val ar: String,
    val en: String,
    val translit: String = "",
    val count: Int = 1,
    val reference: String = "",
)

/** A named group of duas (e.g. Morning, Evening). */
data class DuaCategory(
    val id: String,
    @SerializedName("title_ar") val titleAr: String,
    @SerializedName("title_en") val titleEn: String,
    val duas: List<DuaItem> = emptyList(),
)

/** Root wrapper matching the duas.json structure. */
data class DuasWrapper(
    val categories: List<DuaCategory> = emptyList(),
)

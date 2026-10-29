package me.shawaf.themuslimapp.data.local.model

import com.google.gson.annotations.SerializedName

/** A single hadith entry loaded from res/raw/hadith.json. */
data class HadithItem(
    val ar: String,
    val en: String,
    val narrator: String = "",
    val reference: String = "",
    val grade: String = "",
)

/** A hadith collection (e.g. Sahih al-Bukhari). */
data class HadithCollection(
    val id: String,
    @SerializedName("name_ar") val nameAr: String,
    @SerializedName("name_en") val nameEn: String,
    val total: Int = 0,
    val hadiths: List<HadithItem> = emptyList(),
)

/** Root wrapper matching the hadith.json structure. */
data class HadithWrapper(
    val collections: List<HadithCollection> = emptyList(),
)

package me.shawaf.themuslimapp.data.local.model

import me.shawaf.themuslimapp.data.local.dp.entity.ZekrEntity

data class ZekrModel(
    val zekr: String,
    val zekr_en: String,
    val count: Int,
    val sanad: String,
) {
    fun toZekrEntity() = ZekrEntity(
        zekr = this.zekr,
        zekrEn = this.zekr_en,
        count = this.count,
        sanad = this.sanad,
        createdAt = System.currentTimeMillis()
    )
}

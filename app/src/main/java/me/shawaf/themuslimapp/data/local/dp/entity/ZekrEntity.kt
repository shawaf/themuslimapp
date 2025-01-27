package me.shawaf.themuslimapp.data.local.dp.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "zekr")
data class ZekrEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val zekr: String,
    val zekrEn: String,
    val count: Int,
    val sanad: String,
    val createdAt: Long
)
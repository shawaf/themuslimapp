package me.shawaf.themuslimapp.features.main.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasbih")
data class TasbihEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val count: Int,
    val tasbih: List<String>,
    val prayer: String,
    val timestamp: Long
)
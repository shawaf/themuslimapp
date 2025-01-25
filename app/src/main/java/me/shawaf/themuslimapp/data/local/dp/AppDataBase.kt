package me.shawaf.themuslimapp.data.local.dp

import androidx.room.Database
import androidx.room.RoomDatabase
import me.shawaf.themuslimapp.features.main.data.local.dao.TasbihDao
import me.shawaf.themuslimapp.features.main.data.local.entity.TasbihEntity

@Database(entities = [TasbihEntity::class], version = 1)
abstract class AppDataBase : RoomDatabase(){
    abstract fun tasbihDao(): TasbihDao
}
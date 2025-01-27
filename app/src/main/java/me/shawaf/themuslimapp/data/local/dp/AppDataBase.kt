package me.shawaf.themuslimapp.data.local.dp

import androidx.room.Database
import androidx.room.RoomDatabase
import me.shawaf.themuslimapp.data.local.dp.dao.ZekrDao
import me.shawaf.themuslimapp.features.main.data.local.entity.ZekrEntity

@Database(entities = [ZekrEntity::class], version = 1)
abstract class AppDataBase : RoomDatabase(){
    abstract fun zekrDao(): ZekrDao
}
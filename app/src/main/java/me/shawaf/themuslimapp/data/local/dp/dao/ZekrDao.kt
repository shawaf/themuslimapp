package me.shawaf.themuslimapp.data.local.dp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.shawaf.themuslimapp.data.local.dp.ZEKR_TABLE_NAME
import me.shawaf.themuslimapp.data.local.dp.entity.ZekrEntity

@Dao
interface ZekrDao {
    @Insert
    suspend fun insertZekr(zekrEntity: ZekrEntity)

    @Query("SELECT * from $ZEKR_TABLE_NAME Order BY createdAt DESC")
    fun getAllZekr(): Flow<List<ZekrEntity>>
}
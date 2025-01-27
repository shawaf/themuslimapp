package me.shawaf.themuslimapp.data.local.dp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.shawaf.themuslimapp.features.main.data.local.entity.ZekrEntity

@Dao
interface ZekrDao {

    @Insert
    suspend fun insertTasbih(zekrEntity: ZekrEntity)

    @Query("SELECT * from zekr Order BY createdAt DESC")
    suspend fun getAllTasbih(): Flow<List<ZekrEntity>>
}
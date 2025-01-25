package me.shawaf.themuslimapp.features.main.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import me.shawaf.themuslimapp.features.main.data.local.entity.TasbihEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TasbihDao {

    @Insert
    suspend fun insertTasbih(tasbihEntity: TasbihEntity)

    @Query("SELECT * from tasbih Order BY timestamp DESC")
    suspend fun getAllTasbih(): Flow<List<TasbihEntity>>
}
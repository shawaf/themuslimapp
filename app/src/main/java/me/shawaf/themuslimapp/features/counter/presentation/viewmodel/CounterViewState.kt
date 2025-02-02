package me.shawaf.themuslimapp.features.counter.presentation.viewmodel

import androidx.compose.runtime.Immutable
import me.shawaf.themuslimapp.data.local.dp.entity.ZekrEntity
import me.shawaf.themuslimapp.data.local.model.ZekrModel
import me.shawaf.themuslimapp.data.local.prefers.ConfigModel

@Immutable
data class CounterViewState(
    val zekrList: List<ZekrModel> = emptyList(),
    val configModel: ConfigModel = ConfigModel(),
    val currentZekrEntity: ZekrEntity ,
    var counter: Int = 0
)

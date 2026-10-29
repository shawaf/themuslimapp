package me.shawaf.themuslimapp.features.counter.presentation.viewmodel

import androidx.compose.runtime.Immutable
import me.shawaf.themuslimapp.data.local.dp.entity.ZekrEntity
import me.shawaf.themuslimapp.data.local.prefers.ConfigModel

@Immutable
data class CounterViewState(
    val configModel: ConfigModel = ConfigModel(),
    val items: List<ZekrEntity> = emptyList(),
    val currentIndex: Int = 0,
    var counter: Int = 0,
) {
    val currentItem: ZekrEntity?
        get() = items.getOrNull(currentIndex)
}

package me.shawaf.themuslimapp.features.counter.presentation.viewmodel

import me.shawaf.themuslimapp.data.local.dp.entity.ZekrEntity
import me.shawaf.themuslimapp.data.local.model.ZekrModel


sealed class CounterIntent {
    object LoadConfigData : CounterIntent()
    object ToggleSoundState : CounterIntent()
    object ToggleVibrationState : CounterIntent()
    object LoadZekrListFromJson : CounterIntent()
    object IncreaseCounter: CounterIntent()
    object ResetCounter: CounterIntent()
    data class SetCurrentZekrEntity(val zekrModel: ZekrModel): CounterIntent()
    object SaveZekrUpdate: CounterIntent()
}
package me.shawaf.themuslimapp.features.counter.presentation.viewmodel

sealed class CounterIntent {
    object LoadConfigData : CounterIntent()
    object ToggleSoundState : CounterIntent()
    object ToggleVibrationState : CounterIntent()
    /** One tap: increments the current dhikr, capped at its target; marks completed when reached. */
    object Count : CounterIntent()
    object ResetCounter : CounterIntent()
    object SaveZekrUpdate : CounterIntent()
}

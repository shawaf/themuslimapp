package me.shawaf.themuslimapp.features.counter.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.shawaf.themuslimapp.data.local.dp.entity.ZekrEntity
import me.shawaf.themuslimapp.data.local.model.ZekrModel
import me.shawaf.themuslimapp.data.local.prefers.SharedPreferencesManager
import me.shawaf.themuslimapp.features.counter.domain.usecase.GetJsonZekrListUseCase
import me.shawaf.themuslimapp.features.counter.domain.usecase.InsertZekrUseCase
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CounterViewModel @Inject constructor(
    private val insertZekrUseCase: InsertZekrUseCase,
    private val getJsonZekrListUseCase: GetJsonZekrListUseCase,
    private val sharedPreferencesManager: SharedPreferencesManager,
) : ViewModel() {
    private val _viewState =
        MutableStateFlow(CounterViewState(currentZekrEntity = ZekrEntity(0, "", "", 0, "", 0)))

    val viewState: StateFlow<CounterViewState> = _viewState

    init {
        handleIntent(viewIntent = CounterIntent.LoadConfigData)
        handleIntent(viewIntent = CounterIntent.LoadZekrListFromJson)
    }

    fun handleIntent(viewIntent: CounterIntent) {
        when (viewIntent) {
            CounterIntent.LoadConfigData -> {
                viewModelScope.launch {
                    getConfigData()
                }
            }

            is CounterIntent.SetCurrentZekrEntity -> {
                Timber.e("Current Zekr Model : ${viewIntent.zekrModel}")
                _viewState.value =
                    _viewState.value.copy(currentZekrEntity = viewIntent.zekrModel.toZekrEntity())
            }

            CounterIntent.ToggleSoundState -> {
                toggleSoundState()
            }

            CounterIntent.ToggleVibrationState -> {
                toggleVibrationState()
            }

            CounterIntent.LoadZekrListFromJson -> {
                loadZekrFromJson { zekrModels ->
                    _viewState.value = _viewState.value.copy(zekrList = zekrModels)
                }
            }

            CounterIntent.IncreaseCounter -> {
                _viewState.value = _viewState.value.copy(counter = _viewState.value.counter + 1)
            }

            CounterIntent.ResetCounter -> {
                _viewState.value = _viewState.value.copy(counter = 0)
            }

            is CounterIntent.SaveZekrUpdate -> {

                viewModelScope.launch {
                    val currentZekrEntity =
                        _viewState.value.currentZekrEntity.copy(createdAt = System.currentTimeMillis())
                    insertZekrUseCase(currentZekrEntity)
                }
            }
        }
    }

    private suspend fun getConfigData() {
        val savedConfigModel = sharedPreferencesManager.getConfig()
        _viewState.value = _viewState.value.copy(configModel = savedConfigModel)
    }

    private fun toggleSoundState() {
        viewModelScope.launch {
            val updatedConfig = _viewState.value.configModel.copy(
                soundEnabled = !_viewState.value.configModel.soundEnabled
            )
            sharedPreferencesManager.saveConfig(updatedConfig)
            getConfigData()
        }
    }

    private fun toggleVibrationState() {
        viewModelScope.launch {
            val updatedConfig = _viewState.value.configModel.copy(
                vibrationEnabled = !_viewState.value.configModel.vibrationEnabled
            )
            sharedPreferencesManager.saveConfig(updatedConfig)
            getConfigData()
        }
    }

    private fun loadZekrFromJson(callback: (List<ZekrModel>) -> Unit) {
        viewModelScope.launch {
            callback(getJsonZekrListUseCase())
        }
    }
}
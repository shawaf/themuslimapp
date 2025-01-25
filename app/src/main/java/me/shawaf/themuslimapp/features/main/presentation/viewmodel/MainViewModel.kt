package me.shawaf.themuslimapp.features.main.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.shawaf.themuslimapp.data.local.ConfigModel
import me.shawaf.themuslimapp.data.local.prefers.SharedPreferencesManager
import me.shawaf.themuslimapp.features.main.data.local.entity.ZekrEntity
import me.shawaf.themuslimapp.utils.AzkarUtils
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private var sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {

    private var _configModel = MutableStateFlow(ConfigModel())
    val configModel: StateFlow<ConfigModel> get() = _configModel

    init {
        viewModelScope.launch {
            getConfigData()
        }
    }

    private suspend fun getConfigData() {
        val savedConfigModel = sharedPreferencesManager.getConfig()
        _configModel.value = savedConfigModel
    }

    fun toggleSoundState() {
        viewModelScope.launch {
            val updatedConfig = _configModel.value.copy(
                soundEnabled = !_configModel.value.soundEnabled
            )
            sharedPreferencesManager.saveConfig(updatedConfig)
            getConfigData()
        }
    }

    fun toggleVibrationState() {
        viewModelScope.launch {
            val updatedConfig = _configModel.value.copy(
                vibrationEnabled = !_configModel.value.vibrationEnabled
            )
            sharedPreferencesManager.saveConfig(updatedConfig)
            getConfigData()
        }
    }

    fun loadAzkarFromJson(context: Context): List<ZekrEntity> {
        var azkarList = emptyList<ZekrEntity>()
        viewModelScope.launch {
            azkarList = AzkarUtils.loadAzkar(context = context)
        }
        return azkarList
    }


}
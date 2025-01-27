package me.shawaf.themuslimapp.features.main.presentation.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.shawaf.themuslimapp.data.local.prefers.ConfigModel
import me.shawaf.themuslimapp.data.local.prefers.SharedPreferencesManager
import me.shawaf.themuslimapp.data.local.dp.entity.ZekrEntity
import me.shawaf.themuslimapp.utils.AzkarUtils
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private var sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {

    var counter  = mutableIntStateOf(0)

    var configModel = MutableStateFlow(ConfigModel())
        private set

    init {
        viewModelScope.launch {
            getConfigData()
        }
    }

    private suspend fun getConfigData() {
        val savedConfigModel = sharedPreferencesManager.getConfig()
        configModel.value = savedConfigModel
    }

    fun toggleSoundState() {
        viewModelScope.launch {
            val updatedConfig = configModel.value.copy(
                soundEnabled = !configModel.value.soundEnabled
            )
            sharedPreferencesManager.saveConfig(updatedConfig)
            getConfigData()
        }
    }

    fun toggleVibrationState() {
        viewModelScope.launch {
            val updatedConfig = configModel.value.copy(
                vibrationEnabled = !configModel.value.vibrationEnabled
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
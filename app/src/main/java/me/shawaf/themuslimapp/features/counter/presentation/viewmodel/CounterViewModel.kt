package me.shawaf.themuslimapp.features.counter.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.shawaf.themuslimapp.data.local.dp.entity.ZekrEntity
import me.shawaf.themuslimapp.data.local.prefers.SharedPreferencesManager
import me.shawaf.themuslimapp.features.counter.domain.usecase.InsertZekrUseCase
import me.shawaf.themuslimapp.features.duas.domain.usecase.GetAdhkarUseCase
import javax.inject.Inject

@HiltViewModel
class CounterViewModel @Inject constructor(
    private val insertZekrUseCase: InsertZekrUseCase,
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val getAdhkarUseCase: GetAdhkarUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    // Either a whole category (count through all its azkar) or a single selected dhikr.
    private val argCategory = savedStateHandle.get<String>("category").orEmpty()
    private val argAr = savedStateHandle.get<String>("ar").orEmpty()
    private val argEn = savedStateHandle.get<String>("en").orEmpty()
    private val argCount = savedStateHandle.get<Int>("count") ?: 0

    private val _viewState = MutableStateFlow(CounterViewState())
    val viewState: StateFlow<CounterViewState> = _viewState

    init {
        handleIntent(CounterIntent.LoadConfigData)
        loadItems()
    }

    private fun loadItems() {
        viewModelScope.launch {
            val items = if (argCategory.isNotBlank()) {
                getAdhkarUseCase()
                    .filter { it.categoryId == argCategory }
                    .map { ZekrEntity(zekr = it.ar, zekrEn = it.en, count = it.count, sanad = it.reference, createdAt = 0L) }
            } else {
                listOf(ZekrEntity(zekr = argAr, zekrEn = argEn, count = argCount, sanad = "", createdAt = 0L))
            }
            _viewState.value = _viewState.value.copy(items = items, currentIndex = 0, counter = 0)
        }
    }

    fun handleIntent(viewIntent: CounterIntent) {
        when (viewIntent) {
            CounterIntent.LoadConfigData -> {
                viewModelScope.launch { getConfigData() }
            }

            CounterIntent.ToggleSoundState -> toggleSoundState()

            CounterIntent.ToggleVibrationState -> toggleVibrationState()

            CounterIntent.Count -> {
                // Synchronous on the main thread → rapid taps can't overshoot or skip a card.
                val state = _viewState.value
                val list = state.items
                if (list.isNotEmpty()) {
                    val index = state.currentIndex.coerceIn(0, list.lastIndex)
                    val target = list[index].count
                    val next = state.counter + 1
                    _viewState.value = if (target > 0 && next >= target) {
                        val nextIndex = if (index >= list.lastIndex) 0 else index + 1
                        state.copy(counter = 0, currentIndex = nextIndex)
                    } else {
                        state.copy(counter = next, currentIndex = index)
                    }
                }
            }

            CounterIntent.ResetCounter -> {
                _viewState.value = _viewState.value.copy(counter = 0)
            }

            CounterIntent.SaveZekrUpdate -> {
                viewModelScope.launch {
                    val current = _viewState.value.currentItem ?: return@launch
                    insertZekrUseCase(current.copy(createdAt = System.currentTimeMillis()))
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
}

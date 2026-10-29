package me.shawaf.themuslimapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.shawaf.themuslimapp.data.local.prefers.SharedPreferencesManager
import javax.inject.Inject

/** Holds the app's theme mode ("system" | "light" | "dark"), persisted in prefs. */
@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val prefs: SharedPreferencesManager,
) : ViewModel() {

    private val _themeMode = MutableStateFlow("system")
    val themeMode: StateFlow<String> = _themeMode

    init {
        viewModelScope.launch {
            _themeMode.value = prefs.getConfig().themeMode
        }
    }

    fun setThemeMode(mode: String) {
        viewModelScope.launch {
            val updated = prefs.getConfig().copy(themeMode = mode)
            prefs.saveConfig(updated)
            _themeMode.value = mode
        }
    }
}

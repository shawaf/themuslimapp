package me.shawaf.themuslimapp.features.root

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import me.shawaf.themuslimapp.ui.theme.ColorThemes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(): ViewModel() {

    private val themeKeys = ColorThemes.keys.toList()
    private var currentIndex = 0

    private val _currentTheme = mutableStateOf(ColorThemes[themeKeys[currentIndex]]!!)
    val currentTheme: State<ColorScheme> = _currentTheme

    fun switchToNextTheme() {
        currentIndex = (currentIndex + 1) % themeKeys.size
        _currentTheme.value = ColorThemes[themeKeys[currentIndex]]!!
    }
}
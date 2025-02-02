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
) : ViewModel() {


}
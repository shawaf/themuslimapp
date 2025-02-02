package me.shawaf.themuslimapp.features.history.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.shawaf.themuslimapp.data.local.dp.entity.ZekrEntity
import me.shawaf.themuslimapp.features.counter.domain.usecase.GetSavedZekrListUseCase
import me.shawaf.themuslimapp.features.history.presentation.model.HistoryFilter
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getSavedZekrListUseCase: GetSavedZekrListUseCase
) : ViewModel() {

    private val _savedZekrList = MutableStateFlow<List<ZekrEntity>>(emptyList())
    val savedZekrList: StateFlow<List<ZekrEntity>> = _savedZekrList



    init {
        getDBZekrList()
    }

    private val  _selectedFilter = mutableStateOf(HistoryFilter.BY_DAY)
    val selectedFilter: State<HistoryFilter> = _selectedFilter

    fun changeSelectedFilter(filter: HistoryFilter){
        _selectedFilter.value = filter
    }

    private fun getDBZekrList() {
        CoroutineScope(Dispatchers.IO).launch {
            getSavedZekrListUseCase().collect { zekrList ->
                Timber.e("DB List : $zekrList")
                _savedZekrList.value = zekrList
            }
        }
    }
}
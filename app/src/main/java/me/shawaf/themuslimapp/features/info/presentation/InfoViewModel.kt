package me.shawaf.themuslimapp.features.info.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.shawaf.themuslimapp.data.local.dp.entity.ZekrEntity
import me.shawaf.themuslimapp.data.local.model.ZekrModel
import me.shawaf.themuslimapp.features.counter.domain.usecase.GetJsonZekrListUseCase
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val getJsonZekrListUseCase: GetJsonZekrListUseCase
) : ViewModel() {

    private val _savedZekrList = MutableStateFlow(emptyList<ZekrModel>())
    val savedZekrList: StateFlow<List<ZekrModel>> = _savedZekrList

    init {
        loadZekrFromJson()
    }

    private fun loadZekrFromJson(){
        viewModelScope.launch {
            _savedZekrList.value = getJsonZekrListUseCase.invoke()
        }
    }
}
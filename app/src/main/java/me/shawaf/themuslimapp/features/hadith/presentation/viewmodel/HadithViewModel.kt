package me.shawaf.themuslimapp.features.hadith.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.shawaf.themuslimapp.features.hadith.domain.usecase.GetHadithCollectionsUseCase
import javax.inject.Inject

@HiltViewModel
class HadithViewModel @Inject constructor(
    private val getHadithCollectionsUseCase: GetHadithCollectionsUseCase,
) : ViewModel() {

    private val _viewState = MutableStateFlow(HadithViewState())
    val viewState: StateFlow<HadithViewState> = _viewState

    init {
        handleIntent(HadithIntent.LoadHadith)
    }

    fun handleIntent(intent: HadithIntent) {
        when (intent) {
            HadithIntent.LoadHadith -> viewModelScope.launch {
                val collections = getHadithCollectionsUseCase()
                _viewState.value = _viewState.value.copy(
                    collections = collections,
                    selectedCollectionId = collections.firstOrNull()?.id.orEmpty(),
                    isLoading = false,
                )
            }
            is HadithIntent.SelectCollection -> {
                _viewState.value = _viewState.value.copy(selectedCollectionId = intent.collectionId)
            }
        }
    }
}

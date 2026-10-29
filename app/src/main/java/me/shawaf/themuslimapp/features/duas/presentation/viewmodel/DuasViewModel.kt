package me.shawaf.themuslimapp.features.duas.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.shawaf.themuslimapp.features.duas.domain.usecase.GetDuaCategoriesUseCase
import javax.inject.Inject

@HiltViewModel
class DuasViewModel @Inject constructor(
    private val getDuaCategoriesUseCase: GetDuaCategoriesUseCase,
) : ViewModel() {

    private val _viewState = MutableStateFlow(DuasViewState())
    val viewState: StateFlow<DuasViewState> = _viewState

    init {
        handleIntent(DuasIntent.LoadDuas)
    }

    fun handleIntent(intent: DuasIntent) {
        when (intent) {
            DuasIntent.LoadDuas -> loadDuas()
            is DuasIntent.SelectCategory -> {
                _viewState.value = _viewState.value.copy(selectedCategoryId = intent.categoryId)
            }
            is DuasIntent.Search -> {
                _viewState.value = _viewState.value.copy(searchQuery = intent.query)
            }
        }
    }

    private fun loadDuas() {
        viewModelScope.launch {
            val categories = getDuaCategoriesUseCase()
            _viewState.value = _viewState.value.copy(
                categories = categories,
                selectedCategoryId = categories.firstOrNull()?.id.orEmpty(),
                isLoading = false,
            )
        }
    }
}

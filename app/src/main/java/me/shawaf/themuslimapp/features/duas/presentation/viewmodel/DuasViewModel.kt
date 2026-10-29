package me.shawaf.themuslimapp.features.duas.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.shawaf.themuslimapp.data.local.model.AdhkarCategory
import me.shawaf.themuslimapp.features.duas.domain.usecase.GetAdhkarUseCase
import javax.inject.Inject

@HiltViewModel
class DuasViewModel @Inject constructor(
    private val getAdhkarUseCase: GetAdhkarUseCase,
) : ViewModel() {

    private val _viewState = MutableStateFlow(DuasViewState())
    val viewState: StateFlow<DuasViewState> = _viewState

    init {
        handleIntent(DuasIntent.LoadDuas)
    }

    fun handleIntent(intent: DuasIntent) {
        when (intent) {
            DuasIntent.LoadDuas -> loadAdhkar()
            is DuasIntent.SelectCategory -> {
                _viewState.value = _viewState.value.copy(selectedCategoryId = intent.categoryId)
            }
            is DuasIntent.Search -> {
                _viewState.value = _viewState.value.copy(searchQuery = intent.query)
            }
        }
    }

    private fun loadAdhkar() {
        viewModelScope.launch {
            val all = getAdhkarUseCase()
            val categories = buildList {
                add(AdhkarCategory(DuasViewState.ALL, "الكل", "All"))
                all.map { Triple(it.categoryId, it.categoryAr, it.categoryEn) }
                    .distinctBy { it.first }
                    .forEach { add(AdhkarCategory(it.first, it.second, it.third)) }
            }
            _viewState.value = _viewState.value.copy(
                adhkar = all,
                categories = categories,
                selectedCategoryId = DuasViewState.ALL,
                isLoading = false,
            )
        }
    }
}

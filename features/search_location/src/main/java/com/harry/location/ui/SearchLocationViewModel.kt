@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class, kotlinx.coroutines.FlowPreview::class)

package com.harry.location.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harry.location.domain.usecase.SearchLocationsUseCase
import com.harry.location.ui.mapper.SearchLocationUiMapper
import com.harry.location.ui.model.SearchLocationUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private const val SEARCH_DEBOUNCE_MS = 300L
private const val MIN_QUERY_SIZE = 2

class SearchLocationViewModel(
    private val searchLocationsUseCase: SearchLocationsUseCase,
    private val searchLocationUiMapper: SearchLocationUiMapper,
) : ViewModel() {
    private val _uiState = MutableStateFlow<SearchLocationUiState>(SearchLocationUiState.Idle)
    val uiState: StateFlow<SearchLocationUiState> = _uiState.asStateFlow()

    private val searchQueryFlow = MutableSharedFlow<String>(replay = 1)

    init {
        searchQueryFlow
            .distinctUntilChanged()
            .debounce(SEARCH_DEBOUNCE_MS)
            .filter { query -> query.isNotBlank() && query.length >= MIN_QUERY_SIZE }
            .flatMapLatest { query ->
                flow {
                    emit(SearchLocationUiState.Loading)

                    val result = searchLocationsUseCase(query)

                    val uiState =
                        if (result.isSuccess) {
                            val searchResult = result.getOrThrow()
                            SearchLocationUiState.Success(
                                locations = searchLocationUiMapper.mapToSearchResults(searchResult.locations),
                                query = searchResult.query,
                            )
                        } else {
                            SearchLocationUiState.Error(
                                message = result.exceptionOrNull()?.message ?: "Unknown error occurred",
                                query = query,
                            )
                        }

                    emit(uiState)
                }
            }.onEach { uiState ->
                _uiState.value = uiState
            }.launchIn(viewModelScope)
    }

    fun searchLocations(query: String) {
        if (query.isBlank() || query.length < MIN_QUERY_SIZE) {
            _uiState.value = SearchLocationUiState.Idle
            return
        }

        searchQueryFlow.tryEmit(query)
    }

    fun clearSearch() {
        _uiState.value = SearchLocationUiState.Idle
    }
}

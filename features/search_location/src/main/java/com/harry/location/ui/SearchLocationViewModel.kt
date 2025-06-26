@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class, kotlinx.coroutines.FlowPreview::class)

package com.harry.location.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harry.location.R
import com.harry.location.domain.usecase.SearchLocationsUseCase
import com.harry.location.domain.usecase.SetLocationUseCase
import com.harry.location.ui.mapper.SearchLocationUiMapper
import com.harry.location.ui.model.SearchLocationUiState
import com.harry.location.ui.model.SearchResult
import com.harry.utils.ResourceProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private const val SEARCH_DEBOUNCE_MS = 300L
private const val MIN_QUERY_SIZE = 2

class SearchLocationViewModel(
    private val searchLocationsUseCase: SearchLocationsUseCase,
    private val setLocationUseCase: SetLocationUseCase,
    private val searchLocationUiMapper: SearchLocationUiMapper,
    private val resourceProvider: ResourceProvider,
) : ViewModel() {
    private val _uiState = MutableStateFlow<SearchLocationUiState>(SearchLocationUiState.Idle)
    val uiState: StateFlow<SearchLocationUiState> = _uiState.asStateFlow()

    private val searchQueryFlow = MutableStateFlow("")

    init {
        searchQueryFlow
            .debounce(SEARCH_DEBOUNCE_MS)
            .filter { query -> query.isNotBlank() && query.length >= MIN_QUERY_SIZE }
            .flatMapLatest { query -> performSearch(query) }
            .onEach { uiState -> _uiState.value = uiState }
            .launchIn(viewModelScope)
    }

    private fun performSearch(query: String) =
        flow {
            emit(SearchLocationUiState.Loading)
            emit(executeSearch(query))
        }

    private suspend fun executeSearch(query: String): SearchLocationUiState {
        val result = searchLocationsUseCase(query)

        return if (result.isSuccess) {
            val searchResult = result.getOrThrow()
            SearchLocationUiState.Success(
                locations = searchLocationUiMapper.mapToSearchResults(searchResult.locations),
                query = searchResult.query,
            )
        } else {
            SearchLocationUiState.Error(
                message =
                    result.exceptionOrNull()?.message
                        ?: resourceProvider.getString(R.string.error_unknown),
                query = query,
            )
        }
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

    fun onLocationSelected(searchResult: SearchResult) {
        viewModelScope.launch {
            val location = searchLocationUiMapper.mapToLocation(searchResult)
            val result = setLocationUseCase(location)

            if (result.isSuccess) {
                _uiState.value = SearchLocationUiState.NavigateToWeather(location)
            }
        }
    }

    fun retrySearch() {
        val currentState = _uiState.value
        if (currentState is SearchLocationUiState.Error) {
            viewModelScope.launch {
                _uiState.value = SearchLocationUiState.Loading
                _uiState.value = executeSearch(currentState.query)
            }
        }
    }
}

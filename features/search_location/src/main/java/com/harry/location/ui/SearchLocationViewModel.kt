package com.harry.location.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harry.location.domain.usecase.SearchLocationsUseCase
import com.harry.location.ui.mapper.SearchLocationUiMapper
import com.harry.location.ui.model.SearchLocationUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchLocationViewModel(
    private val searchLocationsUseCase: SearchLocationsUseCase,
    private val searchLocationUiMapper: SearchLocationUiMapper,
) : ViewModel() {
    private val _uiState = MutableStateFlow<SearchLocationUiState>(SearchLocationUiState.Idle)
    val uiState: StateFlow<SearchLocationUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun searchLocations(query: String) {
        // Cancel previous search
        searchJob?.cancel()

        if (query.isBlank() || query.length < 2) {
            _uiState.value = SearchLocationUiState.Idle
            return
        }

        searchJob =
            viewModelScope.launch {
                // Debounce for 300ms
                delay(300)

                _uiState.value = SearchLocationUiState.Loading

                val result = searchLocationsUseCase(query)

                _uiState.value =
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
            }
    }

    fun clearSearch() {
        searchJob?.cancel()
        _uiState.value = SearchLocationUiState.Idle
    }
}

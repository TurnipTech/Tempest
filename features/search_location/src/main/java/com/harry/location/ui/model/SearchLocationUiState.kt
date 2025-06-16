package com.harry.location.ui.model

import com.harry.location.domain.model.Location

sealed class SearchLocationUiState {
    data object Idle : SearchLocationUiState()

    data object Loading : SearchLocationUiState()

    data class Success(
        val locations: List<SearchResult>,
        val query: String,
    ) : SearchLocationUiState()

    data class Error(
        val message: String,
        val query: String,
    ) : SearchLocationUiState()

    data class NavigateToWeather(
        val location: Location,
    ) : SearchLocationUiState()
}

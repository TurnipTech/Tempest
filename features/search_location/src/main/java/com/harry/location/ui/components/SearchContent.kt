package com.harry.location.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.harry.location.ui.model.SearchLocationUiState
import com.harry.location.ui.model.SearchResult

@Composable
fun SearchContent(
    uiState: SearchLocationUiState,
    query: String,
    onLocationSelected: (SearchResult) -> Unit,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
) {
    if (!isExpanded) return

    when (uiState) {
        is SearchLocationUiState.Loading -> {
            SearchLoadingIndicator(modifier = modifier)
        }
        is SearchLocationUiState.Success -> {
            if (uiState.locations.isNotEmpty()) {
                SearchResultsList(
                    searchResults = uiState.locations,
                    onLocationSelected = onLocationSelected,
                    modifier = modifier,
                )
            } else if (query.isNotEmpty()) {
                SearchEmptyState(modifier = modifier)
            }
        }
        else -> {
        }
    }
}

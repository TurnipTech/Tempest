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
    when (uiState) {
        is SearchLocationUiState.Loading -> {
            if (isExpanded) {
                SearchLoadingIndicator(modifier = modifier)
            }
        }
        is SearchLocationUiState.Success -> {
            if (isExpanded) {
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
        }
        is SearchLocationUiState.Error -> {
            if (isExpanded && query.isNotEmpty()) {
                SearchEmptyState(modifier = modifier)
            }
        }
        else -> {
            if (isExpanded && query.isNotEmpty()) {
                SearchEmptyState(modifier = modifier)
            }
        }
    }
}

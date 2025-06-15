package com.harry.location.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.harry.location.ui.components.LocationSearchBar
import com.harry.location.ui.model.SearchLocationUiState
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchLocationScreen(viewModel: SearchLocationViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Text(
            text = "Search Location",
            style =
                MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        val searchResults =
            when (val state = uiState) {
                is SearchLocationUiState.Success -> state.locations.map { it.displayName }
                else -> emptyList()
            }

        LocationSearchBar(
            onSearchQueryChange = { query ->
                viewModel.searchLocations(query)
            },
            searchResults = searchResults,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun SearchLocationScreenPreview() {
    SearchLocationScreen()
}

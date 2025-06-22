package com.harry.location.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.harry.design.Tempest
import com.harry.location.domain.model.Location
import com.harry.location.ui.components.LocationSearchBarComponent
import com.harry.location.ui.components.SearchContent
import com.harry.location.ui.components.WelcomeMessage
import com.harry.location.ui.model.SearchLocationUiState
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchLocationScreen(
    viewModel: SearchLocationViewModel = koinViewModel(),
    onNavigateToWeather: (Location) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var query by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }

    (uiState as? SearchLocationUiState.NavigateToWeather)?.let { state ->
        LaunchedEffect(state.location) {
            onNavigateToWeather(state.location)
        }
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Tempest.colors.backgroundGradient)
                .padding(),
    ) {
        LocationSearchBarComponent(
            query = query,
            onQueryChange = { newQuery ->
                query = newQuery
                viewModel.searchLocations(newQuery)
                expanded = newQuery.isNotEmpty()
            },
            onSearch = {
                expanded = false
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
            onClearQuery = {
                query = ""
                viewModel.searchLocations("")
                expanded = false
            },
        ) {
            SearchContent(
                uiState = uiState,
                query = query,
                isExpanded = expanded,
                onLocationSelected = { location ->
                    query = location.displayName
                    expanded = false
                    viewModel.onLocationSelected(location)
                },
            )
        }

        WelcomeMessage()
    }
}

@Preview
@Composable
private fun SearchLocationScreenPreview() {
    SearchLocationScreen()
}

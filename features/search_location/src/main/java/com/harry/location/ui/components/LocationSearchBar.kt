package com.harry.location.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSearchBar(
    modifier: Modifier = Modifier,
    onLocationSelected: (String) -> Unit = {},
    onSearchQueryChange: (String) -> Unit = {},
    searchResults: List<String> = emptyList(),
) {
    var query by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }

    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = { newQuery ->
                    query = newQuery
                    onSearchQueryChange(newQuery)
                },
                onSearch = { searchQuery ->
                    expanded = false
                    if (searchQuery.isNotBlank()) {
                        onLocationSelected(searchQuery)
                    }
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = { Text("Search for a city or location") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                query = ""
                                onSearchQueryChange("")
                            },
                        ) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
            )
        },
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier.fillMaxWidth(),
    ) {
        LazyColumn {
            items(searchResults) { location ->
                ListItem(
                    headlineContent = { Text(location) },
                    leadingContent = {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = "Location",
                        )
                    },
                    modifier =
                        Modifier
                            .clickable {
                                query = location
                                expanded = false
                                onLocationSelected(location)
                            }.fillMaxWidth()
                            .padding(horizontal = 16.dp),
                )
            }
        }
    }
}

@Preview
@Composable
private fun LocationSearchBarPreview() {
    LocationSearchBar(
        searchResults =
            listOf(
                "London, UK",
                "London, Ontario, Canada",
                "New London, CT, USA",
                "Paris, France",
                "Paris, TX, USA",
            ),
    )
}

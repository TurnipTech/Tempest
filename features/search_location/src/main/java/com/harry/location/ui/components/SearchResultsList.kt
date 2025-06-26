package com.harry.location.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.harry.design.OverlayColors
import com.harry.location.R
import com.harry.location.ui.model.SearchResult

@Composable
fun SearchResultsList(
    searchResults: List<SearchResult>,
    onLocationSelected: (SearchResult) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier =
            modifier
                .fillMaxWidth(),
    ) {
        items(searchResults) { location ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier =
                    Modifier
                        .clickable {
                            onLocationSelected(location)
                        }.fillMaxWidth()
                        .padding(vertical = 16.dp),
            ) {
                Spacer(Modifier.padding(8.dp))
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = stringResource(R.string.location_icon_description),
                    tint = OverlayColors.contentPrimary,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(Modifier.padding(8.dp))
                Text(
                    location.displayName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = OverlayColors.contentPrimary,
                )
            }
        }
    }
}

@Preview
@Composable
private fun SearchResultsListPreview() {
    SearchResultsList(
        searchResults =
            listOf(
                SearchResult("London, UK", "London", 51.5074, -0.1278, "United Kingdom"),
                SearchResult("London, Ontario, Canada", "London", 42.9849, -81.2453, "Canada", "Ontario"),
                SearchResult("New London, CT, USA", "New London", 41.3556, -72.0995, "United States", "Connecticut"),
                SearchResult("Paris, France", "Paris", 48.8566, 2.3522, "France"),
                SearchResult("Paris, TX, USA", "Paris", 33.6609, -95.5555, "United States", "Texas"),
            ),
        onLocationSelected = {},
    )
}

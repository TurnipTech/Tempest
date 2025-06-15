package com.harry.location.ui.mapper

import com.harry.location.domain.model.Location
import com.harry.location.ui.model.SearchResult

class SearchLocationUiMapper {
    fun mapToSearchResult(location: Location): SearchResult {
        val displayName = "${location.name}, ${location.state?.let { "$it, " } ?: ""}${location.country}"

        return SearchResult(
            displayName = displayName,
            latitude = location.latitude,
            longitude = location.longitude,
        )
    }

    fun mapToSearchResults(locations: List<Location>): List<SearchResult> = locations.map { mapToSearchResult(it) }
}

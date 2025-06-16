package com.harry.location.ui.mapper

import com.harry.location.domain.model.Location
import com.harry.location.ui.model.SearchResult

class SearchLocationUiMapper {
    fun mapToSearchResult(location: Location): SearchResult {
        val displayName = "${location.name}, ${location.state?.let { "$it, " } ?: ""}${location.country}"

        return SearchResult(
            displayName = displayName,
            name = location.name,
            latitude = location.latitude,
            longitude = location.longitude,
            country = location.country,
            state = location.state,
            localNames = location.localNames,
        )
    }

    fun mapToSearchResults(locations: List<Location>): List<SearchResult> = locations.map { mapToSearchResult(it) }

    fun mapToLocation(searchResult: SearchResult): Location =
        Location(
            name = searchResult.name,
            localNames = searchResult.localNames,
            latitude = searchResult.latitude,
            longitude = searchResult.longitude,
            country = searchResult.country,
            state = searchResult.state,
        )
}

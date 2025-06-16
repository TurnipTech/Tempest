package com.harry.location.ui.mapper

import com.harry.location.domain.model.Location
import com.harry.location.ui.model.SearchResult
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchLocationUiMapperTest {
    private val mapper = SearchLocationUiMapper()

    private val testLocation =
        Location(
            name = "London",
            latitude = 51.5074,
            longitude = -0.1278,
            country = "United Kingdom",
            state = "England",
            localNames = mapOf("en" to "London", "fr" to "Londres"),
        )

    private val testSearchResult =
        SearchResult(
            displayName = "London, England, United Kingdom",
            name = "London",
            latitude = 51.5074,
            longitude = -0.1278,
            country = "United Kingdom",
            state = "England",
            localNames = mapOf("en" to "London", "fr" to "Londres"),
        )

    @Test
    fun `mapToSearchResult should map domain Location to UI SearchResult`() {
        val result = mapper.mapToSearchResult(testLocation)

        assertEquals("London, England, United Kingdom", result.displayName)
        assertEquals("London", result.name)
        assertEquals(51.5074, result.latitude, 0.001)
        assertEquals(-0.1278, result.longitude, 0.001)
        assertEquals("United Kingdom", result.country)
        assertEquals("England", result.state)
        assertEquals(mapOf("en" to "London", "fr" to "Londres"), result.localNames)
    }

    @Test
    fun `mapToLocation should map UI SearchResult to domain Location`() {
        val result = mapper.mapToLocation(testSearchResult)

        assertEquals("London", result.name)
        assertEquals(51.5074, result.latitude, 0.001)
        assertEquals(-0.1278, result.longitude, 0.001)
        assertEquals("United Kingdom", result.country)
        assertEquals("England", result.state)
        assertEquals(mapOf("en" to "London", "fr" to "Londres"), result.localNames)
    }

    @Test
    fun `bidirectional mapping should be consistent`() {
        val searchResult = mapper.mapToSearchResult(testLocation)
        val backToLocation = mapper.mapToLocation(searchResult)

        assertEquals(testLocation, backToLocation)
    }
}

package com.harry.location.data.mapper

import com.harry.location.data.dto.GeocodingLocationDto
import org.junit.Assert.assertEquals
import org.junit.Test

class LocationMapperTest {
    @Test
    fun `mapToLocationSearchResult maps list of DTOs correctly`() {
        val dtos =
            listOf(
                GeocodingLocationDto(
                    name = "London",
                    localNames = mapOf("en" to "London", "fr" to "Londres"),
                    latitude = 51.5074,
                    longitude = -0.1278,
                    country = "GB",
                    state = "England",
                ),
                GeocodingLocationDto(
                    name = "Paris",
                    localNames = mapOf("en" to "Paris", "fr" to "Paris"),
                    latitude = 48.8566,
                    longitude = 2.3522,
                    country = "FR",
                    state = null,
                ),
            )
        val query = "European capitals"

        val result = LocationMapper.mapToLocationSearchResult(dtos, query)

        assertEquals(query, result.query)
        assertEquals(2, result.locations.size)

        val london = result.locations[0]
        assertEquals("London", london.name)
        assertEquals(51.5074, london.latitude, 0.0001)
        assertEquals(-0.1278, london.longitude, 0.0001)
        assertEquals("GB", london.country)
        assertEquals("England", london.state)
        assertEquals(mapOf("en" to "London", "fr" to "Londres"), london.localNames)

        val paris = result.locations[1]
        assertEquals("Paris", paris.name)
        assertEquals(48.8566, paris.latitude, 0.0001)
        assertEquals(2.3522, paris.longitude, 0.0001)
        assertEquals("FR", paris.country)
        assertEquals(null, paris.state)
        assertEquals(mapOf("en" to "Paris", "fr" to "Paris"), paris.localNames)
    }

    @Test
    fun `mapToLocationSearchResult handles empty list`() {
        val dtos = emptyList<GeocodingLocationDto>()
        val query = "nowhere"

        val result = LocationMapper.mapToLocationSearchResult(dtos, query)

        assertEquals(query, result.query)
        assertEquals(0, result.locations.size)
    }
}

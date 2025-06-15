package com.harry.location.data.repository

import com.harry.location.data.dto.GeocodingLocationDto
import com.harry.location.data.mapper.LocationMapper
import com.harry.location.domain.model.LocationSearchResult
import com.harry.location.domain.repository.LocationRepository
import com.harry.network.client.HttpClient
import com.harry.network.client.get

internal class OpenWeatherMapLocationRepository(
    private val client: HttpClient,
    private val mapper: LocationMapper,
    private val apiKey: String,
) : LocationRepository {
    companion object {
        private const val GEOCODING_BASE_URL = "https://api.openweathermap.org/geo/1.0"
        private const val DIRECT_GEOCODING_ENDPOINT = "$GEOCODING_BASE_URL/direct"
        private const val REVERSE_GEOCODING_ENDPOINT = "$GEOCODING_BASE_URL/reverse"

        // API parameter names
        private const val PARAM_QUERY = "q"
        private const val PARAM_LATITUDE = "lat"
        private const val PARAM_LONGITUDE = "lon"
        private const val PARAM_LIMIT = "limit"
        private const val PARAM_APP_ID = "appid"
    }

    override suspend fun searchLocations(query: String, limit: Int): Result<LocationSearchResult> {
        val params =
            mapOf(
                PARAM_QUERY to query,
                PARAM_LIMIT to limit.toString(),
                PARAM_APP_ID to apiKey,
            )

        return client
            .get<List<GeocodingLocationDto>>(
                endpoint = DIRECT_GEOCODING_ENDPOINT,
                params = params,
            ).map { dtos ->
                mapper.mapToLocationSearchResult(dtos, query)
            }
    }

    override suspend fun getLocationByCoordinates(
        latitude: Double,
        longitude: Double,
        limit: Int,
    ): Result<LocationSearchResult> {
        val params =
            mapOf(
                PARAM_LATITUDE to latitude.toString(),
                PARAM_LONGITUDE to longitude.toString(),
                PARAM_LIMIT to limit.toString(),
                PARAM_APP_ID to apiKey,
            )

        val coordinateQuery = "$latitude,$longitude"

        return client
            .get<List<GeocodingLocationDto>>(
                endpoint = REVERSE_GEOCODING_ENDPOINT,
                params = params,
            ).map { dtos ->
                mapper.mapToLocationSearchResult(dtos, coordinateQuery)
            }
    }
}

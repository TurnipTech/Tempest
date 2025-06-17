package com.harry.location.data.repository

import com.harry.location.data.dto.GeocodingLocationDto
import com.harry.location.data.mapper.LocationMapper
import com.harry.location.domain.model.Location
import com.harry.location.domain.model.LocationSearchResult
import com.harry.location.domain.repository.LocationRepository
import com.harry.network.client.HttpClient
import com.harry.network.client.get
import com.harry.storage.Storage
import com.harry.storage.get
import com.harry.storage.getNullable
import com.harry.storage.put
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

private const val GEOCODING_BASE_URL = "https://api.openweathermap.org/geo/1.0"
private const val DIRECT_GEOCODING_ENDPOINT = "$GEOCODING_BASE_URL/direct"
private const val REVERSE_GEOCODING_ENDPOINT = "$GEOCODING_BASE_URL/reverse"
private const val STORED_LOCATION_KEY = "stored_location"

// API parameter names
private const val PARAM_QUERY = "q"
private const val PARAM_LATITUDE = "lat"
private const val PARAM_LONGITUDE = "lon"
private const val PARAM_LIMIT = "limit"
private const val PARAM_APP_ID = "appid"

internal class LocationRepositoryImpl(
    private val client: HttpClient,
    private val mapper: LocationMapper,
    private val apiKey: String,
    private val storage: Storage,
    private val ioDispatcher: CoroutineDispatcher,
) : LocationRepository {
    override suspend fun searchLocations(query: String, limit: Int): Result<LocationSearchResult> =
        withContext(ioDispatcher) {
            val params =
                mapOf(
                    PARAM_QUERY to query,
                    PARAM_LIMIT to limit.toString(),
                    PARAM_APP_ID to apiKey,
                )

            client
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
    ): Result<LocationSearchResult> =
        withContext(ioDispatcher) {
            val params =
                mapOf(
                    PARAM_LATITUDE to latitude.toString(),
                    PARAM_LONGITUDE to longitude.toString(),
                    PARAM_LIMIT to limit.toString(),
                    PARAM_APP_ID to apiKey,
                )

            val coordinateQuery = "$latitude,$longitude"

            client
                .get<List<GeocodingLocationDto>>(
                    endpoint = REVERSE_GEOCODING_ENDPOINT,
                    params = params,
                ).map { dtos ->
                    mapper.mapToLocationSearchResult(dtos, coordinateQuery)
                }
        }

    override suspend fun getStoredLocation(): Location? =
        withContext(ioDispatcher) {
            storage.getNullable<Location>(key = STORED_LOCATION_KEY).first()
        }

    override suspend fun setLocation(location: Location) =
        withContext(ioDispatcher) {
            storage.put(key = STORED_LOCATION_KEY, location)
        }
}

package com.harry.location.domain.repository

import com.harry.location.domain.model.Location
import com.harry.location.domain.model.LocationSearchResult

/**
 * Repository for location search and geocoding operations
 */
interface LocationRepository {
    /**
     * Search for locations by name or query string
     *
     * @param query The search query (city name, coordinates, etc.)
     * @param limit Maximum number of results to return (default: 5)
     * @return Result containing a list of matching locations
     */
    suspend fun searchLocations(query: String, limit: Int = 5): Result<LocationSearchResult>

    /**
     * Get location information by coordinates (reverse geocoding)
     *
     * @param latitude Latitude coordinate
     * @param longitude Longitude coordinate
     * @param limit Maximum number of results to return (default: 1)
     * @return Result containing location information for the coordinates
     */
    suspend fun getLocationByCoordinates(
        latitude: Double,
        longitude: Double,
        limit: Int = 1,
    ): Result<LocationSearchResult>

    suspend fun getStoredLocation(): Location?

    suspend fun setLocation(location: Location)
}

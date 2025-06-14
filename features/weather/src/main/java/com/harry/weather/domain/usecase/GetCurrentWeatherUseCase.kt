package com.harry.weather.domain.usecase

import com.harry.weather.data.WeatherRepository
import com.harry.weather.domain.model.WeatherData

/**
 * Use case for getting current weather data for a specific location.
 *
 * Currently a thin wrapper around the repository, but provides flexibility
 * for adding business logic like caching, validation, or data combination.
 */
class GetCurrentWeatherUseCase(
    private val repository: WeatherRepository,
) {
    /**
     * Get current weather and forecasts for the specified location
     *
     * @param latitude Latitude of the location
     * @param longitude Longitude of the location
     * @param units Temperature units (standard, metric, imperial)
     * @param language Language for weather descriptions
     * @return Result containing weather data or error
     */
    suspend operator fun invoke(
        latitude: Double,
        longitude: Double,
        units: String = "metric",
        language: String = "en",
    ): Result<WeatherData> =
        repository.getCurrentWeatherAndForecasts(
            latitude = latitude,
            longitude = longitude,
            excludeParts = emptyList(), // Get all data for now
            units = units,
            language = language,
        )
}

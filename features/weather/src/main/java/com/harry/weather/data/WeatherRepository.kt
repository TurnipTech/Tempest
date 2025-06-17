package com.harry.weather.data

import com.harry.weather.domain.WeatherConstants
import com.harry.weather.domain.model.WeatherData

interface WeatherRepository {
    /**
     * Get current weather and forecasts for the specified location
     * @param latitude Latitude of the location
     * @param longitude Longitude of the location
     * @param excludeParts Parts to exclude from response (minutely, hourly, daily, alerts)
     * @param units Temperature units (standard, metric, imperial)
     * @param language Language for weather descriptions
     */
    suspend fun getCurrentWeatherAndForecasts(
        latitude: Double,
        longitude: Double,
        excludeParts: List<String> = emptyList(),
        units: String = WeatherConstants.METRIC_UNIT,
        language: String = WeatherConstants.DEFAULT_LANGUAGE,
    ): Result<WeatherData>
}

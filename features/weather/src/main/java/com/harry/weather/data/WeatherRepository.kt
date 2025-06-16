package com.harry.weather.data

import com.harry.weather.domain.model.DailySummary
import com.harry.weather.domain.model.HistoricalWeather
import com.harry.weather.domain.model.WeatherData
import com.harry.weather.domain.model.WeatherOverview

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
        units: String = "metric",//todo - make const as well as the other hardcoded ones
        language: String = "en",
    ): Result<WeatherData>

    /**
     * Get historical weather data for a specific timestamp
     * @param latitude Latitude of the location
     * @param longitude Longitude of the location
     * @param timestamp Unix timestamp for the date
     * @param units Temperature units (standard, metric, imperial)
     * @param language Language for weather descriptions
     */
    suspend fun getHistoricalWeather(
        latitude: Double,
        longitude: Double,
        timestamp: Long,
        units: String = "metric",
        language: String = "en",
    ): Result<HistoricalWeather>

    /**
     * Get daily weather summary for a specific date
     * @param latitude Latitude of the location
     * @param longitude Longitude of the location
     * @param date Date in YYYY-MM-DD format
     * @param units Temperature units (standard, metric, imperial)
     * @param timezone Timezone offset in ±XX:XX format
     */
    suspend fun getDailyWeatherSummary(
        latitude: Double,
        longitude: Double,
        date: String,
        units: String = "metric",
        timezone: String? = null,
    ): Result<DailySummary>

    /**
     * Get AI-generated weather overview
     * @param latitude Latitude of the location
     * @param longitude Longitude of the location
     * @param date Date in YYYY-MM-DD format (optional, defaults to today)
     * @param units Temperature units (standard, metric, imperial)
     */
    suspend fun getWeatherOverview(
        latitude: Double,
        longitude: Double,
        date: String? = null,
        units: String = "metric",
    ): Result<WeatherOverview>
}

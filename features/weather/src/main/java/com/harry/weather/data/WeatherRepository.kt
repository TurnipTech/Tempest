package com.harry.weather.data

import com.harry.network.client.HttpClient
import com.harry.network.client.get
import com.harry.weather.BuildConfig
import com.harry.weather.data.dto.DailySummaryResponseDto
import com.harry.weather.data.dto.HistoricalWeatherResponseDto
import com.harry.weather.data.dto.WeatherOverviewResponseDto
import com.harry.weather.data.dto.WeatherResponseDto
import com.harry.weather.data.mapper.WeatherMapper

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
        units: String = "metric",
        language: String = "en"
    ): Result<com.harry.weather.domain.model.WeatherData>

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
        language: String = "en"
    ): Result<com.harry.weather.domain.model.HistoricalWeather>

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
        timezone: String? = null
    ): Result<com.harry.weather.domain.model.DailySummary>

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
        units: String = "metric"
    ): Result<com.harry.weather.domain.model.WeatherOverview>
}


class WeatherRepositoryImpl(
    private val client: HttpClient
): WeatherRepository {

    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/data/3.0/onecall"
        private const val API_KEY = BuildConfig.OPEN_WEATHER_API_KEY // todo - set up to be provided in CI build job
    }

    override suspend fun getCurrentWeatherAndForecasts(
        latitude: Double,
        longitude: Double,
        excludeParts: List<String>,
        units: String,
        language: String
    ): Result<com.harry.weather.domain.model.WeatherData> {
        val params = buildMap {
            put("lat", latitude.toString())
            put("lon", longitude.toString())
            put("appid", API_KEY)
            put("units", units)
            put("lang", language)
            if (excludeParts.isNotEmpty()) {
                put("exclude", excludeParts.joinToString(","))
            }
        }

        return client.get<WeatherResponseDto>(
            endpoint = BASE_URL,
            params = params
        ).map { dto ->
            WeatherMapper.mapToWeatherData(dto)
        }
    }

    override suspend fun getHistoricalWeather(
        latitude: Double,
        longitude: Double,
        timestamp: Long,
        units: String,
        language: String
    ): Result<com.harry.weather.domain.model.HistoricalWeather> {
        val params = mapOf(
            "lat" to latitude.toString(),
            "lon" to longitude.toString(),
            "dt" to timestamp.toString(),
            "appid" to API_KEY,
            "units" to units,
            "lang" to language
        )

        return client.get<HistoricalWeatherResponseDto>(
            endpoint = "$BASE_URL/timemachine",
            params = params
        ).map { dto ->
            WeatherMapper.mapToHistoricalWeather(dto)
        }
    }

    override suspend fun getDailyWeatherSummary(
        latitude: Double,
        longitude: Double,
        date: String,
        units: String,
        timezone: String?
    ): Result<com.harry.weather.domain.model.DailySummary> {
        val params = buildMap {
            put("lat", latitude.toString())
            put("lon", longitude.toString())
            put("date", date)
            put("appid", API_KEY)
            put("units", units)
            timezone?.let { put("tz", it) }
        }

        return client.get<DailySummaryResponseDto>(
            endpoint = "$BASE_URL/day_summary",
            params = params
        ).map { dto ->
            WeatherMapper.mapToDailySummary(dto)
        }
    }

    override suspend fun getWeatherOverview(
        latitude: Double,
        longitude: Double,
        date: String?,
        units: String
    ): Result<com.harry.weather.domain.model.WeatherOverview> {
        val params = buildMap {
            put("lat", latitude.toString())
            put("lon", longitude.toString())
            put("appid", API_KEY)
            put("units", units)
            date?.let { put("date", it) }
        }

        return client.get<WeatherOverviewResponseDto>(
            endpoint = "$BASE_URL/overview",
            params = params
        ).map { dto ->
            WeatherMapper.mapToWeatherOverview(dto)
        }
    }
}

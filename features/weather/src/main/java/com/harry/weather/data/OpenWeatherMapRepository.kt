package com.harry.weather.data

import com.harry.network.client.HttpClient
import com.harry.network.client.get
import com.harry.weather.data.dto.DailySummaryResponseDto
import com.harry.weather.data.dto.HistoricalWeatherResponseDto
import com.harry.weather.data.dto.WeatherOverviewResponseDto
import com.harry.weather.data.dto.WeatherResponseDto
import com.harry.weather.data.mapper.WeatherMapper
import com.harry.weather.domain.model.DailySummary
import com.harry.weather.domain.model.HistoricalWeather
import com.harry.weather.domain.model.WeatherData
import com.harry.weather.domain.model.WeatherOverview

internal class OpenWeatherMapRepository(
    private val client: HttpClient,
    private val mapper: WeatherMapper,
    private val apiKey: String,
) : WeatherRepository {
    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/data/3.0/onecall"

        // API parameter names
        private const val PARAM_LATITUDE = "lat"
        private const val PARAM_LONGITUDE = "lon"
        private const val PARAM_APP_ID = "appid"
        private const val PARAM_UNITS = "units"
        private const val PARAM_LANGUAGE = "lang"
        private const val PARAM_EXCLUDE = "exclude"
        private const val PARAM_TIMESTAMP = "dt"
        private const val PARAM_DATE = "date"
        private const val PARAM_TIMEZONE = "tz"

        // API endpoint paths
        private const val ENDPOINT_TIMEMACHINE = "/timemachine"
        private const val ENDPOINT_DAY_SUMMARY = "/day_summary"
        private const val ENDPOINT_OVERVIEW = "/overview"
    }

    override suspend fun getCurrentWeatherAndForecasts(
        latitude: Double,
        longitude: Double,
        excludeParts: List<String>,
        units: String,
        language: String,
    ): Result<WeatherData> {
        val params =
            buildMap {
                put(PARAM_LATITUDE, latitude.toString())
                put(PARAM_LONGITUDE, longitude.toString())
                put(PARAM_APP_ID, apiKey)
                put(PARAM_UNITS, units)
                put(PARAM_LANGUAGE, language)
                if (excludeParts.isNotEmpty()) {
                    put(PARAM_EXCLUDE, excludeParts.joinToString(","))
                }
            }

        return client
            .get<WeatherResponseDto>(
                endpoint = BASE_URL,
                params = params,
            ).map { dto ->
                mapper.mapToWeatherData(dto)
            }
    }

    override suspend fun getHistoricalWeather(
        latitude: Double,
        longitude: Double,
        timestamp: Long,
        units: String,
        language: String,
    ): Result<HistoricalWeather> {
        val params =
            mapOf(
                PARAM_LATITUDE to latitude.toString(),
                PARAM_LONGITUDE to longitude.toString(),
                PARAM_TIMESTAMP to timestamp.toString(),
                PARAM_APP_ID to apiKey,
                PARAM_UNITS to units,
                PARAM_LANGUAGE to language,
            )

        return client
            .get<HistoricalWeatherResponseDto>(
                endpoint = "$BASE_URL$ENDPOINT_TIMEMACHINE",
                params = params,
            ).map { dto ->
                mapper.mapToHistoricalWeather(dto)
            }
    }

    override suspend fun getDailyWeatherSummary(
        latitude: Double,
        longitude: Double,
        date: String,
        units: String,
        timezone: String?,
    ): Result<DailySummary> {
        val params =
            buildMap {
                put(PARAM_LATITUDE, latitude.toString())
                put(PARAM_LONGITUDE, longitude.toString())
                put(PARAM_DATE, date)
                put(PARAM_APP_ID, apiKey)
                put(PARAM_UNITS, units)
                timezone?.let { put(PARAM_TIMEZONE, it) }
            }

        return client
            .get<DailySummaryResponseDto>(
                endpoint = "$BASE_URL$ENDPOINT_DAY_SUMMARY",
                params = params,
            ).map { dto ->
                mapper.mapToDailySummary(dto)
            }
    }

    override suspend fun getWeatherOverview(
        latitude: Double,
        longitude: Double,
        date: String?,
        units: String,
    ): Result<WeatherOverview> {
        val params =
            buildMap {
                put(PARAM_LATITUDE, latitude.toString())
                put(PARAM_LONGITUDE, longitude.toString())
                put(PARAM_APP_ID, apiKey)
                put(PARAM_UNITS, units)
                date?.let { put(PARAM_DATE, it) }
            }

        return client
            .get<WeatherOverviewResponseDto>(
                endpoint = "$BASE_URL$ENDPOINT_OVERVIEW",
                params = params,
            ).map { dto ->
                mapper.mapToWeatherOverview(dto)
            }
    }
}

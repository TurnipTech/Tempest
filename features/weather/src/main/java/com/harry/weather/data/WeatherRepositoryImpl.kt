package com.harry.weather.data

import com.harry.network.client.HttpClient
import com.harry.network.client.get
import com.harry.weather.data.dto.WeatherResponseDto
import com.harry.weather.data.mapper.WeatherMapper
import com.harry.weather.domain.model.WeatherData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

private const val BASE_URL = "https://api.openweathermap.org/data/3.0/onecall"

// API parameter names
private const val PARAM_LATITUDE = "lat"
private const val PARAM_LONGITUDE = "lon"
private const val PARAM_APP_ID = "appid"
private const val PARAM_UNITS = "units"
private const val PARAM_LANGUAGE = "lang"
private const val PARAM_EXCLUDE = "exclude"

internal class WeatherRepositoryImpl(
    private val client: HttpClient,
    private val mapper: WeatherMapper,
    private val apiKey: String,
    private val ioDispatcher: CoroutineDispatcher,
) : WeatherRepository {
    override suspend fun getCurrentWeatherAndForecasts(
        latitude: Double,
        longitude: Double,
        excludeParts: List<String>,
        units: String,
        language: String,
    ): Result<WeatherData> =
        withContext(ioDispatcher) {
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

            client
                .get<WeatherResponseDto>(
                    endpoint = BASE_URL,
                    params = params,
                ).map { dto ->
                    mapper.mapToWeatherData(dto)
                }
        }
}

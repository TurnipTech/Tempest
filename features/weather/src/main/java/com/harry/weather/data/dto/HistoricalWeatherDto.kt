package com.harry.weather.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HistoricalWeatherResponseDto(
    @SerialName("lat") val latitude: Double,
    @SerialName("lon") val longitude: Double,
    @SerialName("timezone") val timezone: String,
    @SerialName("timezone_offset") val timezoneOffset: Int,
    @SerialName("data") val data: List<HistoricalWeatherDto>,
)

@Serializable
data class HistoricalWeatherDto(
    @SerialName("dt") val dateTime: Long,
    @SerialName("sunrise") val sunrise: Long? = null,
    @SerialName("sunset") val sunset: Long? = null,
    @SerialName("temp") val temperature: Double,
    @SerialName("feels_like") val feelsLike: Double,
    @SerialName("pressure") val pressure: Int,
    @SerialName("humidity") val humidity: Int,
    @SerialName("dew_point") val dewPoint: Double,
    @SerialName("clouds") val cloudiness: Int,
    @SerialName("uvi") val uvIndex: Double,
    @SerialName("visibility") val visibility: Int,
    @SerialName("wind_speed") val windSpeed: Double,
    @SerialName("wind_deg") val windDirection: Int,
    @SerialName("wind_gust") val windGust: Double? = null,
    @SerialName("weather") val weather: List<WeatherConditionDto>,
    @SerialName("rain") val rain: PrecipitationDto? = null,
    @SerialName("snow") val snow: PrecipitationDto? = null,
)

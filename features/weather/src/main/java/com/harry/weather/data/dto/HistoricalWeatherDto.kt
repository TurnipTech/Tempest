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
    @SerialName("pressure") val pressure: Int? = null,
    @SerialName("humidity") val humidity: Int? = null,
    @SerialName("dew_point") val dewPoint: Double? = null,
    @SerialName("clouds") val cloudiness: Int? = null,
    @SerialName("uvi") val uvIndex: Double? = null,
    @SerialName("visibility") val visibility: Int? = null,
    @SerialName("wind_speed") val windSpeed: Double? = null,
    @SerialName("wind_deg") val windDirection: Int? = null,
    @SerialName("wind_gust") val windGust: Double? = null,
    @SerialName("weather") val weather: List<WeatherConditionDto>,
    @SerialName("rain") val rain: PrecipitationDto? = null,
    @SerialName("snow") val snow: PrecipitationDto? = null,
)

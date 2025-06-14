package com.harry.weather.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherOverviewResponseDto(
    @SerialName("lat") val latitude: Double,
    @SerialName("lon") val longitude: Double,
    @SerialName("tz") val timezone: String,
    @SerialName("date") val date: String,
    @SerialName("units") val units: String,
    @SerialName("weather_overview") val weatherOverview: String,
)

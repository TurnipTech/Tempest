package com.harry.weather.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DailySummaryResponseDto(
    @SerialName("lat") val latitude: Double,
    @SerialName("lon") val longitude: Double,
    @SerialName("tz") val timezone: String,
    @SerialName("date") val date: String,
    @SerialName("units") val units: String,
    @SerialName("cloud_cover") val cloudCover: CloudCoverDto,
    @SerialName("humidity") val humidity: HumidityDto,
    @SerialName("precipitation") val precipitation: PrecipitationSummaryDto,
    @SerialName("temperature") val temperature: TemperatureSummaryDto,
    @SerialName("pressure") val pressure: PressureDto,
    @SerialName("wind") val wind: WindSummaryDto,
)

@Serializable
data class CloudCoverDto(
    @SerialName("afternoon") val afternoon: Int,
)

@Serializable
data class HumidityDto(
    @SerialName("afternoon") val afternoon: Int,
)

@Serializable
data class PrecipitationSummaryDto(
    @SerialName("total") val total: Double,
)

@Serializable
data class TemperatureSummaryDto(
    @SerialName("min") val min: Double,
    @SerialName("max") val max: Double,
    @SerialName("afternoon") val afternoon: Double,
    @SerialName("night") val night: Double,
    @SerialName("evening") val evening: Double,
    @SerialName("morning") val morning: Double,
)

@Serializable
data class PressureDto(
    @SerialName("afternoon") val afternoon: Int,
)

@Serializable
data class WindSummaryDto(
    @SerialName("max") val max: MaxWindDto,
)

@Serializable
data class MaxWindDto(
    @SerialName("speed") val speed: Double,
    @SerialName("direction") val direction: Int,
)

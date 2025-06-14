package com.harry.weather.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponseDto(
    @SerialName("lat") val latitude: Double,
    @SerialName("lon") val longitude: Double,
    @SerialName("timezone") val timezone: String,
    @SerialName("timezone_offset") val timezoneOffset: Int,
    @SerialName("current") val current: CurrentWeatherDto? = null,
    @SerialName("minutely") val minutely: List<MinutelyDto>? = null,
    @SerialName("hourly") val hourly: List<HourlyDto>? = null,
    @SerialName("daily") val daily: List<DailyDto>? = null,
    @SerialName("alerts") val alerts: List<AlertDto>? = null
)

@Serializable
data class CurrentWeatherDto(
    @SerialName("dt") val dateTime: Long,
    @SerialName("sunrise") val sunrise: Long? = null,
    @SerialName("sunset") val sunset: Long? = null,
    @SerialName("temp") val temperature: Double,
    @SerialName("feels_like") val feelsLike: Double,
    @SerialName("pressure") val pressure: Int,
    @SerialName("humidity") val humidity: Int,
    @SerialName("dew_point") val dewPoint: Double,
    @SerialName("uvi") val uvIndex: Double,
    @SerialName("clouds") val cloudiness: Int,
    @SerialName("visibility") val visibility: Int,
    @SerialName("wind_speed") val windSpeed: Double,
    @SerialName("wind_deg") val windDirection: Int,
    @SerialName("wind_gust") val windGust: Double? = null,
    @SerialName("weather") val weather: List<WeatherConditionDto>,
    @SerialName("rain") val rain: PrecipitationDto? = null,
    @SerialName("snow") val snow: PrecipitationDto? = null
)

@Serializable
data class MinutelyDto(
    @SerialName("dt") val dateTime: Long,
    @SerialName("precipitation") val precipitation: Double
)

@Serializable
data class HourlyDto(
    @SerialName("dt") val dateTime: Long,
    @SerialName("temp") val temperature: Double,
    @SerialName("feels_like") val feelsLike: Double,
    @SerialName("pressure") val pressure: Int,
    @SerialName("humidity") val humidity: Int,
    @SerialName("dew_point") val dewPoint: Double,
    @SerialName("uvi") val uvIndex: Double,
    @SerialName("clouds") val cloudiness: Int,
    @SerialName("visibility") val visibility: Int,
    @SerialName("wind_speed") val windSpeed: Double,
    @SerialName("wind_deg") val windDirection: Int,
    @SerialName("wind_gust") val windGust: Double? = null,
    @SerialName("pop") val probabilityOfPrecipitation: Double,
    @SerialName("weather") val weather: List<WeatherConditionDto>,
    @SerialName("rain") val rain: PrecipitationDto? = null,
    @SerialName("snow") val snow: PrecipitationDto? = null
)

@Serializable
data class DailyDto(
    @SerialName("dt") val dateTime: Long,
    @SerialName("sunrise") val sunrise: Long? = null,
    @SerialName("sunset") val sunset: Long? = null,
    @SerialName("moonrise") val moonrise: Long? = null,
    @SerialName("moonset") val moonset: Long? = null,
    @SerialName("moon_phase") val moonPhase: Double,
    @SerialName("summary") val summary: String? = null,
    @SerialName("temp") val temperature: DailyTemperatureDto,
    @SerialName("feels_like") val feelsLike: DailyFeelsLikeDto,
    @SerialName("pressure") val pressure: Int,
    @SerialName("humidity") val humidity: Int,
    @SerialName("dew_point") val dewPoint: Double,
    @SerialName("wind_speed") val windSpeed: Double,
    @SerialName("wind_deg") val windDirection: Int,
    @SerialName("wind_gust") val windGust: Double? = null,
    @SerialName("weather") val weather: List<WeatherConditionDto>,
    @SerialName("clouds") val cloudiness: Int,
    @SerialName("pop") val probabilityOfPrecipitation: Double,
    @SerialName("rain") val rain: Double? = null,
    @SerialName("snow") val snow: Double? = null,
    @SerialName("uvi") val uvIndex: Double
)

@Serializable
data class DailyTemperatureDto(
    @SerialName("day") val day: Double,
    @SerialName("min") val min: Double,
    @SerialName("max") val max: Double,
    @SerialName("night") val night: Double,
    @SerialName("eve") val evening: Double,
    @SerialName("morn") val morning: Double
)

@Serializable
data class DailyFeelsLikeDto(
    @SerialName("day") val day: Double,
    @SerialName("night") val night: Double,
    @SerialName("eve") val evening: Double,
    @SerialName("morn") val morning: Double
)

@Serializable
data class WeatherConditionDto(
    @SerialName("id") val id: Int,
    @SerialName("main") val main: String,
    @SerialName("description") val description: String,
    @SerialName("icon") val icon: String
)

@Serializable
data class PrecipitationDto(
    @SerialName("1h") val oneHour: Double
)

@Serializable
data class AlertDto(
    @SerialName("sender_name") val senderName: String,
    @SerialName("event") val event: String,
    @SerialName("start") val start: Long,
    @SerialName("end") val end: Long,
    @SerialName("description") val description: String,
    @SerialName("tags") val tags: List<String>
)

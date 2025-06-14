package com.harry.weather.domain.model

data class WeatherData(
    val location: Location,
    val currentWeather: CurrentWeather?,
    val hourlyForecast: List<HourlyWeather>?,
    val dailyForecast: List<DailyWeather>?,
    val alerts: List<WeatherAlert>?
)

data class Location(
    val latitude: Double,
    val longitude: Double,
    val timezone: String
)

data class CurrentWeather(
    val dateTime: Long,
    val temperature: Double,
    val feelsLike: Double,
    val humidity: Int,
    val pressure: Int,
    val windSpeed: Double,
    val windDirection: Int,
    val uvIndex: Double,
    val cloudiness: Int,
    val visibility: Int,
    val condition: WeatherCondition
)

data class HourlyWeather(
    val dateTime: Long,
    val temperature: Double,
    val feelsLike: Double,
    val humidity: Int,
    val pressure: Int,
    val windSpeed: Double,
    val uvIndex: Double,
    val probabilityOfPrecipitation: Double,
    val condition: WeatherCondition
)

data class DailyWeather(
    val dateTime: Long,
    val temperatureHigh: Double,
    val temperatureLow: Double,
    val humidity: Int,
    val pressure: Int,
    val windSpeed: Double,
    val uvIndex: Double,
    val probabilityOfPrecipitation: Double,
    val condition: WeatherCondition,
    val summary: String?
)

data class WeatherCondition(
    val id: Int,
    val main: String,
    val description: String,
    val iconCode: String
)

data class WeatherAlert(
    val senderName: String,
    val event: String,
    val startTime: Long,
    val endTime: Long,
    val description: String
)

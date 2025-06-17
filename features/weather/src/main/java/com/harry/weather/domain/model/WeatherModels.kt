package com.harry.weather.domain.model

// Note: Analysis of UI usage shows several fields are currently unused but kept for future features:
// - WeatherData.alerts: not displayed in UI
// - Location.latitude/longitude: not used in UI mapper (location name comes from external source)
// - CurrentWeather: only temperature, sunrise, sunset, condition are used; other fields unused
// - HourlyWeather: only dateTime, temperature, probabilityOfPrecipitation, condition are used
// - DailyWeather: only dateTime, temperatureHigh/Low, condition are used; summary, humidity, etc. unused
// - WeatherCondition: only description and iconCode are used; id and main are unused
// - WeatherAlert: entire class unused in UI
data class WeatherData(
    val location: Location,
    val currentWeather: CurrentWeather?,
    val hourlyForecast: List<HourlyWeather>?,
    val dailyForecast: List<DailyWeather>?,
    val alerts: List<WeatherAlert>?,
)

data class Location(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
)

data class CurrentWeather(
    val dateTime: Long,
    val sunrise: Long?,
    val sunset: Long?,
    val temperature: Double,
    val feelsLike: Double,
    val humidity: Int,
    val pressure: Int,
    val windSpeed: Double,
    val windDirection: Int,
    val uvIndex: Double,
    val cloudiness: Int,
    val visibility: Int,
    val condition: WeatherCondition,
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
    val condition: WeatherCondition,
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
    val summary: String?,
)

data class WeatherCondition(
    val id: Int,
    val main: String,
    val description: String,
    val iconCode: String,
)

data class WeatherAlert(
    val senderName: String,
    val event: String,
    val startTime: Long,
    val endTime: Long,
    val description: String,
)

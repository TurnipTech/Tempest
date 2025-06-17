package com.harry.weather.domain.model

data class WeatherData(
    val timezone: String,
    val currentWeather: CurrentWeather?,
    val hourlyForecast: List<HourlyWeather>?,
    val dailyForecast: List<DailyWeather>?,
)


data class CurrentWeather(
    val temperature: Double,
    val sunrise: Long?,
    val sunset: Long?,
    val condition: WeatherCondition,
)

data class HourlyWeather(
    val dateTime: Long,
    val temperature: Double,
    val probabilityOfPrecipitation: Double,
    val condition: WeatherCondition,
)

data class DailyWeather(
    val dateTime: Long,
    val temperatureHigh: Double,
    val temperatureLow: Double,
    val condition: WeatherCondition,
)

data class WeatherCondition(
    val description: String,
    val iconCode: String,
)

package com.harry.weather.domain.model

// Note: These models are currently unused in the UI layer and use cases.
// They are only referenced in the data layer (repositories, mappers, DTOs, tests).
// Consider removing if these features are not planned for future implementation.
data class HistoricalWeather(
    val location: Location,
    val data: List<HistoricalWeatherData>,
)

data class HistoricalWeatherData(
    val dateTime: Long,
    val temperature: Double,
    val feelsLike: Double,
    val humidity: Int,
    val pressure: Int,
    val windSpeed: Double,
    val uvIndex: Double,
    val condition: WeatherCondition,
)

data class DailySummary(
    val location: Location,
    val date: String,
    val temperatureRange: TemperatureRange,
    val humidity: Int,
    val pressure: Int,
    val maxWindSpeed: Double,
    val totalPrecipitation: Double,
    val cloudCover: Int,
)

data class TemperatureRange(
    val min: Double,
    val max: Double,
    val morning: Double,
    val afternoon: Double,
    val evening: Double,
    val night: Double,
)

data class WeatherOverview(
    val location: Location,
    val date: String,
    val overview: String,
)

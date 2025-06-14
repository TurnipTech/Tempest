package com.harry.weather.ui.mapper

import com.harry.weather.domain.model.HourlyWeather
import com.harry.weather.domain.model.WeatherData
import com.harry.weather.ui.model.HourlyWeatherUiModel
import com.harry.weather.ui.model.WeatherUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherUiMapper {
    fun mapToSuccessState(weatherData: WeatherData, units: String): WeatherUiState.Success {
        val currentWeather = weatherData.currentWeather

        return WeatherUiState.Success(
            weatherData = weatherData,
            formattedTemperature = formatTemperature(currentWeather?.temperature, units),
            formattedLocation = formatLocation(weatherData.location.latitude, weatherData.location.longitude),
            weatherDescription = formatWeatherDescription(currentWeather?.condition?.description),
            lastUpdated = formatLastUpdated(),
            todaysHourlyForecast = mapTodaysHourlyForecast(weatherData.hourlyForecast ?: emptyList()),
        )
    }

    private fun formatTemperature(temperature: Double?, units: String): String =
        if (temperature != null) {
            val unitSymbol =
                when (units) {
                    "imperial" -> "°F"
                    "standard" -> "K"
                    else -> "°C"
                }
            "${temperature.toInt()}$unitSymbol"
        } else {
            "N/A"
        }

    private fun formatLocation(latitude: Double, longitude: Double): String = "$latitude, $longitude"

    private fun formatWeatherDescription(description: String?): String =
        description?.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase() else it.toString()
        } ?: "No data available"

    private fun formatLastUpdated(): String =
        "Updated ${SimpleDateFormat(
            "HH:mm",
            Locale.getDefault(),
        ).format(Date())}"

    private fun mapTodaysHourlyForecast(hourlyForecast: List<HourlyWeather>): List<HourlyWeatherUiModel> {
        val currentTime = System.currentTimeMillis() / 1000
        val endOfDay = currentTime + (24 * 60 * 60) // Next 24 hours

        return hourlyForecast
            .filter { weather ->
                weather.dateTime >= currentTime && weather.dateTime <= endOfDay
            }.take(12) // Limit to next 12 hours for today
            .map { hourlyWeather ->
                mapToHourlyWeatherUiModel(hourlyWeather)
            }
    }

    private fun mapToHourlyWeatherUiModel(hourlyWeather: HourlyWeather): HourlyWeatherUiModel =
        HourlyWeatherUiModel(
            formattedTime = formatTime(hourlyWeather.dateTime),
            temperature = "${hourlyWeather.temperature.toInt()}°",
            iconUrl = createIconUrl(hourlyWeather.condition.iconCode),
            iconDescription = hourlyWeather.condition.description,
            precipitationProbability = "${hourlyWeather.probabilityOfPrecipitation.toInt()}%",
        )

    private fun formatTime(timestamp: Long): String {
        val date = Date(timestamp * 1000) // Convert seconds to milliseconds
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        return formatter.format(date)
    }

    private fun createIconUrl(iconCode: String): String = "https://openweathermap.org/img/wn/$iconCode@2x.png"
}

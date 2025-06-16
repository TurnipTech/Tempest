package com.harry.weather.ui.model

import com.harry.weather.domain.model.TimeOfDay
import com.harry.weather.domain.model.WeatherData

sealed class WeatherUiState {
    data object Loading : WeatherUiState()

    data class Success(
        val weatherData: WeatherData,
        val formattedTemperature: String,
        val formattedLocation: String,
        val weatherDescription: String,
        val lastUpdated: String,
        val todaysHourlyForecast: List<HourlyWeatherUiModel>,
        val weeklyForecast: List<DailyWeatherUiModel>,
        val timeOfDay: TimeOfDay,
    ) : WeatherUiState()

    data class Error(
        val message: String,
        val canRetry: Boolean = true,
    ) : WeatherUiState()
}

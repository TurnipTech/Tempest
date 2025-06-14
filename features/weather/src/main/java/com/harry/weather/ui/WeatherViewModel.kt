package com.harry.weather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harry.weather.domain.model.HourlyWeather
import com.harry.weather.domain.model.WeatherData
import com.harry.weather.domain.usecase.GetCurrentWeatherUseCase
import com.harry.weather.ui.model.HourlyWeatherUiModel
import com.harry.weather.ui.model.WeatherUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherViewModel(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    fun loadWeather(
        latitude: Double,
        longitude: Double,
        units: String = "metric",
        language: String = "en",
    ) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading

            getCurrentWeatherUseCase(
                latitude = latitude,
                longitude = longitude,
                units = units,
                language = language,
            ).fold(
                onSuccess = { weatherData ->
                    _uiState.value = transformToUiState(weatherData, units)
                },
                onFailure = { error ->
                    _uiState.value =
                        WeatherUiState.Error(
                            message = error.message ?: "Failed to load weather data",
                            canRetry = true,
                        )
                },
            )
        }
    }

    private fun transformToUiState(weatherData: WeatherData, units: String): WeatherUiState.Success {
        val currentWeather = weatherData.currentWeather

        return WeatherUiState.Success(
            weatherData = weatherData,
            formattedTemperature =
                if (currentWeather != null) {
                    val unitSymbol =
                        when (units) {
                            "imperial" -> "°F"
                            "standard" -> "K"
                            else -> "°C"
                        }
                    "${currentWeather.temperature.toInt()}$unitSymbol"
                } else {
                    "N/A"
                },
            formattedLocation = "${weatherData.location.latitude}, ${weatherData.location.longitude}",
            weatherDescription =
                currentWeather?.condition?.description?.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase() else it.toString()
                } ?: "No data available",
            lastUpdated = "Updated ${SimpleDateFormat(
                "HH:mm",
                Locale.getDefault(),
            ).format(Date())}",
            todaysHourlyForecast = createTodaysHourlyForecast(weatherData.hourlyForecast ?: emptyList()),
        )
    }

    private fun createTodaysHourlyForecast(hourlyForecast: List<HourlyWeather>): List<HourlyWeatherUiModel> {
        val currentTime = System.currentTimeMillis() / 1000
        val endOfDay = currentTime + (24 * 60 * 60) // Next 24 hours

        return hourlyForecast
            .filter { weather ->
                weather.dateTime >= currentTime && weather.dateTime <= endOfDay
            }.take(12) // Limit to next 12 hours for today
            .map { hourlyWeather ->
                HourlyWeatherUiModel(
                    formattedTime = formatTime(hourlyWeather.dateTime),
                    temperature = "${hourlyWeather.temperature.toInt()}°",
                    iconUrl = createIconUrl(hourlyWeather.condition.iconCode),
                    iconDescription = hourlyWeather.condition.description,
                    precipitationProbability = "${hourlyWeather.probabilityOfPrecipitation.toInt()}%",
                )
            }
    }

    private fun formatTime(timestamp: Long): String {
        val date = Date(timestamp * 1000) // Convert seconds to milliseconds
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        return formatter.format(date)
    }

    private fun createIconUrl(iconCode: String): String = "https://openweathermap.org/img/wn/$iconCode@2x.png"
}

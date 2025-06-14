package com.harry.weather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harry.weather.domain.model.WeatherData
import com.harry.weather.domain.usecase.GetCurrentWeatherUseCase
import com.harry.weather.ui.model.WeatherUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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
            lastUpdated = "Updated ${java.text.SimpleDateFormat(
                "HH:mm",
                java.util.Locale.getDefault(),
            ).format(java.util.Date())}",
        )
    }
}

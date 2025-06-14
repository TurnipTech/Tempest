package com.harry.weather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harry.weather.domain.usecase.GetCurrentWeatherUseCase
import com.harry.weather.ui.mapper.WeatherUiMapper
import com.harry.weather.ui.model.WeatherUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val weatherUiMapper: WeatherUiMapper,
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
                    _uiState.value = weatherUiMapper.mapToSuccessState(weatherData, units)
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
}

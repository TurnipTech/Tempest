package com.harry.weather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harry.location.domain.usecase.GetStoredLocationUseCase
import com.harry.weather.domain.usecase.GetCurrentWeatherUseCase
import com.harry.weather.ui.mapper.WeatherUiMapper
import com.harry.weather.ui.model.WeatherUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val location: com.harry.location.domain.model.Location?,
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val weatherUiMapper: WeatherUiMapper,
    private val getStoredLocationUseCase: GetStoredLocationUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    init {
        loadWeather()
    }

    fun retry() {
        loadWeather()
    }

    private fun loadWeather(units: String = "metric", language: String = "en") {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading

            val currentLocation = location ?: getStoredLocationUseCase()

            if (currentLocation == null) {
                _uiState.value =
                    WeatherUiState.Error(
                        message = "No location available",
                        canRetry = false,
                    )
                return@launch
            }

            getCurrentWeatherUseCase(
                latitude = currentLocation.latitude,
                longitude = currentLocation.longitude,
                units = units,
                language = language,
            ).onSuccess { weatherData ->
                _uiState.value =
                    weatherUiMapper.mapToSuccessState(
                        weatherData = weatherData,
                        units = units,
                        locationName = currentLocation.name,
                    )
            }.onFailure { error ->
                _uiState.value =
                    WeatherUiState.Error(
                        message = error.message ?: "Failed to load weather data",
                        canRetry = true,
                    )
            }
        }
    }
}

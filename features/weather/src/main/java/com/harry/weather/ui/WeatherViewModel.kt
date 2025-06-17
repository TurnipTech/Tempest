package com.harry.weather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harry.location.domain.model.Location
import com.harry.location.domain.usecase.GetStoredLocationUseCase
import com.harry.utils.ResourceProvider
import com.harry.weather.R
import com.harry.weather.domain.WeatherConstants
import com.harry.weather.domain.usecase.GetCurrentWeatherUseCase
import com.harry.weather.ui.mapper.WeatherUiMapper
import com.harry.weather.ui.model.WeatherUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val location: Location? = null,
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val weatherUiMapper: WeatherUiMapper,
    private val getStoredLocationUseCase: GetStoredLocationUseCase,
    private val resourceProvider: ResourceProvider,
) : ViewModel() {
    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    init {
        loadWeather()
    }

    fun retry() {
        loadWeather()
    }

    private fun loadWeather() {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading

            val currentLocation = location ?: getStoredLocationUseCase()

            if (currentLocation == null) {
                _uiState.value =
                    WeatherUiState.Error(
                        message = resourceProvider.getString(R.string.error_no_location_available),
                        canRetry = false,
                    )
                return@launch
            }

            getCurrentWeatherUseCase(
                latitude = currentLocation.latitude,
                longitude = currentLocation.longitude,
            ).onSuccess { weatherData ->
                _uiState.value =
                    weatherUiMapper.mapToSuccessState(
                        weatherData = weatherData,
                        units = WeatherConstants.METRIC_UNIT,
                        locationName = currentLocation.name,
                    )
            }.onFailure { error ->
                _uiState.value =
                    WeatherUiState.Error(
                        message = error.message ?: resourceProvider.getString(R.string.error_failed_to_load_weather),
                        canRetry = true,
                    )
            }
        }
    }
}

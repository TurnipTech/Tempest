package com.harry.weather.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.harry.weather.domain.model.WeatherOverview
import com.harry.weather.ui.components.CurrentWeather
import com.harry.weather.ui.model.WeatherUiState
import org.koin.androidx.compose.koinViewModel

@Composable
fun WeatherScreen(viewModel: WeatherViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadWeather(latitude = 40.7128, longitude = -74.0060)
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (state) {
            is WeatherUiState.Loading -> {
                Text("Loading")
            }

            is WeatherUiState.Error -> {
                Text((state as? WeatherUiState.Error)?.message ?: "")
            }

            is WeatherUiState.Success -> {
                val currentState = (state as WeatherUiState.Success)
                CurrentWeather(
                    currentState.weatherDescription,
                    currentState.formattedLocation,
                    currentState.formattedTemperature
                )
            }
        }
    }
}

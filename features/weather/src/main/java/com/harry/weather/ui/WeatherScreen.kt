package com.harry.weather.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.harry.weather.ui.components.WeatherErrorScreen
import com.harry.weather.ui.components.WeatherLoadingScreen
import com.harry.weather.ui.components.WeatherSuccessScreen
import com.harry.weather.ui.model.WeatherUiState
import org.koin.androidx.compose.koinViewModel

@Composable
fun WeatherScreen(viewModel: WeatherViewModel = koinViewModel(), onNavigateToSearch: () -> Unit = {}) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when (state) {
        is WeatherUiState.Loading -> {
            WeatherLoadingScreen()
        }

        is WeatherUiState.Error -> {
            WeatherErrorScreen(
                message = (state as WeatherUiState.Error).message,
                canRetry = (state as WeatherUiState.Error).canRetry,
                onRetry = { viewModel.retry() },
            )
        }

        is WeatherUiState.Success -> {
            WeatherSuccessScreen(
                weatherDescription = (state as WeatherUiState.Success).weatherDescription,
                formattedLocation = (state as WeatherUiState.Success).formattedLocation,
                formattedTemperature = (state as WeatherUiState.Success).formattedTemperature,
                currentWeatherIconUrl = (state as WeatherUiState.Success).currentWeatherIconUrl,
                currentWeatherIconDescription = (state as WeatherUiState.Success).currentWeatherIconDescription,
                todaysHourlyForecast = (state as WeatherUiState.Success).todaysHourlyForecast,
                weeklyForecast = (state as WeatherUiState.Success).weeklyForecast,
                timeOfDay = (state as WeatherUiState.Success).timeOfDay,
                uvIndex = (state as WeatherUiState.Success).uvIndex,
                onLocationClick = onNavigateToSearch,
            )
        }
    }
}

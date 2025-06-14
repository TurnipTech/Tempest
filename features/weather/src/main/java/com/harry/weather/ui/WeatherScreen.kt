package com.harry.weather.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.harry.weather.domain.model.TimeOfDay
import com.harry.weather.ui.components.CurrentWeather
import com.harry.weather.ui.components.DynamicWeatherBackground
import com.harry.weather.ui.components.TodaysForecast
import com.harry.weather.ui.model.HourlyWeatherUiModel
import com.harry.weather.ui.model.WeatherUiState
import org.koin.androidx.compose.koinViewModel

@Composable
fun WeatherScreen(viewModel: WeatherViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        // todo - temporarily hardcoded until location search etc added
        viewModel.loadWeather(latitude = 53.8, longitude = 1.76)
    }

    when (state) {
        is WeatherUiState.Loading -> {
            // Use day background as fallback during loading
            DynamicWeatherBackground(
                timeOfDay = TimeOfDay.DAY,
                modifier = Modifier.fillMaxSize(),
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Loading", color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }

        is WeatherUiState.Error -> {
            // Use day background as fallback during error
            DynamicWeatherBackground(
                timeOfDay = TimeOfDay.DAY,
                modifier = Modifier.fillMaxSize(),
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = (state as? WeatherUiState.Error)?.message ?: "",
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }

        is WeatherUiState.Success -> {
            val currentState = (state as WeatherUiState.Success)

            DynamicWeatherBackground(
                timeOfDay = currentState.timeOfDay,
                modifier = Modifier.fillMaxSize(),
            ) {
                WeatherContent(
                    weatherDescription = currentState.weatherDescription,
                    formattedLocation = currentState.formattedLocation,
                    formattedTemperature = currentState.formattedTemperature,
                    todaysHourlyForecast = currentState.todaysHourlyForecast,
                )
            }
        }
    }
}

@Composable
private fun WeatherContent(
    weatherDescription: String,
    formattedLocation: String,
    formattedTemperature: String,
    todaysHourlyForecast: List<HourlyWeatherUiModel>,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CurrentWeather(
            weatherDescription,
            formattedLocation,
            formattedTemperature,
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (todaysHourlyForecast.isNotEmpty()) {
            TodaysForecast(
                hourlyForecast = todaysHourlyForecast,
            )
        }
    }
}

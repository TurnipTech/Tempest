package com.harry.weather.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.harry.weather.R
import com.harry.weather.domain.model.TimeOfDay
import com.harry.weather.ui.components.CurrentWeather
import com.harry.weather.ui.components.DynamicWeatherBackground
import com.harry.weather.ui.components.TodaysForecast
import com.harry.weather.ui.components.WeatherLoadingSkeleton
import com.harry.weather.ui.components.WeeklyForecast
import com.harry.weather.ui.model.DailyWeatherUiModel
import com.harry.weather.ui.model.HourlyWeatherUiModel
import com.harry.weather.ui.model.WeatherUiState
import org.koin.androidx.compose.koinViewModel

@Composable
fun WeatherScreen(viewModel: WeatherViewModel = koinViewModel(), onNavigateToSearch: () -> Unit = {}) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when (state) {
        is WeatherUiState.Loading -> {
            DynamicWeatherBackground(
                timeOfDay = TimeOfDay.DAY,
                modifier = Modifier.fillMaxSize(),
            ) {
                WeatherLoadingSkeleton()
            }
        }

        is WeatherUiState.Error -> {
            // Use day background as fallback during error
            DynamicWeatherBackground(
                timeOfDay = TimeOfDay.DAY,
                modifier = Modifier.fillMaxSize(),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(32.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.unable_to_load_weather),
                            color = Color.White.copy(alpha = 0.9f),
                            style =
                                MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 0.3.sp,
                                ),
                        )
                        Text(
                            text = (state as? WeatherUiState.Error)?.message ?: "",
                            color = Color.White.copy(alpha = 0.7f),
                            style =
                                MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    letterSpacing = 0.2.sp,
                                ),
                            textAlign = TextAlign.Center,
                        )
                        if ((state as? WeatherUiState.Error)?.canRetry == true) {
                            Button(
                                onClick = { viewModel.retry() },
                                modifier = Modifier.sizeIn(minWidth = 64.dp, minHeight = 40.dp),
                                colors =
                                    ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    ),
                                elevation =
                                    ButtonDefaults.buttonElevation(
                                        defaultElevation = 4.dp,
                                        pressedElevation = 8.dp,
                                    ),
                            ) {
                                Text(
                                    text = stringResource(R.string.retry),
                                    style = MaterialTheme.typography.labelLarge,
                                )
                            }
                        }
                    }
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
                    currentWeatherIconUrl = currentState.currentWeatherIconUrl,
                    currentWeatherIconDescription = currentState.currentWeatherIconDescription,
                    todaysHourlyForecast = currentState.todaysHourlyForecast,
                    weeklyForecast = currentState.weeklyForecast,
                    onLocationClick = onNavigateToSearch,
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
    currentWeatherIconUrl: String,
    currentWeatherIconDescription: String,
    todaysHourlyForecast: List<HourlyWeatherUiModel>,
    weeklyForecast: List<DailyWeatherUiModel>,
    onLocationClick: () -> Unit = {},
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        CurrentWeather(
            description = weatherDescription,
            locationName = formattedLocation,
            currentTemp = formattedTemperature,
            iconUrl = currentWeatherIconUrl,
            iconDescription = currentWeatherIconDescription,
            onLocationClick = onLocationClick,
        )

        Spacer(modifier = Modifier.height(64.dp))

        if (todaysHourlyForecast.isNotEmpty()) {
            TodaysForecast(
                hourlyForecast = todaysHourlyForecast,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (weeklyForecast.isNotEmpty()) {
            WeeklyForecast(
                weeklyForecast = weeklyForecast,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

package com.harry.weather.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.harry.design.TempestTheme
import com.harry.weather.domain.model.TimeOfDay
import com.harry.weather.ui.model.DailyWeatherUiModel
import com.harry.weather.ui.model.HourlyWeatherUiModel
import com.harry.weather.ui.model.UvUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherSuccessScreen(
    weatherDescription: String,
    formattedLocation: String,
    formattedTemperature: String,
    currentWeatherIconUrl: String,
    currentWeatherIconDescription: String,
    todaysHourlyForecast: List<HourlyWeatherUiModel>,
    weeklyForecast: List<DailyWeatherUiModel>,
    timeOfDay: TimeOfDay,
    uvIndex: UvUiModel?,
    onLocationClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    CurrentWeather(
                        description = weatherDescription,
                        locationName = formattedLocation,
                        currentTemp = formattedTemperature,
                        iconUrl = currentWeatherIconUrl,
                        iconDescription = currentWeatherIconDescription,
                        onLocationClick = onLocationClick,
                        scrollBehavior = scrollBehavior,
                    )
                },
                scrollBehavior = scrollBehavior,
                colors =
                    TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent,
                    ),
                expandedHeight = 400.dp,
                collapsedHeight = 56.dp,
            )
        },
    ) { contentPadding ->
        DynamicWeatherBackground(
            timeOfDay = timeOfDay,
            modifier = modifier.fillMaxSize(),
        ) {
            WeatherContent(
                contentPadding = contentPadding,
                todaysHourlyForecast = todaysHourlyForecast,
                weeklyForecast = weeklyForecast,
                uvIndex = uvIndex,
            )
        }
    }
}

@Composable
private fun WeatherContent(
    contentPadding: PaddingValues,
    todaysHourlyForecast: List<HourlyWeatherUiModel>,
    weeklyForecast: List<DailyWeatherUiModel>,
    uvIndex: UvUiModel?,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(top = contentPadding.calculateTopPadding()),
        contentPadding =
            PaddingValues(
                start = 24.dp,
                end = 24.dp,
                bottom = 80.dp,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }

        if (todaysHourlyForecast.isNotEmpty()) {
            item {
                TodaysForecast(
                    hourlyForecast = todaysHourlyForecast,
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        if (weeklyForecast.isNotEmpty()) {
            item {
                WeeklyForecast(
                    weeklyForecast = weeklyForecast,
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        item {
            ExtraInfoCards(uvIndex = uvIndex)
        }
    }
}

@Composable
private fun ExtraInfoCards(uvIndex: UvUiModel?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        if (uvIndex != null) {
            UvCard(
                uvModel = uvIndex,
                modifier = Modifier.weight(1f),
            )
        }

        // Placeholder for second card - will be filled later
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview(name = "Weather Success Screen - Day")
@Composable
private fun WeatherSuccessScreenDayPreview() {
    TempestTheme {
        WeatherSuccessScreen(
            weatherDescription = "Clear sky",
            formattedLocation = "London, UK",
            formattedTemperature = "22°C",
            currentWeatherIconUrl = "https://openweathermap.org/img/wn/01d@2x.png",
            currentWeatherIconDescription = "clear sky",
            todaysHourlyForecast =
                listOf(
                    HourlyWeatherUiModel(
                        formattedTime = "14:00",
                        temperature = "22°",
                        iconUrl = "https://openweathermap.org/img/wn/01d@2x.png",
                        iconDescription = "clear sky",
                        precipitationProbability = "0%",
                    ),
                    HourlyWeatherUiModel(
                        formattedTime = "15:00",
                        temperature = "24°",
                        iconUrl = "https://openweathermap.org/img/wn/01d@2x.png",
                        iconDescription = "clear sky",
                        precipitationProbability = "5%",
                    ),
                    HourlyWeatherUiModel(
                        formattedTime = "16:00",
                        temperature = "26°",
                        iconUrl = "https://openweathermap.org/img/wn/02d@2x.png",
                        iconDescription = "few clouds",
                        precipitationProbability = "10%",
                    ),
                ),
            weeklyForecast =
                listOf(
                    DailyWeatherUiModel(
                        formattedDay = "Mon",
                        temperatureHigh = "25°C",
                        temperatureLow = "15°C",
                        iconUrl = "https://openweathermap.org/img/wn/01d@2x.png",
                        iconDescription = "clear sky",
                    ),
                    DailyWeatherUiModel(
                        formattedDay = "Tue",
                        temperatureHigh = "23°C",
                        temperatureLow = "13°C",
                        iconUrl = "https://openweathermap.org/img/wn/02d@2x.png",
                        iconDescription = "few clouds",
                    ),
                    DailyWeatherUiModel(
                        formattedDay = "Wed",
                        temperatureHigh = "21°C",
                        temperatureLow = "11°C",
                        iconUrl = "https://openweathermap.org/img/wn/03d@2x.png",
                        iconDescription = "scattered clouds",
                    ),
                ),
            timeOfDay = TimeOfDay.DAY,
            uvIndex = UvUiModel(6, "High", "Protection essential", 0.55f),
            onLocationClick = {},
        )
    }
}

@Preview(name = "Weather Success Screen - Night")
@Composable
private fun WeatherSuccessScreenNightPreview() {
    TempestTheme {
        WeatherSuccessScreen(
            weatherDescription = "Clear sky",
            formattedLocation = "Paris, FR",
            formattedTemperature = "18°C",
            currentWeatherIconUrl = "https://openweathermap.org/img/wn/01n@2x.png",
            currentWeatherIconDescription = "clear sky",
            todaysHourlyForecast =
                listOf(
                    HourlyWeatherUiModel(
                        formattedTime = "22:00",
                        temperature = "18°",
                        iconUrl = "https://openweathermap.org/img/wn/01n@2x.png",
                        iconDescription = "clear sky",
                        precipitationProbability = "0%",
                    ),
                    HourlyWeatherUiModel(
                        formattedTime = "23:00",
                        temperature = "16°",
                        iconUrl = "https://openweathermap.org/img/wn/01n@2x.png",
                        iconDescription = "clear sky",
                        precipitationProbability = "0%",
                    ),
                ),
            weeklyForecast =
                listOf(
                    DailyWeatherUiModel(
                        formattedDay = "Tomorrow",
                        temperatureHigh = "22°C",
                        temperatureLow = "12°C",
                        iconUrl = "https://openweathermap.org/img/wn/02d@2x.png",
                        iconDescription = "few clouds",
                    ),
                ),
            timeOfDay = TimeOfDay.NIGHT,
            uvIndex = UvUiModel(1, "Low", "Minimal protection required", 0.09f),
            onLocationClick = {},
        )
    }
}

@Preview(name = "Weather Success Screen - No Forecasts")
@Composable
private fun WeatherSuccessScreenNoForecastsPreview() {
    TempestTheme {
        WeatherSuccessScreen(
            weatherDescription = "Partly cloudy",
            formattedLocation = "Tokyo, JP",
            formattedTemperature = "28°C",
            currentWeatherIconUrl = "https://openweathermap.org/img/wn/02d@2x.png",
            currentWeatherIconDescription = "few clouds",
            todaysHourlyForecast = emptyList(),
            weeklyForecast = emptyList(),
            timeOfDay = TimeOfDay.DAY,
            uvIndex = null,
            onLocationClick = {},
        )
    }
}

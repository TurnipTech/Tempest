package com.harry.weather.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.harry.design.TempestTheme
import com.harry.weather.domain.model.TimeOfDay
import com.harry.weather.ui.model.DailyWeatherUiModel
import com.harry.weather.ui.model.HourlyWeatherUiModel

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
    onLocationClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DynamicWeatherBackground(
        timeOfDay = timeOfDay,
        modifier = modifier.fillMaxSize(),
    ) {
        WeatherContent(
            weatherDescription = weatherDescription,
            formattedLocation = formattedLocation,
            formattedTemperature = formattedTemperature,
            currentWeatherIconUrl = currentWeatherIconUrl,
            currentWeatherIconDescription = currentWeatherIconDescription,
            todaysHourlyForecast = todaysHourlyForecast,
            weeklyForecast = weeklyForecast,
            onLocationClick = onLocationClick,
        )
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
            onLocationClick = {},
        )
    }
}

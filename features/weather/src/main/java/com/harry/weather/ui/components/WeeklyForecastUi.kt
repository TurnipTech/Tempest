package com.harry.weather.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.harry.design.OverlayColors
import com.harry.design.TempestTheme
import com.harry.weather.R
import com.harry.weather.ui.model.DailyWeatherUiModel

@Composable
fun WeeklyForecast(weeklyForecast: List<DailyWeatherUiModel>, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor = OverlayColors.surfaceTranslucent,
            ),
        shape = RoundedCornerShape(16.dp),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 0.dp,
            ),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
        ) {
            Text(
                text = stringResource(R.string.seven_day_forecast),
                style = MaterialTheme.typography.titleLarge,
                color = OverlayColors.contentPrimary,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                weeklyForecast.forEach { dailyWeather ->
                    DailyWeatherItem(dailyWeather = dailyWeather)
                }
            }
        }
    }
}

@Composable
fun DailyWeatherItem(dailyWeather: DailyWeatherUiModel, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = dailyWeather.formattedDay,
            style = MaterialTheme.typography.titleMedium,
            color = OverlayColors.contentPrimary,
            modifier = Modifier.weight(1f),
        )

        WeatherIcon(
            iconUrl = dailyWeather.iconUrl,
            contentDescription = dailyWeather.iconDescription,
            modifier = Modifier.size(32.dp),
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.temperature_high_icon),
                    style = MaterialTheme.typography.labelMedium,
                    color = OverlayColors.contentInteractive,
                )
                Text(
                    text = dailyWeather.temperatureHigh,
                    style = MaterialTheme.typography.bodyMedium,
                    color = OverlayColors.contentPrimary,
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.temperature_low_icon),
                    style = MaterialTheme.typography.labelMedium,
                    color = OverlayColors.contentDisabled,
                )
                Text(
                    text = dailyWeather.temperatureLow,
                    style = MaterialTheme.typography.bodyMedium,
                    color = OverlayColors.contentTertiary,
                )
            }
        }
    }
}

@Preview
@Composable
fun WeeklyForecastPreview() {
    val sampleWeeklyData =
        listOf(
            DailyWeatherUiModel(
                formattedDay = "Mon",
                temperatureHigh = "25°",
                temperatureLow = "15°",
                iconUrl = "https://openweathermap.org/img/wn/01d@2x.png",
                iconDescription = "clear sky",
            ),
            DailyWeatherUiModel(
                formattedDay = "Tue",
                temperatureHigh = "23°",
                temperatureLow = "13°",
                iconUrl = "https://openweathermap.org/img/wn/02d@2x.png",
                iconDescription = "few clouds",
            ),
            DailyWeatherUiModel(
                formattedDay = "Wed",
                temperatureHigh = "20°",
                temperatureLow = "10°",
                iconUrl = "https://openweathermap.org/img/wn/10d@2x.png",
                iconDescription = "light rain",
            ),
            DailyWeatherUiModel(
                formattedDay = "Thu",
                temperatureHigh = "22°",
                temperatureLow = "12°",
                iconUrl = "https://openweathermap.org/img/wn/04d@2x.png",
                iconDescription = "broken clouds",
            ),
            DailyWeatherUiModel(
                formattedDay = "Fri",
                temperatureHigh = "24°",
                temperatureLow = "14°",
                iconUrl = "https://openweathermap.org/img/wn/03d@2x.png",
                iconDescription = "scattered clouds",
            ),
        )

    TempestTheme {
        WeeklyForecast(weeklyForecast = sampleWeeklyData)
    }
}

@Preview
@Composable
fun DailyWeatherItemPreview() {
    val sampleDailyWeather =
        DailyWeatherUiModel(
            formattedDay = "Mon",
            temperatureHigh = "25°",
            temperatureLow = "15°",
            iconUrl = "https://openweathermap.org/img/wn/01d@2x.png",
            iconDescription = "clear sky",
        )

    TempestTheme {
        DailyWeatherItem(dailyWeather = sampleDailyWeather)
    }
}

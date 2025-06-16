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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.harry.weather.ui.model.DailyWeatherUiModel

@Composable
fun WeeklyForecast(weeklyForecast: List<DailyWeatherUiModel>, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.15f),
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
                text = "7-Day Forecast",
                style =
                    MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                    ),
                color = Color.White.copy(alpha = 0.9f),
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
            style =
                MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                ),
            color = Color.White.copy(alpha = 0.9f),
            modifier = Modifier.weight(1f),
        )

        WeatherIcon(
            iconUrl = dailyWeather.iconUrl,
            contentDescription = dailyWeather.iconDescription,
            modifier = Modifier.size(32.dp),
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = dailyWeather.temperatureHigh,
                style =
                    MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                    ),
                color = Color.White,
            )

            Text(
                text = dailyWeather.temperatureLow,
                style =
                    MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                    ),
                color = Color.White.copy(alpha = 0.7f),
            )
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

    MaterialTheme {
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

    MaterialTheme {
        DailyWeatherItem(dailyWeather = sampleDailyWeather)
    }
}

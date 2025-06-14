package com.harry.weather.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.harry.weather.ui.model.HourlyWeatherUiModel

@Composable
fun TodaysForecast(hourlyForecast: List<HourlyWeatherUiModel>, modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
        items(hourlyForecast) { hourlyWeather ->
            HourlyWeatherItem(hourlyWeather = hourlyWeather)
        }
    }
}

@Composable
fun HourlyWeatherItem(hourlyWeather: HourlyWeatherUiModel, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = hourlyWeather.formattedTime,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
            )

            WeatherIcon(
                iconUrl = hourlyWeather.iconUrl,
                contentDescription = hourlyWeather.iconDescription,
                modifier = Modifier.size(32.dp),
            )

            Text(
                text = hourlyWeather.temperature,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = hourlyWeather.precipitationProbability,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
fun WeatherIcon(iconUrl: String, contentDescription: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model =
            ImageRequest
                .Builder(LocalContext.current)
                .data(iconUrl)
                .crossfade(true)
                .build(),
        contentDescription = contentDescription,
        modifier = modifier,
    )
}

@Preview
@Composable
fun TodaysForecastPreview() {
    val sampleHourlyData =
        listOf(
            HourlyWeatherUiModel(
                formattedTime = "14:00",
                temperature = "22°",
                iconUrl = "https://openweathermap.org/img/wn/01d@2x.png",
                iconDescription = "clear sky",
                precipitationProbability = "20%",
            ),
            HourlyWeatherUiModel(
                formattedTime = "15:00",
                temperature = "25°",
                iconUrl = "https://openweathermap.org/img/wn/02d@2x.png",
                iconDescription = "few clouds",
                precipitationProbability = "10%",
            ),
            HourlyWeatherUiModel(
                formattedTime = "16:00",
                temperature = "20°",
                iconUrl = "https://openweathermap.org/img/wn/10d@2x.png",
                iconDescription = "light rain",
                precipitationProbability = "60%",
            ),
        )

    MaterialTheme {
        TodaysForecast(hourlyForecast = sampleHourlyData)
    }
}

@Preview
@Composable
fun HourlyWeatherItemPreview() {
    val sampleHourlyWeather =
        HourlyWeatherUiModel(
            formattedTime = "14:00",
            temperature = "22°",
            iconUrl = "https://openweathermap.org/img/wn/01d@2x.png",
            iconDescription = "clear sky",
            precipitationProbability = "20%",
        )

    MaterialTheme {
        HourlyWeatherItem(hourlyWeather = sampleHourlyWeather)
    }
}

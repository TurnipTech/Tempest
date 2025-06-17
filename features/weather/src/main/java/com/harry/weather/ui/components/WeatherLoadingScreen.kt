package com.harry.weather.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.harry.design.TempestTheme
import com.harry.weather.domain.model.TimeOfDay

@Composable
fun WeatherLoadingScreen(modifier: Modifier = Modifier) {
    DynamicWeatherBackground(
        timeOfDay = TimeOfDay.DAY,
        modifier = modifier.fillMaxSize(),
    ) {
        WeatherLoadingSkeleton()
    }
}

@Preview(name = "Weather Loading Screen - Day")
@Composable
private fun WeatherLoadingScreenDayPreview() {
    TempestTheme {
        WeatherLoadingScreen()
    }
}

@Preview(name = "Weather Loading Screen - Night", backgroundColor = 0xFF1A1A2E)
@Composable
private fun WeatherLoadingScreenNightPreview() {
    TempestTheme {
        DynamicWeatherBackground(
            timeOfDay = TimeOfDay.NIGHT,
            modifier = Modifier.fillMaxSize(),
        ) {
            WeatherLoadingSkeleton()
        }
    }
}

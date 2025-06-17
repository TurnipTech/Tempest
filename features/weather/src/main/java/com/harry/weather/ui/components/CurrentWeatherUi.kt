package com.harry.weather.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.harry.design.TempestTheme

@Composable
fun CurrentWeather(
    description: String,
    locationName: String,
    currentTemp: String,
    iconUrl: String,
    iconDescription: String,
    onLocationClick: () -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Location(
            locationName = locationName,
            onLocationClick = onLocationClick,
        )

        AsyncImage(
            model =
                ImageRequest
                    .Builder(LocalContext.current)
                    .data(iconUrl)
                    .crossfade(true)
                    .build(),
            contentDescription = iconDescription,
            modifier = Modifier.size(120.dp),
        )

        Text(
            text = currentTemp,
            style = MaterialTheme.typography.displayLarge,
            color = Color.White,
        )

        Text(
            text = description,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White.copy(alpha = 0.85f),
        )
    }
}

@Composable
fun Location(modifier: Modifier = Modifier, locationName: String, onLocationClick: () -> Unit = {}) {
    Row(
        modifier = modifier.clickable { onLocationClick() },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Location",
            modifier = Modifier.size(16.dp),
            tint = Color.White.copy(alpha = 0.8f),
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = locationName,
            style =
                MaterialTheme.typography.titleMedium.copy(
                    textDecoration = TextDecoration.Underline,
                ),
            color = Color.White.copy(alpha = 0.8f),
        )
    }
}

@Preview
@Composable
fun CurrentWeatherPreview() {
    TempestTheme {
        CurrentWeather(
            description = "broken clouds",
            locationName = "Bradford",
            currentTemp = "22°",
            iconUrl = "https://openweathermap.org/img/wn/04d@2x.png",
            iconDescription = "broken clouds",
        )
    }
}

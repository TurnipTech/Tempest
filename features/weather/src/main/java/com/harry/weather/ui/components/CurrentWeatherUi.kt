package com.harry.weather.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CurrentWeather(description: String, locationName: String, currentTemp: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Location(modifier = Modifier, locationName)
        Text(text = currentTemp, fontSize = 48.sp)
        Text(text = description)
    }
}

@Composable
fun Location(modifier: Modifier = Modifier, locationName: String) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Location", // todo - merge decendants
            Modifier.size(8.dp),
        )
        Text(text = locationName, fontSize = 8.sp)
    }
}

@Preview
@Composable
fun CurrentWeatherPreview() {
    CurrentWeather(description = "broken clouds", locationName = "Bradford", currentTemp = "22°")
}

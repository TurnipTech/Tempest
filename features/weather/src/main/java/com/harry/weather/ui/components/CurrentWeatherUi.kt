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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CurrentWeather(
    description: String,
    locationName: String,
    currentTemp: String,
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

        Text(
            text = currentTemp,
            fontSize = 96.sp,
            fontWeight = FontWeight.ExtraLight,
            color = Color.White,
            lineHeight = 96.sp,
        )

        Text(
            text = description,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
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
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White.copy(alpha = 0.8f),
            textDecoration = TextDecoration.Underline,
        )
    }
}

@Preview
@Composable
fun CurrentWeatherPreview() {
    CurrentWeather(description = "broken clouds", locationName = "Bradford", currentTemp = "22°")
}

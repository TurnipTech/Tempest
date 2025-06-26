@file:OptIn(ExperimentalMaterial3Api::class)

package com.harry.weather.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.harry.design.OverlayColors
import com.harry.design.TempestTheme

private const val MIN_SCALE = 0.8f
private const val MAX_BLUR_RADIUS = 8f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentWeather(
    description: String,
    locationName: String,
    currentTemp: String,
    iconUrl: String,
    iconDescription: String,
    onLocationClick: () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    val collapseFraction = scrollBehavior?.state?.collapsedFraction ?: 0f

    if (collapseFraction > 0.95f) {
        CollapsedCurrentWeather(
            locationName = locationName,
            currentTemp = currentTemp,
            iconUrl = iconUrl,
            iconDescription = iconDescription,
            onLocationClick = onLocationClick,
            collapseFraction = collapseFraction,
        )
    } else {
        ExpandedCurrentWeather(
            description = description,
            locationName = locationName,
            currentTemp = currentTemp,
            iconUrl = iconUrl,
            iconDescription = iconDescription,
            onLocationClick = onLocationClick,
            expandedFraction = 1f - collapseFraction,
            scrollBehavior = scrollBehavior,
        )
    }
}

@Composable
private fun ExpandedCurrentWeather(
    description: String,
    locationName: String,
    currentTemp: String,
    iconUrl: String,
    iconDescription: String,
    onLocationClick: () -> Unit,
    expandedFraction: Float,
    scrollBehavior: TopAppBarScrollBehavior?,
) {
    // Calculate zoom out effect: scale from 1.0 to MIN_SCALE as we collapse
    val scale = MIN_SCALE + ((1f - MIN_SCALE) * expandedFraction)
    // val alpha = (expandedFraction * 1.5f).coerceIn(0f, 1f)

    val fadeThreshold = 0.7f
    val alpha =
        if (expandedFraction > fadeThreshold) {
            ((expandedFraction - fadeThreshold) / (1f - fadeThreshold)).coerceIn(0f, 1f)
        } else {
            0f
        }

    val offsetY =
        if (scrollBehavior != null) {
            (scrollBehavior.state.heightOffset * 0.1f).dp // Adjust this multiplier
        } else {
            0.dp
        }

    // Calculate blur effect: blur from 0dp to MAX_BLUR_RADIUS as we collapse
    val blurRadius = (1f - expandedFraction) * MAX_BLUR_RADIUS

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .scale(scale)
                .blur(blurRadius.dp)
                .alpha(alpha)
                .offset(y = -offsetY),
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
            color = OverlayColors.contentPrimary,
        )

        Text(
            text = description,
            style = MaterialTheme.typography.headlineMedium,
            color = OverlayColors.contentSecondary,
            modifier = Modifier.alpha(expandedFraction),
        )
    }
}

@Composable
private fun CollapsedCurrentWeather(
    locationName: String,
    currentTemp: String,
    iconUrl: String,
    iconDescription: String,
    onLocationClick: () -> Unit,
    collapseFraction: Float,
) {
    val fadeThreshold = 0.05f
    val alpha =
        if (collapseFraction > fadeThreshold) {
            ((collapseFraction - fadeThreshold) / (1f - fadeThreshold)).coerceIn(0f, 1f)
        } else {
            0f
        }

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .alpha(alpha),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Location(
            locationName = locationName,
            onLocationClick = onLocationClick,
        )

        Spacer(Modifier.padding(8.dp))

        AsyncImage(
            model =
                ImageRequest
                    .Builder(LocalContext.current)
                    .data(iconUrl)
                    .crossfade(true)
                    .build(),
            contentDescription = iconDescription,
            modifier = Modifier.size(24.dp),
        )

        Spacer(Modifier.padding(8.dp))

        Text(
            text = currentTemp,
            style = MaterialTheme.typography.titleMedium,
            color = OverlayColors.contentPrimary,
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
            tint = OverlayColors.contentInteractive,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = locationName,
            style =
                MaterialTheme.typography.titleMedium.copy(
                    textDecoration = TextDecoration.Underline,
                ),
            color = OverlayColors.contentInteractive,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ExpandedCurrentWeatherPreview() {
    TempestTheme {
        ExpandedCurrentWeather(
            description = "broken clouds",
            locationName = "Bradford",
            currentTemp = "22°",
            iconUrl = "https://openweathermap.org/img/wn/04d@2x.png",
            iconDescription = "broken clouds",
            onLocationClick = { },
            expandedFraction = 1f,
            null,
        )
    }
}

@Preview
@Composable
fun CollapsedCurrentWeatherPreview() {
    TempestTheme {
        CollapsedCurrentWeather(
            locationName = "Bradford",
            currentTemp = "22°",
            iconUrl = "https://openweathermap.org/img/wn/04d@2x.png",
            iconDescription = "broken clouds",
            onLocationClick = { },
            100f,
        )
    }
}

package com.harry.weather.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.harry.design.TempestTheme
import com.harry.weather.R
import com.harry.weather.domain.model.TimeOfDay

@Composable
fun WeatherErrorScreen(
    message: String,
    canRetry: Boolean,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DynamicWeatherBackground(
        timeOfDay = TimeOfDay.DAY,
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(32.dp),
            ) {
                Text(
                    text = stringResource(R.string.unable_to_load_weather),
                    color = Color.White.copy(alpha = 0.9f),
                    style =
                        MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.3.sp,
                        ),
                )
                Text(
                    text = message,
                    color = Color.White.copy(alpha = 0.7f),
                    style =
                        MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.2.sp,
                        ),
                    textAlign = TextAlign.Center,
                )
                if (canRetry) {
                    Button(
                        onClick = onRetry,
                        modifier = Modifier.sizeIn(minWidth = 64.dp, minHeight = 40.dp),
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            ),
                        elevation =
                            ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp,
                                pressedElevation = 8.dp,
                            ),
                    ) {
                        Text(
                            text = stringResource(R.string.retry),
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Weather Error Screen - With Retry")
@Composable
private fun WeatherErrorScreenWithRetryPreview() {
    TempestTheme {
        WeatherErrorScreen(
            message = "Network error occurred. Please check your internet connection.",
            canRetry = true,
            onRetry = {},
        )
    }
}

@Preview(name = "Weather Error Screen - No Retry")
@Composable
private fun WeatherErrorScreenNoRetryPreview() {
    TempestTheme {
        WeatherErrorScreen(
            message = "No location available. Please enable location services.",
            canRetry = false,
            onRetry = {},
        )
    }
}

@Preview(name = "Weather Error Screen - Short Message")
@Composable
private fun WeatherErrorScreenShortMessagePreview() {
    TempestTheme {
        WeatherErrorScreen(
            message = "Failed to load weather data",
            canRetry = true,
            onRetry = {},
        )
    }
}

package com.harry.weather.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class HourlyWeatherUiModel(
    val formattedTime: String,
    val temperature: String,
    val iconUrl: String,
    val iconDescription: String,
    val precipitationProbability: String,
)

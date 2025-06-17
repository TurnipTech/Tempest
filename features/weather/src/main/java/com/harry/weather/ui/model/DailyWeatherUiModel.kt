package com.harry.weather.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class DailyWeatherUiModel(
    val formattedDay: String,
    val temperatureHigh: String,
    val temperatureLow: String,
    val iconUrl: String,
    val iconDescription: String,
)

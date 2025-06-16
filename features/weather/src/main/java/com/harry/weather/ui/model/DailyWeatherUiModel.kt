package com.harry.weather.ui.model

data class DailyWeatherUiModel(
    val formattedDay: String,
    val temperatureHigh: String,
    val temperatureLow: String,
    val iconUrl: String,
    val iconDescription: String,
)

package com.harry.weather.domain

object WeatherConstants {
    const val METRIC_UNIT = "metric"
    const val IMPERIAL_UNIT = "imperial"
    const val STANDARD_UNIT = "standard"

    const val DEFAULT_LANGUAGE = "en"

    // Forecast parts that can be excluded from API calls
    const val EXCLUDE_MINUTELY = "minutely"
    const val EXCLUDE_HOURLY = "hourly"
    const val EXCLUDE_DAILY = "daily"
    const val EXCLUDE_ALERTS = "alerts"
}

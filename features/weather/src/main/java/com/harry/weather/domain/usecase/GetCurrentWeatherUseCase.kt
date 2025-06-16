package com.harry.weather.domain.usecase

import com.harry.weather.data.WeatherRepository
import com.harry.weather.domain.WeatherConstants
import com.harry.weather.domain.model.WeatherData

class GetCurrentWeatherUseCase(
    private val repository: WeatherRepository,
) {
    suspend operator fun invoke(
        latitude: Double,
        longitude: Double,
        units: String = WeatherConstants.METRIC_UNIT,
        language: String = WeatherConstants.DEFAULT_LANGUAGE,
    ): Result<WeatherData> =
        repository.getCurrentWeatherAndForecasts(
            latitude = latitude,
            longitude = longitude,
            excludeParts = listOf(WeatherConstants.EXCLUDE_MINUTELY),
            units = units,
            language = language,
        )
}

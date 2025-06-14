package com.harry.weather.domain.usecase

import com.harry.weather.data.WeatherRepository
import com.harry.weather.domain.model.WeatherData

class GetCurrentWeatherUseCase(
    private val repository: WeatherRepository,
) {
    suspend operator fun invoke(
        latitude: Double,
        longitude: Double,
        units: String = "metric",
        language: String = "en",
    ): Result<WeatherData> =
        repository.getCurrentWeatherAndForecasts(
            latitude = latitude,
            longitude = longitude,
            excludeParts = emptyList(), // Get all data for now
            units = units,
            language = language,
        )
}

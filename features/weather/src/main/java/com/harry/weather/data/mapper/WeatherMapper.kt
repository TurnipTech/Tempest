package com.harry.weather.data.mapper

import com.harry.weather.data.dto.CurrentWeatherDto
import com.harry.weather.data.dto.DailyDto
import com.harry.weather.data.dto.HourlyDto
import com.harry.weather.data.dto.WeatherConditionDto
import com.harry.weather.data.dto.WeatherResponseDto
import com.harry.weather.domain.model.CurrentWeather
import com.harry.weather.domain.model.DailyWeather
import com.harry.weather.domain.model.HourlyWeather
import com.harry.weather.domain.model.WeatherCondition
import com.harry.weather.domain.model.WeatherData

object WeatherMapper {
    fun mapToWeatherData(dto: WeatherResponseDto): WeatherData =
        WeatherData(
            timezone = dto.timezone,
            currentWeather = dto.current?.let { mapToCurrentWeather(it) },
            hourlyForecast = dto.hourly?.map { mapToHourlyWeather(it) },
            dailyForecast = dto.daily?.map { mapToDailyWeather(it) },
        )

    private fun mapToCurrentWeather(dto: CurrentWeatherDto): CurrentWeather =
        CurrentWeather(
            temperature = dto.temperature,
            sunrise = dto.sunrise,
            sunset = dto.sunset,
            condition = mapToWeatherCondition(dto.weather.first()),
        )

    private fun mapToHourlyWeather(dto: HourlyDto): HourlyWeather =
        HourlyWeather(
            dateTime = dto.dateTime,
            temperature = dto.temperature,
            probabilityOfPrecipitation = dto.probabilityOfPrecipitation ?: 0.0,
            condition = mapToWeatherCondition(dto.weather.first()),
        )

    private fun mapToDailyWeather(dto: DailyDto): DailyWeather =
        DailyWeather(
            dateTime = dto.dateTime,
            temperatureHigh = dto.temperature.max,
            temperatureLow = dto.temperature.min,
            condition = mapToWeatherCondition(dto.weather.first()),
        )

    private fun mapToWeatherCondition(dto: WeatherConditionDto): WeatherCondition =
        WeatherCondition(
            description = dto.description,
            iconCode = dto.icon,
        )
}

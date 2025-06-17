package com.harry.weather.data.mapper

import com.harry.weather.data.dto.CurrentWeatherDto
import com.harry.weather.data.dto.DailyDto
import com.harry.weather.data.dto.DailySummaryResponseDto
import com.harry.weather.data.dto.HistoricalWeatherDto
import com.harry.weather.data.dto.HistoricalWeatherResponseDto
import com.harry.weather.data.dto.HourlyDto
import com.harry.weather.data.dto.WeatherConditionDto
import com.harry.weather.data.dto.WeatherOverviewResponseDto
import com.harry.weather.data.dto.WeatherResponseDto
import com.harry.weather.domain.model.CurrentWeather
import com.harry.weather.domain.model.DailySummary
import com.harry.weather.domain.model.DailyWeather
import com.harry.weather.domain.model.HistoricalWeather
import com.harry.weather.domain.model.HistoricalWeatherData
import com.harry.weather.domain.model.HourlyWeather
import com.harry.weather.domain.model.TemperatureRange
import com.harry.weather.domain.model.WeatherCondition
import com.harry.weather.domain.model.WeatherData
import com.harry.weather.domain.model.WeatherOverview

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
            probabilityOfPrecipitation = dto.probabilityOfPrecipitation,
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

    fun mapToHistoricalWeather(dto: HistoricalWeatherResponseDto): HistoricalWeather =
        HistoricalWeather(
            timezone = dto.timezone,
            data = dto.data.map { mapToHistoricalWeatherData(it) },
        )

    private fun mapToHistoricalWeatherData(dto: HistoricalWeatherDto): HistoricalWeatherData =
        HistoricalWeatherData(
            dateTime = dto.dateTime,
            temperature = dto.temperature,
            feelsLike = dto.feelsLike,
            humidity = dto.humidity,
            pressure = dto.pressure,
            windSpeed = dto.windSpeed,
            uvIndex = dto.uvIndex,
            condition = mapToWeatherCondition(dto.weather.first()),
        )

    fun mapToDailySummary(dto: DailySummaryResponseDto): DailySummary =
        DailySummary(
            timezone = dto.timezone,
            date = dto.date,
            temperatureRange =
                TemperatureRange(
                    min = dto.temperature.min,
                    max = dto.temperature.max,
                    morning = dto.temperature.morning,
                    afternoon = dto.temperature.afternoon,
                    evening = dto.temperature.evening,
                    night = dto.temperature.night,
                ),
            humidity = dto.humidity.afternoon,
            pressure = dto.pressure.afternoon,
            maxWindSpeed = dto.wind.max.speed,
            totalPrecipitation = dto.precipitation.total,
            cloudCover = dto.cloudCover.afternoon,
        )

    fun mapToWeatherOverview(dto: WeatherOverviewResponseDto): WeatherOverview =
        WeatherOverview(
            timezone = dto.timezone,
            date = dto.date,
            overview = dto.weatherOverview,
        )
}

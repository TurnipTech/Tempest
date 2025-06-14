package com.harry.weather.data.mapper

import com.harry.weather.data.dto.AlertDto
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
import com.harry.weather.domain.model.Location
import com.harry.weather.domain.model.TemperatureRange
import com.harry.weather.domain.model.WeatherAlert
import com.harry.weather.domain.model.WeatherCondition
import com.harry.weather.domain.model.WeatherData
import com.harry.weather.domain.model.WeatherOverview

object WeatherMapper {
    fun mapToWeatherData(dto: WeatherResponseDto): WeatherData =
        WeatherData(
            location =
                Location(
                    latitude = dto.latitude,
                    longitude = dto.longitude,
                    timezone = dto.timezone,
                ),
            currentWeather = dto.current?.let { mapToCurrentWeather(it) },
            hourlyForecast = dto.hourly?.map { mapToHourlyWeather(it) },
            dailyForecast = dto.daily?.map { mapToDailyWeather(it) },
            alerts = dto.alerts?.map { mapToWeatherAlert(it) },
        )

    private fun mapToCurrentWeather(dto: CurrentWeatherDto): CurrentWeather =
        CurrentWeather(
            dateTime = dto.dateTime,
            sunrise = dto.sunrise,
            sunset = dto.sunset,
            temperature = dto.temperature,
            feelsLike = dto.feelsLike,
            humidity = dto.humidity,
            pressure = dto.pressure,
            windSpeed = dto.windSpeed,
            windDirection = dto.windDirection,
            uvIndex = dto.uvIndex,
            cloudiness = dto.cloudiness,
            visibility = dto.visibility,
            condition = mapToWeatherCondition(dto.weather.first()),
        )

    private fun mapToHourlyWeather(dto: HourlyDto): HourlyWeather =
        HourlyWeather(
            dateTime = dto.dateTime,
            temperature = dto.temperature,
            feelsLike = dto.feelsLike,
            humidity = dto.humidity,
            pressure = dto.pressure,
            windSpeed = dto.windSpeed,
            uvIndex = dto.uvIndex,
            probabilityOfPrecipitation = dto.probabilityOfPrecipitation,
            condition = mapToWeatherCondition(dto.weather.first()),
        )

    private fun mapToDailyWeather(dto: DailyDto): DailyWeather =
        DailyWeather(
            dateTime = dto.dateTime,
            temperatureHigh = dto.temperature.max,
            temperatureLow = dto.temperature.min,
            humidity = dto.humidity,
            pressure = dto.pressure,
            windSpeed = dto.windSpeed,
            uvIndex = dto.uvIndex,
            probabilityOfPrecipitation = dto.probabilityOfPrecipitation,
            condition = mapToWeatherCondition(dto.weather.first()),
            summary = dto.summary,
        )

    private fun mapToWeatherCondition(dto: WeatherConditionDto): WeatherCondition =
        WeatherCondition(
            id = dto.id,
            main = dto.main,
            description = dto.description,
            iconCode = dto.icon,
        )

    private fun mapToWeatherAlert(dto: AlertDto): WeatherAlert =
        WeatherAlert(
            senderName = dto.senderName,
            event = dto.event,
            startTime = dto.start,
            endTime = dto.end,
            description = dto.description,
        )

    fun mapToHistoricalWeather(dto: HistoricalWeatherResponseDto): HistoricalWeather =
        HistoricalWeather(
            location =
                Location(
                    latitude = dto.latitude,
                    longitude = dto.longitude,
                    timezone = dto.timezone,
                ),
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
            location =
                Location(
                    latitude = dto.latitude,
                    longitude = dto.longitude,
                    timezone = dto.timezone,
                ),
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
            location =
                Location(
                    latitude = dto.latitude,
                    longitude = dto.longitude,
                    timezone = dto.timezone,
                ),
            date = dto.date,
            overview = dto.weatherOverview,
        )
}

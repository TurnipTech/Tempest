package com.harry.weather.data.mapper

import com.harry.weather.data.dto.AlertDto
import com.harry.weather.data.dto.CloudCoverDto
import com.harry.weather.data.dto.CurrentWeatherDto
import com.harry.weather.data.dto.DailyDto
import com.harry.weather.data.dto.DailyFeelsLikeDto
import com.harry.weather.data.dto.DailySummaryResponseDto
import com.harry.weather.data.dto.DailyTemperatureDto
import com.harry.weather.data.dto.HistoricalWeatherDto
import com.harry.weather.data.dto.HistoricalWeatherResponseDto
import com.harry.weather.data.dto.HourlyDto
import com.harry.weather.data.dto.HumidityDto
import com.harry.weather.data.dto.MaxWindDto
import com.harry.weather.data.dto.PrecipitationSummaryDto
import com.harry.weather.data.dto.PressureDto
import com.harry.weather.data.dto.TemperatureSummaryDto
import com.harry.weather.data.dto.WeatherConditionDto
import com.harry.weather.data.dto.WeatherOverviewResponseDto
import com.harry.weather.data.dto.WeatherResponseDto
import com.harry.weather.data.dto.WindSummaryDto

/**
 * Factory for creating test data objects with sensible defaults.
 * Provides builder-style methods for customization when needed.
 */
object WeatherMapperTestDataFactory {
    fun createWeatherConditionDto(
        id: Int = 800,
        main: String = "Clear",
        description: String = "clear sky",
        icon: String = "01d",
    ) = WeatherConditionDto(
        id = id,
        main = main,
        description = description,
        icon = icon,
    )

    fun createCurrentWeatherDto(
        dateTime: Long = 1609459200L,
        temperature: Double = 20.5,
        feelsLike: Double = 18.3,
        humidity: Int = 65,
        pressure: Int = 1013,
        windSpeed: Double = 5.2,
        windDirection: Int = 180,
        uvIndex: Double = 6.8,
        cloudiness: Int = 10,
        visibility: Int = 10000,
        weather: List<WeatherConditionDto> = listOf(createWeatherConditionDto()),
        dewPoint: Double = 12.5,
    ) = CurrentWeatherDto(
        dateTime = dateTime,
        temperature = temperature,
        feelsLike = feelsLike,
        humidity = humidity,
        pressure = pressure,
        windSpeed = windSpeed,
        windDirection = windDirection,
        uvIndex = uvIndex,
        cloudiness = cloudiness,
        visibility = visibility,
        weather = weather,
        dewPoint = dewPoint,
    )

    fun createHourlyDto(
        dateTime: Long = 1609462800L,
        temperature: Double = 21.0,
        feelsLike: Double = 19.5,
        humidity: Int = 60,
        pressure: Int = 1014,
        windSpeed: Double = 4.8,
        uvIndex: Double = 7.2,
        probabilityOfPrecipitation: Double = 0.15,
        weather: List<WeatherConditionDto> = listOf(createWeatherConditionDto()),
        dewPoint: Double = 12.5,
        cloudiness: Int = 15,
        visibility: Int = 9000,
        windDirection: Int = 190,
    ) = HourlyDto(
        dateTime = dateTime,
        temperature = temperature,
        feelsLike = feelsLike,
        humidity = humidity,
        pressure = pressure,
        windSpeed = windSpeed,
        uvIndex = uvIndex,
        probabilityOfPrecipitation = probabilityOfPrecipitation,
        weather = weather,
        dewPoint = dewPoint,
        cloudiness = cloudiness,
        visibility = visibility,
        windDirection = windDirection,
    )

    fun createDailyTemperatureDto(
        day: Double = 22.0,
        min: Double = 15.0,
        max: Double = 25.0,
        night: Double = 16.0,
        evening: Double = 20.0,
        morning: Double = 18.0,
    ) = DailyTemperatureDto(
        day = day,
        min = min,
        max = max,
        night = night,
        evening = evening,
        morning = morning,
    )

    fun createDailyFeelsLikeDto(
        day: Double = 21.0,
        night: Double = 14.0,
        evening: Double = 19.0,
        morning: Double = 17.0,
    ) = DailyFeelsLikeDto(
        day = day,
        night = night,
        evening = evening,
        morning = morning,
    )

    fun createDailyDto(
        dateTime: Long = 1609459200L,
        temperature: DailyTemperatureDto = createDailyTemperatureDto(),
        feelsLike: DailyFeelsLikeDto = createDailyFeelsLikeDto(),
        humidity: Int = 70,
        pressure: Int = 1015,
        windSpeed: Double = 6.0,
        uvIndex: Double = 8.5,
        probabilityOfPrecipitation: Double = 0.3,
        weather: List<WeatherConditionDto> = listOf(createWeatherConditionDto()),
        summary: String = "Partly cloudy day",
        moonPhase: Double = 0.25,
        dewPoint: Double = 14.0,
        windDirection: Int = 200,
        cloudiness: Int = 25,
    ) = DailyDto(
        dateTime = dateTime,
        temperature = temperature,
        feelsLike = feelsLike,
        humidity = humidity,
        pressure = pressure,
        windSpeed = windSpeed,
        uvIndex = uvIndex,
        probabilityOfPrecipitation = probabilityOfPrecipitation,
        weather = weather,
        summary = summary,
        moonPhase = moonPhase,
        dewPoint = dewPoint,
        windDirection = windDirection,
        cloudiness = cloudiness,
    )

    fun createAlertDto(
        senderName: String = "National Weather Service",
        event: String = "Heavy Rain Warning",
        start: Long = 1609459200L,
        end: Long = 1609545600L,
        description: String = "Heavy rain expected in the area",
        tags: List<String> = listOf("rain", "warning"),
    ) = AlertDto(
        senderName = senderName,
        event = event,
        start = start,
        end = end,
        description = description,
        tags = tags,
    )

    fun createWeatherResponseDto(
        latitude: Double = 40.7128,
        longitude: Double = -74.0060,
        timezone: String = "America/New_York",
        timezoneOffset: Int = -18000,
        current: CurrentWeatherDto? = createCurrentWeatherDto(),
        hourly: List<HourlyDto>? = listOf(createHourlyDto()),
        daily: List<DailyDto>? = listOf(createDailyDto()),
        alerts: List<AlertDto>? = listOf(createAlertDto()),
    ) = WeatherResponseDto(
        latitude = latitude,
        longitude = longitude,
        timezone = timezone,
        timezoneOffset = timezoneOffset,
        current = current,
        hourly = hourly,
        daily = daily,
        alerts = alerts,
    )

    fun createHistoricalWeatherDto(
        dateTime: Long = 1609459200L,
        temperature: Double = 18.5,
        feelsLike: Double = 16.2,
        humidity: Int = 75,
        pressure: Int = 1010,
        windSpeed: Double = 3.8,
        uvIndex: Double = 4.5,
        weather: List<WeatherConditionDto> =
            listOf(createWeatherConditionDto(id = 801, main = "Clouds", description = "few clouds", icon = "02d")),
        dewPoint: Double = 13.5,
        cloudiness: Int = 20,
        visibility: Int = 8000,
        windDirection: Int = 150,
    ) = HistoricalWeatherDto(
        dateTime = dateTime,
        temperature = temperature,
        feelsLike = feelsLike,
        humidity = humidity,
        pressure = pressure,
        windSpeed = windSpeed,
        uvIndex = uvIndex,
        weather = weather,
        dewPoint = dewPoint,
        cloudiness = cloudiness,
        visibility = visibility,
        windDirection = windDirection,
    )

    fun createHistoricalWeatherResponseDto(
        latitude: Double = 48.8566,
        longitude: Double = 2.3522,
        timezone: String = "Europe/Paris",
        timezoneOffset: Int = 3600,
        data: List<HistoricalWeatherDto> = listOf(createHistoricalWeatherDto()),
    ) = HistoricalWeatherResponseDto(
        latitude = latitude,
        longitude = longitude,
        timezone = timezone,
        timezoneOffset = timezoneOffset,
        data = data,
    )

    fun createTemperatureSummaryDto(
        min: Double = 12.0,
        max: Double = 28.0,
        afternoon: Double = 24.0,
        night: Double = 14.0,
        evening: Double = 20.0,
        morning: Double = 16.0,
    ) = TemperatureSummaryDto(
        min = min,
        max = max,
        afternoon = afternoon,
        night = night,
        evening = evening,
        morning = morning,
    )

    fun createWindSummaryDto(maxSpeed: Double = 8.5, maxDirection: Int = 225) =
        WindSummaryDto(
            max =
                MaxWindDto(
                    speed = maxSpeed,
                    direction = maxDirection,
                ),
        )

    fun createDailySummaryResponseDto(
        latitude: Double = 35.6762,
        longitude: Double = 139.6503,
        timezone: String = "Asia/Tokyo",
        date: String = "2024-06-14",
        units: String = "metric",
        cloudCover: CloudCoverDto = CloudCoverDto(afternoon = 30),
        humidity: HumidityDto = HumidityDto(afternoon = 55),
        precipitation: PrecipitationSummaryDto = PrecipitationSummaryDto(total = 2.5),
        temperature: TemperatureSummaryDto = createTemperatureSummaryDto(),
        pressure: PressureDto = PressureDto(afternoon = 1018),
        wind: WindSummaryDto = createWindSummaryDto(),
    ) = DailySummaryResponseDto(
        latitude = latitude,
        longitude = longitude,
        timezone = timezone,
        date = date,
        units = units,
        cloudCover = cloudCover,
        humidity = humidity,
        precipitation = precipitation,
        temperature = temperature,
        pressure = pressure,
        wind = wind,
    )

    fun createWeatherOverviewResponseDto(
        latitude: Double = 37.7749,
        longitude: Double = -122.4194,
        timezone: String = "America/Los_Angeles",
        date: String = "2024-06-14",
        units: String = "imperial",
        weatherOverview: String = "Sunny with clear skies throughout the day. Light winds from the west.",
    ) = WeatherOverviewResponseDto(
        latitude = latitude,
        longitude = longitude,
        timezone = timezone,
        date = date,
        units = units,
        weatherOverview = weatherOverview,
    )
}

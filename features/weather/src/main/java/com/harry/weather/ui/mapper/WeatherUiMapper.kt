package com.harry.weather.ui.mapper

import com.harry.utils.ResourceProvider
import com.harry.weather.R
import com.harry.weather.domain.WeatherConstants
import com.harry.weather.domain.model.DailyWeather
import com.harry.weather.domain.model.HourlyWeather
import com.harry.weather.domain.model.TimeOfDay
import com.harry.weather.domain.model.WeatherData
import com.harry.weather.ui.model.DailyWeatherUiModel
import com.harry.weather.ui.model.HourlyWeatherUiModel
import com.harry.weather.ui.model.WeatherUiState
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Unit type constants (using shared domain constants)
private val IMPERIAL_UNIT = WeatherConstants.IMPERIAL_UNIT
private val STANDARD_UNIT = WeatherConstants.STANDARD_UNIT
private val METRIC_UNIT = WeatherConstants.METRIC_UNIT

// Non-localizable constants
private const val PERCENTAGE_SYMBOL = "%"

// Time and date constants
private const val TIME_FORMAT_PATTERN = "HH:mm"
private const val SECONDS_IN_DAY = 24 * 60 * 60
private const val MILLISECONDS_CONVERSION_FACTOR = 1000L

// Forecast constants
private const val MAX_HOURLY_FORECAST_ITEMS = 24
private const val MAX_DAILY_FORECAST_ITEMS = 7

// Icon URL constants
private const val ICON_BASE_URL = "https://openweathermap.org/img/wn/"

enum class IconSize(
    val suffix: String,
) {
    MEDIUM("@2x.png"),
    LARGE("@4x.png"),
}

class WeatherUiMapper(
    private val resourceProvider: ResourceProvider,
) {
    enum class TemperatureUnit(
        val unitString: String,
        val symbol: String,
    ) {
        CELSIUS(METRIC_UNIT, "°C"),
        FAHRENHEIT(IMPERIAL_UNIT, "°F"),
        KELVIN(STANDARD_UNIT, "K"),
        ;

        companion object {
            fun fromUnitString(unitString: String): TemperatureUnit =
                entries.find { it.unitString == unitString } ?: CELSIUS
        }
    }

    fun mapToSuccessState(weatherData: WeatherData, units: String, locationName: String): WeatherUiState.Success {
        val currentWeather = weatherData.currentWeather
        val currentTime = Clock.System.now()
        val timeOfDay =
            TimeOfDay.fromSolarData(
                currentTime = currentTime,
                sunrise = currentWeather?.sunrise?.let { Instant.fromEpochSeconds(it) },
                sunset = currentWeather?.sunset?.let { Instant.fromEpochSeconds(it) },
                timeZone = TimeZone.of(weatherData.timezone),
            )

        return WeatherUiState.Success(
            weatherData = weatherData,
            formattedTemperature = formatTemperature(currentWeather?.temperature, units),
            formattedLocation = locationName,
            weatherDescription = formatWeatherDescription(currentWeather?.condition?.description),
            currentWeatherIconUrl = createIconUrl(currentWeather?.condition?.iconCode ?: "", IconSize.LARGE),
            currentWeatherIconDescription =
                currentWeather?.condition?.description ?: resourceProvider.getString(R.string.no_data_available),
            lastUpdated = formatLastUpdated(),
            todaysHourlyForecast =
                mapTodaysHourlyForecast(
                    weatherData.hourlyForecast ?: emptyList(),
                    weatherData.timezone,
                ),
            weeklyForecast =
                mapWeeklyForecast(
                    weatherData.dailyForecast ?: emptyList(),
                    units,
                    weatherData.timezone,
                ),
            timeOfDay = timeOfDay,
        )
    }

    private fun formatTemperature(temperature: Double?, units: String): String =
        if (temperature != null) {
            val temperatureUnit = TemperatureUnit.fromUnitString(units)
            "${temperature.toInt()}${temperatureUnit.symbol}"
        } else {
            resourceProvider.getString(R.string.temperature_not_available)
        }

    private fun formatWeatherDescription(description: String?): String =
        description?.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase() else it.toString()
        } ?: resourceProvider.getString(R.string.no_data_available)

    private fun formatLastUpdated(): String =
        "${resourceProvider.getString(R.string.updated_prefix)}${
            SimpleDateFormat(
                TIME_FORMAT_PATTERN,
                Locale.getDefault(),
            ).format(Date())
        }"

    private fun mapTodaysHourlyForecast(
        hourlyForecast: List<HourlyWeather>,
        timezoneId: String,
    ): List<HourlyWeatherUiModel> {
        val currentTime = System.currentTimeMillis() / MILLISECONDS_CONVERSION_FACTOR
        val endOfDay = currentTime + SECONDS_IN_DAY

        return hourlyForecast
            .filter { weather ->
                weather.dateTime in currentTime..endOfDay
            }.take(MAX_HOURLY_FORECAST_ITEMS)
            .map { hourlyWeather ->
                mapToHourlyWeatherUiModel(hourlyWeather, timezoneId)
            }
    }

    private fun mapToHourlyWeatherUiModel(hourlyWeather: HourlyWeather, timezoneId: String): HourlyWeatherUiModel =
        HourlyWeatherUiModel(
            formattedTime = formatTime(hourlyWeather.dateTime, timezoneId),
            temperature = "${hourlyWeather.temperature.toInt()}°",
            iconUrl = createIconUrl(hourlyWeather.condition.iconCode, IconSize.MEDIUM),
            iconDescription = hourlyWeather.condition.description,
            precipitationProbability = "${hourlyWeather.probabilityOfPrecipitation.toInt()}$PERCENTAGE_SYMBOL",
        )

    private fun formatTime(timestamp: Long, timezoneId: String): String {
        val instant = Instant.fromEpochSeconds(timestamp)
        val timezone = TimeZone.of(timezoneId)
        val localDateTime = instant.toLocalDateTime(timezone)
        return String.format("%02d:%02d", localDateTime.hour, localDateTime.minute)
    }

    private fun createIconUrl(iconCode: String, size: IconSize): String = "$ICON_BASE_URL$iconCode${size.suffix}"

    private fun mapWeeklyForecast(
        dailyForecast: List<DailyWeather>,
        units: String,
        timezoneId: String,
    ): List<DailyWeatherUiModel> =
        dailyForecast
            .take(MAX_DAILY_FORECAST_ITEMS)
            .map { dailyWeather ->
                mapToDailyWeatherUiModel(dailyWeather, units, timezoneId)
            }

    private fun mapToDailyWeatherUiModel(
        dailyWeather: DailyWeather,
        units: String,
        timezoneId: String,
    ): DailyWeatherUiModel =
        DailyWeatherUiModel(
            formattedDay = formatDay(dailyWeather.dateTime, timezoneId),
            temperatureHigh = formatTemperature(dailyWeather.temperatureHigh, units),
            temperatureLow = formatTemperature(dailyWeather.temperatureLow, units),
            iconUrl = createIconUrl(dailyWeather.condition.iconCode, IconSize.MEDIUM),
            iconDescription = dailyWeather.condition.description,
        )

    private fun formatDay(timestamp: Long, timezoneId: String): String {
        val instant = Instant.fromEpochSeconds(timestamp)
        val timezone = TimeZone.of(timezoneId)
        val localDateTime = instant.toLocalDateTime(timezone)
        return localDateTime.dayOfWeek.name
            .lowercase()
            .replaceFirstChar { it.uppercase() }
    }
}

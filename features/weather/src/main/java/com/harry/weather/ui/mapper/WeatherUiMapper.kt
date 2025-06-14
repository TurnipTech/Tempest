package com.harry.weather.ui.mapper

import com.harry.weather.domain.model.HourlyWeather
import com.harry.weather.domain.model.TimeOfDay
import com.harry.weather.domain.model.WeatherData
import com.harry.weather.ui.model.HourlyWeatherUiModel
import com.harry.weather.ui.model.WeatherUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Temperature constants
private const val TEMPERATURE_NOT_AVAILABLE = "N/A"

// Unit type constants
private const val IMPERIAL_UNIT = "imperial"
private const val STANDARD_UNIT = "standard"
private const val METRIC_UNIT = "metric"

// Text constants
private const val NO_DATA_AVAILABLE = "No data available"
private const val UPDATED_PREFIX = "Updated "
private const val PERCENTAGE_SYMBOL = "%"

// Time and date constants
private const val TIME_FORMAT_PATTERN = "HH:mm"
private const val SECONDS_IN_DAY = 24 * 60 * 60
private const val MILLISECONDS_CONVERSION_FACTOR = 1000L

// Forecast constants
private const val MAX_HOURLY_FORECAST_ITEMS = 12

// Icon URL constants
private const val ICON_BASE_URL = "https://openweathermap.org/img/wn/"
private const val ICON_SIZE_SUFFIX = "@2x.png"

class WeatherUiMapper {
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

    fun mapToSuccessState(weatherData: WeatherData, units: String): WeatherUiState.Success {
        val currentWeather = weatherData.currentWeather
        val currentTimeSeconds = System.currentTimeMillis() / 1000 // todo - inject time function
        val timeOfDay =
            TimeOfDay.fromSolarData(
                currentTimeSeconds = currentTimeSeconds,
                sunriseSeconds = currentWeather?.sunrise,
                sunsetSeconds = currentWeather?.sunset,
            )

        return WeatherUiState.Success(
            weatherData = weatherData,
            formattedTemperature = formatTemperature(currentWeather?.temperature, units),
            formattedLocation = formatLocation(weatherData.location.latitude, weatherData.location.longitude),
            weatherDescription = formatWeatherDescription(currentWeather?.condition?.description),
            lastUpdated = formatLastUpdated(),
            todaysHourlyForecast = mapTodaysHourlyForecast(weatherData.hourlyForecast ?: emptyList()),
            timeOfDay = timeOfDay,
        )
    }

    private fun formatTemperature(temperature: Double?, units: String): String =
        if (temperature != null) {
            val temperatureUnit = TemperatureUnit.fromUnitString(units)
            "${temperature.toInt()}${temperatureUnit.symbol}"
        } else {
            TEMPERATURE_NOT_AVAILABLE
        }

    private fun formatLocation(latitude: Double, longitude: Double): String = "$latitude, $longitude"

    private fun formatWeatherDescription(description: String?): String =
        description?.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase() else it.toString()
        } ?: NO_DATA_AVAILABLE

    private fun formatLastUpdated(): String =
        "$UPDATED_PREFIX${
            SimpleDateFormat(
                TIME_FORMAT_PATTERN,
                Locale.getDefault(),
            ).format(Date())
        }"

    private fun mapTodaysHourlyForecast(hourlyForecast: List<HourlyWeather>): List<HourlyWeatherUiModel> {
        val currentTime = System.currentTimeMillis() / MILLISECONDS_CONVERSION_FACTOR
        val endOfDay = currentTime + SECONDS_IN_DAY

        return hourlyForecast
            .filter { weather ->
                weather.dateTime in currentTime..endOfDay
            }.take(MAX_HOURLY_FORECAST_ITEMS)
            .map { hourlyWeather ->
                mapToHourlyWeatherUiModel(hourlyWeather)
            }
    }

    private fun mapToHourlyWeatherUiModel(hourlyWeather: HourlyWeather): HourlyWeatherUiModel =
        HourlyWeatherUiModel(
            formattedTime = formatTime(hourlyWeather.dateTime),
            temperature = "${hourlyWeather.temperature.toInt()}°",
            iconUrl = createIconUrl(hourlyWeather.condition.iconCode),
            iconDescription = hourlyWeather.condition.description,
            precipitationProbability = "${hourlyWeather.probabilityOfPrecipitation.toInt()}$PERCENTAGE_SYMBOL",
        )

    private fun formatTime(timestamp: Long): String {
        val date = Date(timestamp * MILLISECONDS_CONVERSION_FACTOR)
        val formatter = SimpleDateFormat(TIME_FORMAT_PATTERN, Locale.getDefault())
        return formatter.format(date)
    }

    private fun createIconUrl(iconCode: String): String = "$ICON_BASE_URL$iconCode$ICON_SIZE_SUFFIX"
}

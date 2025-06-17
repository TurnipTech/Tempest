package com.harry.weather.data.mapper

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class WeatherMapperTest {
    @Test
    fun `mapToWeatherData with full data maps correctly`() {
        val weatherResponseDto = WeatherMapperTestDataFactory.createWeatherResponseDto()

        val result = WeatherMapper.mapToWeatherData(weatherResponseDto)

        assertEquals("America/New_York", result.timezone)

        assertNotNull(result.currentWeather)
        val currentWeather = result.currentWeather!!
        assertEquals(20.5, currentWeather.temperature, 0.0001)
        assertEquals("clear sky", currentWeather.condition.description)
        assertEquals("01d", currentWeather.condition.iconCode)

        assertNotNull(result.hourlyForecast)
        assertEquals(1, result.hourlyForecast!!.size)
        val hourlyWeather = result.hourlyForecast!!.first()
        assertEquals(1609462800L, hourlyWeather.dateTime)
        assertEquals(21.0, hourlyWeather.temperature, 0.0001)
        assertEquals(0.15, hourlyWeather.probabilityOfPrecipitation, 0.0001)

        assertNotNull(result.dailyForecast)
        assertEquals(1, result.dailyForecast!!.size)
        val dailyWeather = result.dailyForecast!!.first()
        assertEquals(1609459200L, dailyWeather.dateTime)
        assertEquals(25.0, dailyWeather.temperatureHigh, 0.0001)
        assertEquals(15.0, dailyWeather.temperatureLow, 0.0001)
    }

    @Test
    fun `mapToWeatherData with minimal data maps correctly`() {
        val weatherResponseDto =
            WeatherMapperTestDataFactory.createWeatherResponseDto(
                latitude = 51.5074,
                longitude = -0.1278,
                timezone = "Europe/London",
                timezoneOffset = 0,
                current = null,
                hourly = null,
                daily = null,
                alerts = null,
            )

        val result = WeatherMapper.mapToWeatherData(weatherResponseDto)

        assertEquals("Europe/London", result.timezone)
        assertNull(result.currentWeather)
        assertNull(result.hourlyForecast)
        assertNull(result.dailyForecast)
    }

    @Test
    fun `mapToWeatherData with custom weather condition verifies condition mapping`() {
        val customCondition =
            WeatherMapperTestDataFactory.createWeatherConditionDto(
                id = 500,
                main = "Rain",
                description = "light rain",
                icon = "10d",
            )
        val weatherResponseDto =
            WeatherMapperTestDataFactory.createWeatherResponseDto(
                current =
                    WeatherMapperTestDataFactory.createCurrentWeatherDto(
                        weather = listOf(customCondition),
                    ),
            )

        val result = WeatherMapper.mapToWeatherData(weatherResponseDto)

        assertNotNull(result.currentWeather)
        val condition = result.currentWeather!!.condition
        assertEquals("light rain", condition.description)
        assertEquals("10d", condition.iconCode)
    }

    @Test
    fun `mapToWeatherData with custom alert verifies alert mapping`() {
        val customAlert =
            WeatherMapperTestDataFactory.createAlertDto(
                senderName = "Emergency Management",
                event = "Flood Warning",
                start = 1609459200L,
                end = 1609632000L,
                description = "Flooding possible in low-lying areas due to heavy rainfall",
                tags = listOf("flood", "warning", "rain"),
            )
        val weatherResponseDto =
            WeatherMapperTestDataFactory.createWeatherResponseDto(
                alerts = listOf(customAlert),
            )

        val result = WeatherMapper.mapToWeatherData(weatherResponseDto)
    }

    @Test
    fun `mapToWeatherData with custom current weather verifies current weather mapping`() {
        val customCurrentWeather =
            WeatherMapperTestDataFactory.createCurrentWeatherDto(
                dateTime = 1609459200L,
                temperature = 15.2,
                feelsLike = 13.8,
                humidity = 80,
                pressure = 1008,
                windSpeed = 7.3,
                windDirection = 270,
                uvIndex = 3.2,
                cloudiness = 75,
                visibility = 5000,
                weather =
                    listOf(
                        WeatherMapperTestDataFactory.createWeatherConditionDto(
                            id = 803,
                            main = "Clouds",
                            description = "broken clouds",
                            icon = "04d",
                        ),
                    ),
            )
        val weatherResponseDto =
            WeatherMapperTestDataFactory.createWeatherResponseDto(
                current = customCurrentWeather,
            )

        val result = WeatherMapper.mapToWeatherData(weatherResponseDto)

        assertNotNull(result.currentWeather)
        val currentWeather = result.currentWeather!!
        assertEquals(15.2, currentWeather.temperature, 0.0001)
        assertEquals("broken clouds", currentWeather.condition.description)
        assertEquals("04d", currentWeather.condition.iconCode)
    }

    @Test
    fun `mapToWeatherData with custom hourly weather verifies hourly mapping`() {
        val customHourly =
            WeatherMapperTestDataFactory.createHourlyDto(
                dateTime = 1609466400L,
                temperature = 22.8,
                feelsLike = 24.1,
                humidity = 85,
                pressure = 995,
                windSpeed = 12.5,
                uvIndex = 0.0,
                probabilityOfPrecipitation = 0.95,
                weather =
                    listOf(
                        WeatherMapperTestDataFactory.createWeatherConditionDto(
                            id = 200,
                            main = "Thunderstorm",
                            description = "thunderstorm with light rain",
                            icon = "11d",
                        ),
                    ),
            )
        val weatherResponseDto =
            WeatherMapperTestDataFactory.createWeatherResponseDto(
                hourly = listOf(customHourly),
            )

        val result = WeatherMapper.mapToWeatherData(weatherResponseDto)

        assertNotNull(result.hourlyForecast)
        val hourlyWeather = result.hourlyForecast!!.first()
        assertEquals(1609466400L, hourlyWeather.dateTime)
        assertEquals(22.8, hourlyWeather.temperature, 0.0001)
        assertEquals(0.95, hourlyWeather.probabilityOfPrecipitation, 0.0001)
        assertEquals("thunderstorm with light rain", hourlyWeather.condition.description)
        assertEquals("11d", hourlyWeather.condition.iconCode)
    }

    @Test
    fun `mapToWeatherData with custom daily weather verifies daily mapping`() {
        val customDaily =
            WeatherMapperTestDataFactory.createDailyDto(
                dateTime = 1609545600L,
                temperature =
                    WeatherMapperTestDataFactory.createDailyTemperatureDto(
                        day = 19.5,
                        min = 8.0,
                        max = 23.0,
                        night = 10.5,
                        evening = 17.0,
                        morning = 12.0,
                    ),
                humidity = 72,
                pressure = 1012,
                windSpeed = 4.2,
                uvIndex = 5.8,
                probabilityOfPrecipitation = 0.65,
                weather =
                    listOf(
                        WeatherMapperTestDataFactory.createWeatherConditionDto(
                            id = 804,
                            main = "Clouds",
                            description = "overcast clouds",
                            icon = "04d",
                        ),
                    ),
                summary = "Cloudy day with occasional breaks",
            )
        val weatherResponseDto =
            WeatherMapperTestDataFactory.createWeatherResponseDto(
                daily = listOf(customDaily),
            )

        val result = WeatherMapper.mapToWeatherData(weatherResponseDto)

        assertNotNull(result.dailyForecast)
        val dailyWeather = result.dailyForecast!!.first()
        assertEquals(1609545600L, dailyWeather.dateTime)
        assertEquals(23.0, dailyWeather.temperatureHigh, 0.0001)
        assertEquals(8.0, dailyWeather.temperatureLow, 0.0001)
        assertEquals("overcast clouds", dailyWeather.condition.description)
        assertEquals("04d", dailyWeather.condition.iconCode)
    }

    @Test
    fun `mapToWeatherData with empty lists maps correctly`() {
        val weatherResponseDto =
            WeatherMapperTestDataFactory.createWeatherResponseDto(
                latitude = 0.0,
                longitude = 0.0,
                timezone = "UTC",
                timezoneOffset = 0,
                current = null,
                hourly = emptyList(),
                daily = emptyList(),
                alerts = emptyList(),
            )

        val result = WeatherMapper.mapToWeatherData(weatherResponseDto)

        assertEquals("UTC", result.timezone)
        assertNull(result.currentWeather)
        assertNotNull(result.hourlyForecast)
        assertEquals(0, result.hourlyForecast!!.size)
        assertNotNull(result.dailyForecast)
        assertEquals(0, result.dailyForecast!!.size)
    }

    @Test
    fun `mapToWeatherData with multiple hourly and daily entries maps correctly`() {
        val hourly1 =
            WeatherMapperTestDataFactory.createHourlyDto(
                dateTime = 1609459200L,
                temperature = 16.0,
                humidity = 80,
                probabilityOfPrecipitation = 0.8,
            )

        val hourly2 =
            WeatherMapperTestDataFactory.createHourlyDto(
                dateTime = 1609462800L,
                temperature = 17.5,
                humidity = 75,
                probabilityOfPrecipitation = 0.6,
            )

        val daily1 =
            WeatherMapperTestDataFactory.createDailyDto(
                dateTime = 1609459200L,
                temperature =
                    WeatherMapperTestDataFactory.createDailyTemperatureDto(
                        min = 12.0,
                        max = 22.0,
                    ),
                humidity = 78,
                summary = "Rainy day with scattered showers",
            )

        val daily2 =
            WeatherMapperTestDataFactory.createDailyDto(
                dateTime = 1609545600L,
                temperature =
                    WeatherMapperTestDataFactory.createDailyTemperatureDto(
                        min = 14.0,
                        max = 24.0,
                    ),
                humidity = 65,
                summary = "Improving conditions",
            )

        val weatherResponseDto =
            WeatherMapperTestDataFactory.createWeatherResponseDto(
                latitude = 52.5200,
                longitude = 13.4050,
                timezone = "Europe/Berlin",
                timezoneOffset = 3600,
                current = null,
                hourly = listOf(hourly1, hourly2),
                daily = listOf(daily1, daily2),
                alerts = null,
            )

        val result = WeatherMapper.mapToWeatherData(weatherResponseDto)

        assertEquals("Europe/Berlin", result.timezone)

        assertNull(result.currentWeather)

        assertNotNull(result.hourlyForecast)
        result.hourlyForecast?.let { hourlyForecast ->
            assertEquals(2, hourlyForecast.size)

            val firstHourly = hourlyForecast[0]
            assertEquals(1609459200L, firstHourly.dateTime)
            assertEquals(16.0, firstHourly.temperature, 0.0001)
            assertEquals(0.8, firstHourly.probabilityOfPrecipitation, 0.0001)

            val secondHourly = hourlyForecast[1]
            assertEquals(1609462800L, secondHourly.dateTime)
            assertEquals(17.5, secondHourly.temperature, 0.0001)
            assertEquals(0.6, secondHourly.probabilityOfPrecipitation, 0.0001)
        }

        assertNotNull(result.dailyForecast)
        result.dailyForecast?.let { dailyForecast ->
            assertEquals(2, dailyForecast.size)

            val firstDaily = dailyForecast[0]
            assertEquals(1609459200L, firstDaily.dateTime)
            assertEquals(22.0, firstDaily.temperatureHigh, 0.0001)
            assertEquals(12.0, firstDaily.temperatureLow, 0.0001)

            val secondDaily = dailyForecast[1]
            assertEquals(1609545600L, secondDaily.dateTime)
            assertEquals(24.0, secondDaily.temperatureHigh, 0.0001)
            assertEquals(14.0, secondDaily.temperatureLow, 0.0001)
        }
    }

    @Test
    fun `mapToWeatherData with multiple alerts maps correctly`() {
        val alert1 =
            WeatherMapperTestDataFactory.createAlertDto(
                senderName = "Weather Service",
                event = "High Wind Warning",
                start = 1609459200L,
                end = 1609488000L,
                description = "Strong winds expected",
                tags = listOf("wind", "warning"),
            )

        val alert2 =
            WeatherMapperTestDataFactory.createAlertDto(
                senderName = "Emergency Management",
                event = "Heat Advisory",
                start = 1609545600L,
                end = 1609632000L,
                description = "Dangerous heat conditions",
                tags = listOf("heat", "advisory"),
            )

        val weatherResponseDto =
            WeatherMapperTestDataFactory.createWeatherResponseDto(
                latitude = 25.7617,
                longitude = -80.1918,
                timezone = "America/New_York",
                timezoneOffset = -18000,
                current = null,
                hourly = null,
                daily = null,
                alerts = listOf(alert1, alert2),
            )

        val result = WeatherMapper.mapToWeatherData(weatherResponseDto)

        assertEquals("America/New_York", result.timezone)

        assertNull(result.currentWeather)
        assertNull(result.hourlyForecast)
        assertNull(result.dailyForecast)
    }
}

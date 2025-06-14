package com.harry.weather.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class TimeOfDayTest {
    @Test
    fun `fromSolarData returns DAY when sunrise and sunset are null`() {
        val result =
            TimeOfDay.fromSolarData(
                currentTimeSeconds = 1640700000L, // Any time
                sunriseSeconds = null,
                sunsetSeconds = null,
            )

        assertEquals(TimeOfDay.DAY, result)
    }

    @Test
    fun `fromSolarData returns DAY when sunrise is null`() {
        val result =
            TimeOfDay.fromSolarData(
                currentTimeSeconds = 1640700000L,
                sunriseSeconds = null,
                sunsetSeconds = 1640714400L,
            )

        assertEquals(TimeOfDay.DAY, result)
    }

    @Test
    fun `fromSolarData returns DAY when sunset is null`() {
        val result =
            TimeOfDay.fromSolarData(
                currentTimeSeconds = 1640700000L,
                sunriseSeconds = 1640678400L,
                sunsetSeconds = null,
            )

        assertEquals(TimeOfDay.DAY, result)
    }

    @Test
    fun `fromSolarData returns DAWN during dawn transition`() {
        // Sunrise at 6:00 AM (6 * 3600 = 21600 seconds since midnight)
        val sunriseSeconds = 21600L
        // Sunset at 6:00 PM (18 * 3600 = 64800 seconds since midnight)
        val sunsetSeconds = 64800L
        // Current time at 6:15 AM (sunrise + 15 minutes)
        val currentTimeSeconds = 22500L // 6.25 hours * 3600

        val result =
            TimeOfDay.fromSolarData(
                currentTimeSeconds = currentTimeSeconds,
                sunriseSeconds = sunriseSeconds,
                sunsetSeconds = sunsetSeconds,
            )

        assertEquals(TimeOfDay.DAWN, result)
    }

    @Test
    fun `fromSolarData returns DUSK during dusk transition`() {
        // Sunrise at 6:00 AM
        val sunriseSeconds = 21600L
        // Sunset at 6:00 PM
        val sunsetSeconds = 64800L
        // Current time at 6:15 PM (sunset + 15 minutes)
        val currentTimeSeconds = 65700L // 18.25 hours * 3600

        val result =
            TimeOfDay.fromSolarData(
                currentTimeSeconds = currentTimeSeconds,
                sunriseSeconds = sunriseSeconds,
                sunsetSeconds = sunsetSeconds,
            )

        assertEquals(TimeOfDay.DUSK, result)
    }

    @Test
    fun `fromSolarData returns DAY during midday`() {
        // Sunrise at 6:00 AM
        val sunriseSeconds = 21600L
        // Sunset at 6:00 PM
        val sunsetSeconds = 64800L
        // Current time at 12:00 PM (noon)
        val currentTimeSeconds = 43200L // 12 hours * 3600

        val result =
            TimeOfDay.fromSolarData(
                currentTimeSeconds = currentTimeSeconds,
                sunriseSeconds = sunriseSeconds,
                sunsetSeconds = sunsetSeconds,
            )

        assertEquals(TimeOfDay.DAY, result)
    }

    @Test
    fun `fromSolarData returns NIGHT during midnight`() {
        // Sunrise at 6:00 AM
        val sunriseSeconds = 21600L
        // Sunset at 6:00 PM
        val sunsetSeconds = 64800L
        // Current time at 12:00 AM (midnight)
        val currentTimeSeconds = 0L

        val result =
            TimeOfDay.fromSolarData(
                currentTimeSeconds = currentTimeSeconds,
                sunriseSeconds = sunriseSeconds,
                sunsetSeconds = sunsetSeconds,
            )

        assertEquals(TimeOfDay.NIGHT, result)
    }

    @Test
    fun `fromSolarData returns NIGHT during late evening`() {
        // Sunrise at 6:00 AM
        val sunriseSeconds = 21600L
        // Sunset at 6:00 PM
        val sunsetSeconds = 64800L
        // Current time at 10:00 PM (22:00)
        val currentTimeSeconds = 79200L // 22 hours * 3600

        val result =
            TimeOfDay.fromSolarData(
                currentTimeSeconds = currentTimeSeconds,
                sunriseSeconds = sunriseSeconds,
                sunsetSeconds = sunsetSeconds,
            )

        assertEquals(TimeOfDay.NIGHT, result)
    }
}

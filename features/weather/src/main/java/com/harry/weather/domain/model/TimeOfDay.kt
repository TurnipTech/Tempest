package com.harry.weather.domain.model

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Represents different periods of the day based on solar position and time.
 */
enum class TimeOfDay {
    NIGHT,
    DAWN,
    DAY,
    DUSK,
    ;

    companion object {
        /**
         * Calculates the time of day based on current time and sunrise/sunset data.
         *
         * @param currentTime Current time as Instant
         * @param sunrise Sunrise time as Instant, null if unknown
         * @param sunset Sunset time as Instant, null if unknown
         * @param timeZone TimeZone for calculations (defaults to system timezone)
         * @param transitionDurationMinutes Duration of dawn/dusk transitions in minutes (default: 30)
         * @return TimeOfDay enum representing the current period
         */
        fun fromSolarData(
            currentTime: Instant,
            sunrise: Instant?,
            sunset: Instant?,
            timeZone: TimeZone = TimeZone.currentSystemDefault(),
            transitionDurationMinutes: Int = 30,
        ): TimeOfDay {
            // Fallback to day if sunrise/sunset data is unavailable
            if (sunrise == null || sunset == null) {
                return DAY
            }

            // Convert all times to local date/time in the specified timezone
            val localNow = currentTime.toLocalDateTime(timeZone)
            val localSunrise = sunrise.toLocalDateTime(timeZone)
            val localSunset = sunset.toLocalDateTime(timeZone)

            // Convert to hours since midnight for easier calculation
            val currentHour = localNow.hour + localNow.minute / 60.0f
            val sunriseHour = localSunrise.hour + localSunrise.minute / 60.0f
            val sunsetHour = localSunset.hour + localSunset.minute / 60.0f

            // Convert transition duration to hours
            val transitionDuration = transitionDurationMinutes / 60.0f

            return when {
                // Dawn transition (before sunrise to after sunrise)
                currentHour >= sunriseHour - transitionDuration &&
                    currentHour <= sunriseHour + transitionDuration -> DAWN

                // Dusk transition (before sunset to after sunset)
                currentHour >= sunsetHour - transitionDuration &&
                    currentHour <= sunsetHour + transitionDuration -> DUSK

                // Day time (between dawn and dusk transitions)
                currentHour > sunriseHour + transitionDuration &&
                    currentHour < sunsetHour - transitionDuration -> DAY

                // Night time (all other times)
                else -> NIGHT
            }
        }
    }
}

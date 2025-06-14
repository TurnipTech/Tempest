package com.harry.weather.domain.model

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
         * @param currentTimeSeconds Current time in seconds (Unix timestamp)
         * @param sunriseSeconds Sunrise time in seconds (Unix timestamp), null if unknown
         * @param sunsetSeconds Sunset time in seconds (Unix timestamp), null if unknown
         * @param transitionDurationMinutes Duration of dawn/dusk transitions in minutes (default: 30)
         * @return TimeOfDay enum representing the current period
         */
        fun fromSolarData(
            currentTimeSeconds: Long,
            sunriseSeconds: Long?,
            sunsetSeconds: Long?,
            transitionDurationMinutes: Int = 30,
        ): TimeOfDay {
            // Fallback to day if sunrise/sunset data is unavailable
            if (sunriseSeconds == null || sunsetSeconds == null) {
                return DAY
            }

            // Convert to hours since midnight for easier calculation
            val currentHour = ((currentTimeSeconds % (24 * 60 * 60)) / (60 * 60)).toFloat()
            val sunriseHour = ((sunriseSeconds % (24 * 60 * 60)) / (60 * 60)).toFloat()
            val sunsetHour = ((sunsetSeconds % (24 * 60 * 60)) / (60 * 60)).toFloat()

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

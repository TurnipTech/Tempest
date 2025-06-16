package com.harry.location.domain.model

// todo - this should be in the main app, NOT in search_location module
sealed interface StartDestination {
    data object SearchLocation : StartDestination

    data class Weather(
        val location: Location,
    ) : StartDestination
}

package com.harry.location.domain.model

sealed interface StartDestination {
    data object SearchLocation : StartDestination

    data class Weather(
        val location: Location,
    ) : StartDestination
}

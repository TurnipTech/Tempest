package com.harry.tempest.navigation

import com.harry.location.domain.model.Location

sealed interface StartDestination {
    data object SearchLocation : StartDestination

    data class Weather(
        val location: Location,
    ) : StartDestination
}

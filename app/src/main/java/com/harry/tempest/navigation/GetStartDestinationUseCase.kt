package com.harry.tempest.navigation

import com.harry.location.domain.repository.LocationRepository

class GetStartDestinationUseCase(
    private val locationRepository: LocationRepository,
) {
    suspend operator fun invoke(): StartDestination {
        val storedLocation = locationRepository.getStoredLocation()
        return when {
            storedLocation != null -> StartDestination.Weather(storedLocation)
            else -> StartDestination.SearchLocation
        }
    }
}

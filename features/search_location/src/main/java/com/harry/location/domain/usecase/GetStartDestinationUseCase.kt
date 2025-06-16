package com.harry.location.domain.usecase

import com.harry.location.domain.model.StartDestination
import com.harry.location.domain.repository.LocationRepository

class GetStartDestinationUseCase(
    private val locationRepository: LocationRepository,
) {
    suspend operator fun invoke(): StartDestination {
        val storedLocation = locationRepository.getStoredLocation()
        return if (storedLocation != null) {
            StartDestination.Weather(storedLocation)
        } else {
            StartDestination.SearchLocation
        }
    }
}

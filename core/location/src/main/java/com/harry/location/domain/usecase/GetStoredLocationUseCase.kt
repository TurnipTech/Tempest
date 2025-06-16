package com.harry.location.domain.usecase

import com.harry.location.domain.model.Location
import com.harry.location.domain.repository.LocationRepository

class GetStoredLocationUseCase(
    private val locationRepository: LocationRepository,
) {
    suspend operator fun invoke(): Location? = locationRepository.getStoredLocation()
}

package com.harry.location.domain.usecase

import com.harry.location.domain.model.Location
import com.harry.location.domain.repository.LocationRepository

class SetLocationUseCase(
    private val locationRepository: LocationRepository,
) {
    suspend operator fun invoke(location: Location): Result<Unit> =
        try {
            locationRepository.setLocation(location)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
}

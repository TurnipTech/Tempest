package com.harry.location.domain.usecase

import com.harry.location.domain.model.LocationSearchResult
import com.harry.location.domain.repository.LocationRepository

class SearchLocationsUseCase(
    private val locationRepository: LocationRepository,
) {
    suspend operator fun invoke(query: String): Result<LocationSearchResult> = locationRepository.searchLocations(query)
}

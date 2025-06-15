package com.harry.location.data.mapper

import com.harry.location.data.dto.GeocodingLocationDto
import com.harry.location.domain.model.Location
import com.harry.location.domain.model.LocationSearchResult

object LocationMapper {
    fun mapToLocationSearchResult(dtos: List<GeocodingLocationDto>, query: String): LocationSearchResult =
        LocationSearchResult(
            locations = dtos.map { dto -> mapToLocation(dto) },
            query = query,
        )

    private fun mapToLocation(dto: GeocodingLocationDto): Location =
        Location(
            name = dto.name,
            localNames = dto.localNames,
            latitude = dto.latitude,
            longitude = dto.longitude,
            country = dto.country,
            state = dto.state,
        )
}

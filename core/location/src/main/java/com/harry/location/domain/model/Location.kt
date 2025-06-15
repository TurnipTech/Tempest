package com.harry.location.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val name: String,
    val localNames: Map<String, String> = emptyMap(),
    val latitude: Double,
    val longitude: Double,
    val country: String,
    val state: String? = null,
)

data class LocationSearchResult(
    val locations: List<Location>,
    val query: String,
)

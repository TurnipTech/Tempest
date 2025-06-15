package com.harry.location.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeocodingLocationDto(
    @SerialName("name") val name: String,
    @SerialName("local_names") val localNames: Map<String, String> = emptyMap(),
    @SerialName("lat") val latitude: Double,
    @SerialName("lon") val longitude: Double,
    @SerialName("country") val country: String,
    @SerialName("state") val state: String? = null,
)

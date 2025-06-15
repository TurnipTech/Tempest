package com.harry.weather

import kotlinx.serialization.Serializable

@Serializable
data class WeatherRoute(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String,
    val state: String? = null,
)

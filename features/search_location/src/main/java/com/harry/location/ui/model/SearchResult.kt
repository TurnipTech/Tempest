package com.harry.location.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class SearchResult(
    val displayName: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String,
    val state: String? = null,
    val localNames: Map<String, String> = emptyMap(),
)

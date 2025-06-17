package com.harry.location

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.harry.location.domain.model.Location
import com.harry.location.ui.SearchLocationScreen
import kotlinx.serialization.Serializable

@Serializable
object SearchLocationDestination

fun NavGraphBuilder.addSearchLocation(onNavigateToWeather: (Location) -> Unit) {
    composable<SearchLocationDestination> {
        SearchLocationScreen(onNavigateToWeather = onNavigateToWeather)
    }
}

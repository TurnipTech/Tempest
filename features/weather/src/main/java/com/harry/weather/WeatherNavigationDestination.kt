package com.harry.weather

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.harry.location.domain.model.Location
import com.harry.weather.ui.WeatherScreen
import com.harry.weather.ui.WeatherViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
data class WeatherDestination(
    val name: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val country: String? = null,
    val state: String? = null,
)

fun NavGraphBuilder.addWeatherDestination(onNavigateToSearch: () -> Unit) {
    composable<WeatherDestination> { backStackEntry ->
        val weatherDestination: WeatherDestination = backStackEntry.toRoute()
        val viewModel: WeatherViewModel = koinViewModel { parametersOf(weatherDestination.toLocation()) }
        WeatherScreen(
            viewModel = viewModel,
            onNavigateToSearch = onNavigateToSearch,
        )
    }
}

private fun WeatherDestination.toLocation(): Location? {
    val safeName = name ?: return null
    val safeLat = latitude ?: return null
    val safeLng = longitude ?: return null
    val safeCountry = country ?: return null
    val safeState = state ?: return null

    return Location(
        name = safeName,
        latitude = safeLat,
        longitude = safeLng,
        country = safeCountry,
        state = safeState,
    )
}

fun Location.toWeatherDestination(): WeatherDestination =
    WeatherDestination(
        name = name,
        latitude = latitude,
        longitude = longitude,
        country = country,
        state = state,
    )

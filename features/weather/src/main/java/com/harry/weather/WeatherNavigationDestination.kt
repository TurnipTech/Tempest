package com.harry.weather

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.harry.location.domain.model.Location
import com.harry.navigation.NavigationDestination
import com.harry.navigation.composableDestination
import com.harry.weather.ui.WeatherScreen
import com.harry.weather.ui.WeatherViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

class WeatherNavigationDestination : NavigationDestination {
    override val route: String
        get() = "weather"

    override fun NavGraphBuilder.graph() {
        graph(location = null)
    }

    fun NavGraphBuilder.graph(location: Location?, onNavigateToSearch: () -> Unit = {}) {
        composable<WeatherRoute> {
            val route = it.toRoute<WeatherRoute>()
            val routeLocation =
                Location(
                    name = route.name,
                    latitude = route.latitude,
                    longitude = route.longitude,
                    country = route.country,
                    state = route.state,
                )
            val viewModel: WeatherViewModel = koinViewModel { parametersOf(routeLocation) }
            WeatherScreen(
                viewModel = viewModel,
                onNavigateToSearch = onNavigateToSearch,
            )
        }

        // Add a simple route for initial navigation without parameters
        composableDestination(this@WeatherNavigationDestination.route) {
            val viewModel: WeatherViewModel = koinViewModel { parametersOf(location) }
            WeatherScreen(
                viewModel = viewModel,
                onNavigateToSearch = onNavigateToSearch,
            )
        }
    }
}

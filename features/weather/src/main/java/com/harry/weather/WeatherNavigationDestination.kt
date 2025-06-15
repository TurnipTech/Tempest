package com.harry.weather

import androidx.navigation.NavGraphBuilder
import com.harry.location.domain.model.Location
import com.harry.navigation.NavigationDestination
import com.harry.navigation.composableDestination
import com.harry.weather.ui.WeatherScreen
import com.harry.weather.ui.WeatherViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

private const val WEATHER_ROUTE = "weather"

class WeatherNavigationDestination : NavigationDestination {
    override val route: String
        get() = WEATHER_ROUTE

    override fun NavGraphBuilder.graph() {
        graph(location = null)
    }

    fun NavGraphBuilder.graph(location: Location?) {
        composableDestination(this@WeatherNavigationDestination.route) {
            val viewModel: WeatherViewModel = koinViewModel { parametersOf(location) }
            WeatherScreen(viewModel = viewModel)
        }
    }
}

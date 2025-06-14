package com.harry.weather

import androidx.navigation.NavGraphBuilder
import com.harry.navigation.NavigationDestination
import com.harry.navigation.composableDestination
import com.harry.weather.ui.WeatherScreen

private const val WEATHER_ROUTE = "weather"

class WeatherNavigationDestination : NavigationDestination {
    override val route: String
        get() = WEATHER_ROUTE

    override fun NavGraphBuilder.graph() {
        composableDestination(this@WeatherNavigationDestination.route) {
            WeatherScreen()
        }
    }
}

package com.harry.location

import androidx.navigation.NavGraphBuilder
import com.harry.location.domain.model.Location
import com.harry.location.ui.SearchLocationScreen
import com.harry.navigation.NavigationDestination
import com.harry.navigation.composableDestination

private const val SEARCH_LOCATION_ROUTE = "search_location"

class SearchLocationNavigationDestination : NavigationDestination {
    override val route: String
        get() = SEARCH_LOCATION_ROUTE

    override fun NavGraphBuilder.graph() {
        graph(onNavigateToWeather = {})
    }

    fun NavGraphBuilder.graph(onNavigateToWeather: (Location) -> Unit) {
        composableDestination(this@SearchLocationNavigationDestination.route) {
            SearchLocationScreen(onNavigateToWeather = onNavigateToWeather)
        }
    }
}

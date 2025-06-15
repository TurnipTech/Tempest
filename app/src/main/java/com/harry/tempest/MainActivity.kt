package com.harry.tempest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.harry.location.SearchLocationNavigationDestination
import com.harry.location.domain.model.StartDestination
import com.harry.tempest.ui.theme.TempestTheme
import com.harry.weather.WeatherNavigationDestination
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TempestTheme {
                TempestNavigation()
            }
        }
    }
}

@Composable
fun TempestNavigation() {
    val navController = rememberNavController()
    val weatherDestination = WeatherNavigationDestination()
    val searchLocationDestination = SearchLocationNavigationDestination()
    val viewModel: TempestViewModel = koinViewModel()
    val startDestinationType by viewModel.startDestination.collectAsStateWithLifecycle()

    val startDestination =
        when (startDestinationType) {
            StartDestination.WEATHER -> weatherDestination.route
            StartDestination.SEARCH_LOCATION -> searchLocationDestination.route
            null -> searchLocationDestination.route
        }

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        with(weatherDestination) {
            graph()
        }
        with(searchLocationDestination) {
            graph(onNavigateToWeather = {
                navController.navigate(weatherDestination.route)
            })
        }
    }
}

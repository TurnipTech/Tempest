package com.harry.tempest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.harry.design.TempestTheme
import com.harry.location.SearchLocationNavigationDestination
import com.harry.location.domain.model.Location
import com.harry.tempest.navigation.StartDestination
import com.harry.weather.WeatherNavigationDestination
import com.harry.weather.WeatherRoute
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
fun TempestNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val weatherDestination = WeatherNavigationDestination()
    val searchLocationDestination = SearchLocationNavigationDestination()
    val viewModel: TempestViewModel = koinViewModel()
    val startDestinationType by viewModel.startDestination.collectAsStateWithLifecycle()

    when (startDestinationType) {
        null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
        is StartDestination.SearchLocation -> {
            TempestNavHost(
                navController = navController,
                startDestination = searchLocationDestination.route,
                weatherDestination = weatherDestination,
                searchLocationDestination = searchLocationDestination,
                initialLocation = null,
            )
        }
        is StartDestination.Weather -> {
            TempestNavHost(
                navController = navController,
                startDestination = weatherDestination.route,
                weatherDestination = weatherDestination,
                searchLocationDestination = searchLocationDestination,
                initialLocation = (startDestinationType as StartDestination.Weather).location,
            )
        }
    }
}

@Composable
private fun TempestNavHost(
    navController: NavHostController,
    startDestination: String,
    weatherDestination: WeatherNavigationDestination,
    searchLocationDestination: SearchLocationNavigationDestination,
    initialLocation: Location?,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        with(weatherDestination) {
            graph(
                location = initialLocation,
                onNavigateToSearch = {
                    navController.navigate(searchLocationDestination.route)
                },
            )
        }
        with(searchLocationDestination) {
            graph(onNavigateToWeather = { location ->
                val weatherRoute =
                    WeatherRoute(
                        name = location.name,
                        latitude = location.latitude,
                        longitude = location.longitude,
                        country = location.country,
                        state = location.state,
                    )
                navController.navigate(weatherRoute) {
                    popUpTo(weatherDestination.route) {
                        inclusive = true
                    }
                }
            })
        }
    }
}

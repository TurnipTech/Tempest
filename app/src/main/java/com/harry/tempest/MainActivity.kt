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
        else -> {
            val startDestination =
                when (startDestinationType) {
                    is StartDestination.Weather -> weatherDestination.route
                    is StartDestination.SearchLocation -> searchLocationDestination.route
                    null -> searchLocationDestination.route // This case won't be reached due to outer when
                }

            NavHost(
                navController = navController,
                startDestination = startDestination,
            ) {
                with(weatherDestination) {
                    val location =
                        when (val destination = startDestinationType) {
                            is StartDestination.Weather -> destination.location
                            else -> null
                        }
                    graph(location = location)
                }
                with(searchLocationDestination) {
                    graph(onNavigateToWeather = { location ->
                        navController.navigate(weatherDestination.route)
                    })
                }
            }
        }
    }
}

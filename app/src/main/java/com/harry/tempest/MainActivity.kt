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
import com.harry.location.SearchLocationDestination
import com.harry.location.addSearchLocation
import com.harry.tempest.navigation.StartDestination
import com.harry.weather.WeatherDestination
import com.harry.weather.addWeatherDestination
import com.harry.weather.toWeatherDestination
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
                startDestination = SearchLocationDestination,
            )
        }

        is StartDestination.Weather -> {
            TempestNavHost(
                navController = navController,
                startDestination = WeatherDestination(),
            )
        }
    }
}

@Composable
private fun TempestNavHost(navController: NavHostController, startDestination: Any) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        addWeatherDestination {
            navController.navigate(SearchLocationDestination)
        }
        addSearchLocation { location ->
            navController.navigate(
                location.toWeatherDestination(),
            ) {
                popUpTo<WeatherDestination> {
                    inclusive = true
                }
            }
        }
    }
}

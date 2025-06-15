package com.harry.tempest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.harry.location.SearchLocationNavigationDestination
import com.harry.tempest.ui.theme.TempestTheme
import com.harry.weather.WeatherNavigationDestination

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

    NavHost(
        navController = navController,
        startDestination = weatherDestination.route,
    ) {
        with(weatherDestination) {
            graph()
        }
        with(searchLocationDestination) {
            graph()
        }
    }
}

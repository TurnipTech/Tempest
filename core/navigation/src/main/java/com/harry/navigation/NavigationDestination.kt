package com.harry.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

interface NavigationDestination {
    /**
     * The route for this destination
     */
    val route: String

    /**
     * Register this destination in the navigation graph
     */
    fun NavGraphBuilder.graph()
}

fun NavGraphBuilder.composableDestination(route: String, content: @Composable () -> Unit) {
    composable(route) {
        content()
    }
}

package com.harry.weather.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.harry.weather.domain.model.TimeOfDay

@Composable
fun DynamicWeatherBackground(timeOfDay: TimeOfDay, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val backgroundBrush = getBackgroundBrush(timeOfDay)
    val atmosphericOverlay = getAtmosphericOverlay(timeOfDay)

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(brush = backgroundBrush),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .alpha(0.4f)
                    .background(brush = atmosphericOverlay),
        )

        content()
    }
}

private fun getBackgroundBrush(timeOfDay: TimeOfDay): Brush =
    when (timeOfDay) {
        TimeOfDay.NIGHT -> createNightBrush()
        TimeOfDay.DAWN -> createDawnBrush()
        TimeOfDay.DAY -> createDayBrush()
        TimeOfDay.DUSK -> createDuskBrush()
    }

private fun createDawnBrush(): Brush =
    Brush.verticalGradient(
        colorStops =
            arrayOf(
                0.0f to Color(0xFF1a1a2e), // Dark pre-dawn sky
                0.3f to Color(0xFF16213e), // Deep blue
                0.6f to Color(0xFFffa726), // Warm orange horizon
                0.8f to Color(0xFFffcc80), // Golden light
                1.0f to Color(0xFFfff3e0), // Soft cream base
            ),
    )

private fun createDuskBrush(): Brush =
    Brush.verticalGradient(
        colorStops =
            arrayOf(
                0.0f to Color(0xFF0d1421), // Dark evening sky
                0.2f to Color(0xFF1a1a2e), // Deep blue-purple
                0.5f to Color(0xFF3d2459), // Purple twilight
                0.7f to Color(0xFFff7043), // Deep orange horizon
                0.85f to Color(0xFFffab40), // Golden sunset
                1.0f to Color(0xFFfff8e1), // Warm cream base
            ),
    )

private fun createDayBrush(): Brush =
    Brush.verticalGradient(
        colorStops =
            arrayOf(
                0.0f to Color(0xFF0d47a1), // Deep sky blue
                0.3f to Color(0xFF1976d2), // Rich blue
                0.6f to Color(0xFF42a5f5), // Bright sky blue
                0.8f to Color(0xFF81d4fa), // Light blue
                1.0f to Color(0xFF90caf9), // Medium blue horizon
            ),
    )

private fun createNightBrush(): Brush =
    Brush.verticalGradient(
        colorStops =
            arrayOf(
                0.0f to Color(0xFF0a0a0f), // Deep space black
                0.2f to Color(0xFF0d1421), // Dark night blue
                0.5f to Color(0xFF1a1a2e), // Rich navy
                0.8f to Color(0xFF16213e), // Lighter navy
                1.0f to Color(0xFF263238), // Subtle blue-grey base
            ),
    )

private fun getAtmosphericOverlay(timeOfDay: TimeOfDay): Brush =
    when (timeOfDay) {
        TimeOfDay.NIGHT -> createNightOverlay()
        TimeOfDay.DAWN -> createDawnOverlay()
        TimeOfDay.DAY -> createDayOverlay()
        TimeOfDay.DUSK -> createDuskOverlay()
    }

private fun createDawnOverlay(): Brush =
    Brush.radialGradient(
        colorStops =
            arrayOf(
                0.0f to Color(0x33ffcc80), // Soft golden center
                0.7f to Color(0x1affa726), // Warm orange mid
                1.0f to Color(0x00000000), // Transparent edge
            ),
        radius = 800f,
    )

private fun createDuskOverlay(): Brush =
    Brush.radialGradient(
        colorStops =
            arrayOf(
                0.0f to Color(0x33ff8a65), // Soft coral center
                0.6f to Color(0x1aff7043), // Orange mid
                1.0f to Color(0x00000000), // Transparent edge
            ),
        radius = 900f,
    )

private fun createDayOverlay(): Brush =
    Brush.radialGradient(
        colorStops =
            arrayOf(
                0.0f to Color(0x10bbdefb), // Subtle blue center
                0.8f to Color(0x0d81d4fa), // Subtle blue mid
                1.0f to Color(0x00000000), // Transparent edge
            ),
        radius = 1000f,
    )

private fun createNightOverlay(): Brush =
    Brush.radialGradient(
        colorStops =
            arrayOf(
                0.0f to Color(0x1a3f51b5), // Subtle indigo center
                0.7f to Color(0x0d1a1a2e), // Dark blue mid
                1.0f to Color(0x00000000), // Transparent edge
            ),
        radius = 700f,
    )

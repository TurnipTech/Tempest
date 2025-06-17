package com.harry.weather.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import com.harry.design.WeatherGradients
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
                0.0f to WeatherGradients.Dawn.skyDark,
                0.3f to WeatherGradients.Dawn.skyDeep,
                0.6f to WeatherGradients.Dawn.horizonWarm,
                0.8f to WeatherGradients.Dawn.lightGolden,
                1.0f to WeatherGradients.Dawn.baseCream,
            ),
    )

private fun createDuskBrush(): Brush =
    Brush.verticalGradient(
        colorStops =
            arrayOf(
                0.0f to WeatherGradients.Dusk.skyEvening,
                0.2f to WeatherGradients.Dusk.skyBluePurple,
                0.5f to WeatherGradients.Dusk.skyTwilight,
                0.7f to WeatherGradients.Dusk.horizonDeep,
                0.85f to WeatherGradients.Dusk.sunsetGolden,
                1.0f to WeatherGradients.Dusk.baseWarm,
            ),
    )

private fun createDayBrush(): Brush =
    Brush.verticalGradient(
        colorStops =
            arrayOf(
                0.0f to WeatherGradients.Day.skyDeep,
                0.3f to WeatherGradients.Day.skyRich,
                0.6f to WeatherGradients.Day.skyBright,
                0.8f to WeatherGradients.Day.skyLight,
                1.0f to WeatherGradients.Day.horizonMedium,
            ),
    )

private fun createNightBrush(): Brush =
    Brush.verticalGradient(
        colorStops =
            arrayOf(
                0.0f to WeatherGradients.Night.spaceBlack,
                0.2f to WeatherGradients.Night.skyDark,
                0.5f to WeatherGradients.Night.navyRich,
                0.8f to WeatherGradients.Night.navyLight,
                1.0f to WeatherGradients.Night.baseGrey,
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
                0.0f to WeatherGradients.Dawn.overlayGolden,
                0.7f to WeatherGradients.Dawn.overlayOrange,
                1.0f to WeatherGradients.Dawn.overlayTransparent,
            ),
        radius = 800f,
    )

private fun createDuskOverlay(): Brush =
    Brush.radialGradient(
        colorStops =
            arrayOf(
                0.0f to WeatherGradients.Dusk.overlayCoral,
                0.6f to WeatherGradients.Dusk.overlayOrange,
                1.0f to WeatherGradients.Dusk.overlayTransparent,
            ),
        radius = 900f,
    )

private fun createDayOverlay(): Brush =
    Brush.radialGradient(
        colorStops =
            arrayOf(
                0.0f to WeatherGradients.Day.overlayBlue,
                0.8f to WeatherGradients.Day.overlayBlueMid,
                1.0f to WeatherGradients.Day.overlayTransparent,
            ),
        radius = 1000f,
    )

private fun createNightOverlay(): Brush =
    Brush.radialGradient(
        colorStops =
            arrayOf(
                0.0f to WeatherGradients.Night.overlayIndigo,
                0.7f to WeatherGradients.Night.overlayDark,
                1.0f to WeatherGradients.Night.overlayTransparent,
            ),
        radius = 700f,
    )

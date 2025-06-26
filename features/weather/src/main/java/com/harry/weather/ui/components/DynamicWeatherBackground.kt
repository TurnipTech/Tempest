package com.harry.weather.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import com.harry.design.createDawnBrush
import com.harry.design.createDawnOverlay
import com.harry.design.createDayBrush
import com.harry.design.createDayOverlay
import com.harry.design.createDuskBrush
import com.harry.design.createDuskOverlay
import com.harry.design.createNightBrush
import com.harry.design.createNightOverlay
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

private fun getAtmosphericOverlay(timeOfDay: TimeOfDay): Brush =
    when (timeOfDay) {
        TimeOfDay.NIGHT -> createNightOverlay()
        TimeOfDay.DAWN -> createDawnOverlay()
        TimeOfDay.DAY -> createDayOverlay()
        TimeOfDay.DUSK -> createDuskOverlay()
    }

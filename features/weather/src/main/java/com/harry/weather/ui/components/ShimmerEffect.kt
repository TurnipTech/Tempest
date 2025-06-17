package com.harry.weather.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.harry.design.TempestTheme

@Composable
fun shimmerBrush(showShimmer: Boolean = true): Brush =
    if (showShimmer) {
        val shimmerColors =
            listOf(
                Color.White.copy(alpha = 0.4f),
                Color.White.copy(alpha = 0.8f),
                Color.White.copy(alpha = 0.4f),
            )

        val transition = rememberInfiniteTransition(label = "shimmerTransition")
        val translateAnim by transition.animateFloat(
            initialValue = 0f,
            targetValue = 1000f,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(1200),
                    repeatMode = RepeatMode.Restart,
                ),
            label = "shimmerAnimation",
        )

        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset.Zero,
            end = Offset(x = translateAnim, y = translateAnim),
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color.Transparent, Color.Transparent),
            start = Offset.Zero,
            end = Offset.Zero,
        )
    }

fun Modifier.shimmerEffect(): Modifier =
    composed {
        background(shimmerBrush())
    }

@Composable
fun ShimmerBox(modifier: Modifier = Modifier, shape: Shape = RoundedCornerShape(4.dp)) {
    Box(
        modifier =
            modifier
                .clip(shape)
                .shimmerEffect(),
    )
}

@Composable
fun WeatherLoadingSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .testTag("weather_loading_skeleton"),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Current weather skeleton
        CurrentWeatherSkeleton()

        Spacer(modifier = Modifier.height(64.dp))

        ForecastSkeleton()
    }
}

@Composable
private fun CurrentWeatherSkeleton() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Location skeleton (icon + text)
        ShimmerBox(
            modifier =
                Modifier
                    .width(120.dp)
                    .height(20.dp),
            shape = RoundedCornerShape(10.dp),
        )

        // Weather icon skeleton
        ShimmerBox(
            modifier = Modifier.size(120.dp),
            shape = CircleShape,
        )

        // Temperature skeleton
        ShimmerBox(
            modifier =
                Modifier
                    .width(140.dp)
                    .height(96.dp),
            shape = RoundedCornerShape(8.dp),
        )

        // Weather description skeleton
        ShimmerBox(
            modifier =
                Modifier
                    .width(120.dp)
                    .height(24.dp),
            shape = RoundedCornerShape(8.dp),
        )
    }
}

@Composable
private fun ForecastSkeleton() {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        // Today's forecast card skeleton
        ShimmerBox(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(200.dp),
            shape = RoundedCornerShape(16.dp),
        )

        // Weekly forecast card skeleton
        ShimmerBox(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(400.dp),
            shape = RoundedCornerShape(16.dp),
        )
    }
}

@Preview
@Composable
private fun WeatherLoadingSkeletonPreview() {
    TempestTheme {
        WeatherLoadingSkeleton()
    }
}

package com.harry.design

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

object OverlayColors {
    val contentPrimary = Color.White
    val contentSecondary = Color.White.copy(alpha = 0.85f)
    val contentTertiary = Color.White.copy(alpha = 0.7f)
    val contentDisabled = Color.White.copy(alpha = 0.6f)
    val contentInteractive = Color.White.copy(alpha = 0.8f)
    val surfaceTranslucent = Color.DarkGray.copy(alpha = 0.2f)
    val shimmerStart = Color.White.copy(alpha = 0.4f)
    val shimmerPeak = Color.White.copy(alpha = 0.8f)
}

object WeatherGradients {
    object Dawn {
        val skyDark = Color(0xFF1a1a2e)
        val skyDeep = Color(0xFF16213e)
        val horizonWarm = Color(0xFFffa726)
        val lightGolden = Color(0xFFffcc80)
        val baseCream = Color(0xFFfff3e0)
        val overlayGolden = Color(0x33ffcc80)
        val overlayOrange = Color(0x1affa726)
        val overlayTransparent = Color(0x00000000)
    }

    object Dusk {
        val skyEvening = Color(0xFF0d1421)
        val skyBluePurple = Color(0xFF1a1a2e)
        val skyTwilight = Color(0xFF3d2459)
        val horizonDeep = Color(0xFFff7043)
        val sunsetGolden = Color(0xFFffab40)
        val baseWarm = Color(0xFFfff8e1)
        val overlayCoral = Color(0x33ff8a65)
        val overlayOrange = Color(0x1aff7043)
        val overlayTransparent = Color(0x00000000)
    }

    object Day {
        val skyDeep = Color(0xFF0d47a1)
        val skyRich = Color(0xFF1976d2)
        val skyBright = Color(0xFF42a5f5)
        val skyLight = Color(0xFF81d4fa)
        val horizonMedium = Color(0xFF90caf9)
        val overlayBlue = Color(0x10bbdefb)
        val overlayBlueMid = Color(0x0d81d4fa)
        val overlayTransparent = Color(0x00000000)
    }

    object Night {
        val spaceBlack = Color(0xFF0a0a0f)
        val skyDark = Color(0xFF0d1421)
        val navyRich = Color(0xFF1a1a2e)
        val navyLight = Color(0xFF16213e)
        val baseGrey = Color(0xFF263238)
        val overlayIndigo = Color(0x1a3f51b5)
        val overlayDark = Color(0x0d1a1a2e)
        val overlayTransparent = Color(0x00000000)
    }
}

fun createDawnBrush(): Brush =
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

fun createDuskBrush(): Brush =
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

fun createDayBrush(): Brush =
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

fun createNightBrush(): Brush =
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

fun createDawnOverlay(): Brush =
    Brush.radialGradient(
        colorStops =
            arrayOf(
                0.0f to WeatherGradients.Dawn.overlayGolden,
                0.7f to WeatherGradients.Dawn.overlayOrange,
                1.0f to WeatherGradients.Dawn.overlayTransparent,
            ),
        radius = 800f,
    )

fun createDuskOverlay(): Brush =
    Brush.radialGradient(
        colorStops =
            arrayOf(
                0.0f to WeatherGradients.Dusk.overlayCoral,
                0.6f to WeatherGradients.Dusk.overlayOrange,
                1.0f to WeatherGradients.Dusk.overlayTransparent,
            ),
        radius = 900f,
    )

fun createDayOverlay(): Brush =
    Brush.radialGradient(
        colorStops =
            arrayOf(
                0.0f to WeatherGradients.Day.overlayBlue,
                0.8f to WeatherGradients.Day.overlayBlueMid,
                1.0f to WeatherGradients.Day.overlayTransparent,
            ),
        radius = 1000f,
    )

fun createNightOverlay(): Brush =
    Brush.radialGradient(
        colorStops =
            arrayOf(
                0.0f to WeatherGradients.Night.overlayIndigo,
                0.7f to WeatherGradients.Night.overlayDark,
                1.0f to WeatherGradients.Night.overlayTransparent,
            ),
        radius = 700f,
    )

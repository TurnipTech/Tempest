package com.harry.weather.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.min
import com.harry.design.OverlayColors

@Composable
fun UvCard(uvi: Double, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor = OverlayColors.surfaceTranslucent,
            ),
        shape = RoundedCornerShape(16.dp),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 0.dp,
            ),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "UV Index",
                style = MaterialTheme.typography.titleLarge,
                color = OverlayColors.contentPrimary,
                fontWeight = FontWeight.Medium
            )
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { min(uvi.toFloat() / 11f, 1f) },
                        modifier = Modifier.size(64.dp),
                        trackColor = OverlayColors.contentDisabled.copy(alpha = 0.3f),
                        color = getUvColor(uvi),
                        strokeWidth = 6.dp,
                        strokeCap = StrokeCap.Round
                    )
                    
                    Text(
                        text = uvi.toInt().toString(),
                        style = MaterialTheme.typography.titleLarge,
                        color = OverlayColors.contentPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Column {
                    Text(
                        text = getUvLevel(uvi),
                        style = MaterialTheme.typography.bodyLarge,
                        color = OverlayColors.contentPrimary,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = getUvDescription(uvi),
                        style = MaterialTheme.typography.bodyMedium,
                        color = OverlayColors.contentSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun getUvColor(uvi: Double): Color {
    return when {
        uvi <= 2 -> Color(0xFF4CAF50) // Green - Low
        uvi <= 5 -> Color(0xFFFFEB3B) // Yellow - Moderate
        uvi <= 7 -> Color(0xFFFF9800) // Orange - High
        uvi <= 10 -> Color(0xFFf44336) // Red - Very High
        else -> Color(0xFF9C27B0) // Purple - Extreme
    }
}

@Composable
private fun getUvLevel(uvi: Double): String {
    return when {
        uvi <= 2 -> "Low"
        uvi <= 5 -> "Moderate"
        uvi <= 7 -> "High"
        uvi <= 10 -> "Very High"
        else -> "Extreme"
    }
}

@Composable
private fun getUvDescription(uvi: Double): String {
    return when {
        uvi <= 2 -> "Minimal protection required"
        uvi <= 5 -> "Seek shade during midday"
        uvi <= 7 -> "Protection essential"
        uvi <= 10 -> "Extra protection required"
        else -> "Avoid sun exposure"
    }
}

@Preview
@Composable
fun UvCardLowPreview() {
    UvCard(2.0)
}

@Preview
@Composable
fun UvCardModeratePreview() {
    UvCard(4.5)
}

@Preview
@Composable
fun UvCardHighPreview() {
    UvCard(8.0)
}

@Preview
@Composable
fun UvCardExtremePreview() {
    UvCard(11.5)
}

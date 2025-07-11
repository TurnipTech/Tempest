package com.harry.weather.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.harry.design.OverlayColors
import com.harry.design.UvColors
import com.harry.weather.ui.model.UvUiModel
import kotlinx.coroutines.launch

@Composable
fun UvCard(uvModel: UvUiModel?, modifier: Modifier = Modifier) {
    Card(
        modifier =
            modifier.fillMaxWidth(),
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
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "UV Index",
                style = MaterialTheme.typography.titleLarge,
                color = OverlayColors.contentPrimary,
                fontWeight = FontWeight.Medium,
            )

            if (uvModel != null) {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { uvModel.uvPercentage },
                        modifier = Modifier.size(64.dp),
                        trackColor = OverlayColors.contentDisabled.copy(alpha = 0.3f),
                        color = getUvColor(uvModel.index.toDouble()),
                        strokeWidth = 6.dp,
                        strokeCap = StrokeCap.Round,
                    )

                    Text(
                        text = uvModel.index.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        color = OverlayColors.contentPrimary,
                        fontWeight = FontWeight.Bold,
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = uvModel.level,
                        style = MaterialTheme.typography.bodyLarge,
                        color = OverlayColors.contentPrimary,
                        fontWeight = FontWeight.Medium,
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    IndexToolTip(uvModel.description)
                }
            } else {
                Text(
                    text = "No UV data available",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OverlayColors.contentSecondary,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndexToolTip(text: String) {
    val tooltipState = rememberTooltipState()
    val coroutineScope = rememberCoroutineScope()
    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = {
            PlainTooltip {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        },
        state = tooltipState,
    ) {
        IconButton(
            onClick = {
                coroutineScope.launch {
                    tooltipState.show()
                }
            },
            modifier = Modifier.size(20.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = "UV info",
                tint = OverlayColors.contentSecondary,
                modifier = Modifier.size(16.dp),
            )
        }
    }
}

@Composable
private fun getUvColor(uvi: Double): Color =
    when {
        uvi <= 2 -> UvColors.low
        uvi <= 5 -> UvColors.moderate
        uvi <= 7 -> UvColors.high
        uvi <= 10 -> UvColors.veryHigh
        else -> UvColors.extreme
    }

@Composable
private fun getUvLevel(uvi: Double): String =
    when {
        uvi <= 2 -> "Low"
        uvi <= 5 -> "Moderate"
        uvi <= 7 -> "High"
        uvi <= 10 -> "Very High"
        else -> "Extreme"
    }

@Composable
private fun getUvDescription(uvi: Double): String =
    when {
        uvi <= 2 -> "Minimal protection required"
        uvi <= 5 -> "Seek shade during midday"
        uvi <= 7 -> "Protection essential"
        uvi <= 10 -> "Extra protection required"
        else -> "Avoid sun exposure"
    }

@Preview
@Composable
fun UvCardLowPreview() {
    UvCard(UvUiModel(2, "Low", "Minimal protection required", 0.18f))
}

@Preview
@Composable
fun UvCardModeratePreview() {
    UvCard(UvUiModel(4, "Moderate", "Seek shade during midday", 0.36f))
}

@Preview
@Composable
fun UvCardHighPreview() {
    UvCard(UvUiModel(8, "High", "Protection essential", 0.73f))
}

@Preview
@Composable
fun UvCardExtremePreview() {
    UvCard(UvUiModel(12, "Extreme", "Avoid sun exposure", 1.0f))
}

@Preview
@Composable
fun UvCardNullPreview() {
    UvCard(null)
}

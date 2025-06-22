package com.harry.location.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.harry.design.OverlayColors
import com.harry.location.R

@Composable
fun SearchEmptyState(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            stringResource(R.string.no_locations_found),
            style = MaterialTheme.typography.headlineMedium,
            color = OverlayColors.contentDisabled,
        )
    }
}

@Preview
@Composable
private fun SearchEmptyStatePreview() {
    SearchEmptyState()
}

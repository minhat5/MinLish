package com.minlish.ui.screen.analytics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Workspaces
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minlish.ui.common.component.CircularProgressMetricCard
import com.minlish.ui.common.component.ProgressMetricCard

@Composable
fun ProgressSummary(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            ProgressMetricCard(
                modifier = Modifier.weight(1f),
                title = "Mastered\nWords",
                value = "482",
                detail = "+12 this\nweek",
                icon = Icons.Filled.Workspaces
            )
            ProgressMetricCard(
                modifier = Modifier.weight(1f),
                title = "Learning\nSpeed",
                value = "2.4",
                detail = "w/min",
                icon = Icons.Filled.Speed
            )
        }

        CircularProgressMetricCard(
            title = "Retention\nRate",
            value = "92%",
            progress = 0.92f,
            icon = Icons.Filled.Workspaces
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProgressSummaryPreview() {
    ProgressSummary()
}

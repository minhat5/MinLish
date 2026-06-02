package com.minlish.ui.screen.analytics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Workspaces
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minlish.ui.common.component.CircularProgressMetricCard
import com.minlish.ui.common.component.ProgressMetricCard

data class ProgressSummaryData(
    val masteredWords: String = "482",
    val masteredWordsDetail: String = "+12 this\nweek",
    val atRiskWords: String = "37",
    val atRiskWordsDetail: String = "words",
    val retentionRate: String = "82%",
    val retentionDetail: String = "0/0\nreviews",
    val retentionProgress: Float = 0.82f
)

@Composable
fun ProgressSummary(
    modifier: Modifier = Modifier,
    data: ProgressSummaryData = ProgressSummaryData()
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
                value = data.masteredWords,
                detail = data.masteredWordsDetail,
                icon = Icons.Filled.Workspaces
            )
            ProgressMetricCard(
                modifier = Modifier.weight(1f),
                title = "At Risk\nWords",
                value = data.atRiskWords,
                detail = data.atRiskWordsDetail,
                icon = Icons.Filled.Warning,
                iconTint = Color(0xFFF5A623)
            )
        }

        CircularProgressMetricCard(
            title = "Retention\nRate",
            value = data.retentionRate,
            progress = data.retentionProgress,
            detail = data.retentionDetail,
            icon = Icons.Filled.Workspaces
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProgressSummaryPreview() {
    ProgressSummary()
}

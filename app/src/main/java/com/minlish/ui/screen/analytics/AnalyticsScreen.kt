package com.minlish.ui.screen.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.ui.screen.dailyReviewSummary.colorOnSurface
import com.minlish.ui.screen.dailyReviewSummary.colorOnSurfaceVariant
import com.minlish.ui.screen.dailyReviewSummary.colorSurface

@Composable
fun AnalyticsScreen(
    modifier: Modifier = Modifier,
    progressSummaryData: ProgressSummaryData = ProgressSummaryData()
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorSurface)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Text(
            text = "Your Progress",
            color = colorOnSurface,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Keep up the great work! You're learning faster than 78% of users.",
            color = colorOnSurfaceVariant,
            fontSize = 14.sp,
            lineHeight = 18.sp,
            modifier = Modifier.padding(top = 6.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))
        ProgressSummary(data = progressSummaryData)

        Spacer(modifier = Modifier.height(24.dp))
        WeeklyConsistencyChart()
    }
}

@Preview(showBackground = true)
@Composable
private fun AnalyticsScreenPreview() {
    AnalyticsScreen()
}

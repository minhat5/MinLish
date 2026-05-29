package com.minlish.ui.screen.dashboardHome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minlish.ui.common.component.StreakCard
import com.minlish.ui.theme.colorPrimary

@Composable
fun BentoGridDashboard() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        DailyGoalCard(percent, timeRemaining)
        StreakCard(colorPrimary,7)

    }
}

@Preview
@Composable
fun PRV() {
    BentoGridDashboard()
}
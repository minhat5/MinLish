package com.minlish.ui.screen.dashboardHome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.minlish.ui.common.component.StreakCard
import com.minlish.ui.theme.colorPrimary

@Composable
fun BentoGridDashboard(
    streakDays: Int = 0
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        StreakCard(colorPrimary, streakDays)
    }
}

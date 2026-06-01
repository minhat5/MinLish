package com.minlish.ui.screen.dashboardHome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.School
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.minlish.ui.common.component.StatCard

@Composable
fun StatsGrid(
    wordsLearned: Int = 363,
    weeklyProgress: Int = 36
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Filled.School,
            value = wordsLearned.toString(),
            label = "Words Learned"
        )

        StatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.AutoMirrored.Filled.TrendingUp,
            value = "+$weeklyProgress%",
            label = "Weekly Progress"
        )
    }
}
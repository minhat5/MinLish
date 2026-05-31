package com.minlish.ui.screen.dashboardHome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.minlish.ui.common.component.ProgressCard
import com.minlish.ui.theme.*


val userName = "Alex"
val percent = 20
val timeRemaining = 50
val wordsLearned = 363
val weeklyProgress = 36
val tag = "Travel"
val progress = 36
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colorSurface
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            WelcomeSection(userName)
            BentoGridDashboard(percent = percent, timeRemaining = timeRemaining)
            ProgressCard(
                title = "Continue Learning",
                tag = tag,
                subtitle = "Common Verbs Deck",
                progressText = "$progress%",
                progress = progress/100f
            )
            StatsGrid(wordsLearned = wordsLearned, weeklyProgress = weeklyProgress)
        }
    }
}
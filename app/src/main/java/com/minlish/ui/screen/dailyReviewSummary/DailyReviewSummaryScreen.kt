package com.minlish.ui.screen.dailyReviewSummary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minlish.ui.common.component.PrimaryButton
import com.minlish.ui.common.component.ProgressCard
import com.minlish.ui.common.state.StreakState
import com.minlish.ui.theme.*

val title = "Level 4: Explorer"
val trailingText = "120 XP to go"
val text = "Continue Learning"
val progress = 36

@Composable
fun DailyReviewSummary(
    wordsCount: Int,
    accuracy: Int,
    onContinueClick: () -> Unit = {}
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorSurface)
            .safeDrawingPadding()
            .padding(horizontal = 20.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        CelebrationHeader()
        Spacer(modifier = Modifier.height(30.dp))
        StatsBentoGrid(wordsCount, accuracy)
        Spacer(modifier = Modifier.height(150.dp))
//        ProgressCard(title = title, trailingText = trailingText, progress = progress/100f)
//        Spacer(modifier = Modifier.height(30.dp))
        PrimaryButton(primaryGradient, text, onClick = onContinueClick)
    }
}
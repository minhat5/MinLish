package com.minlish.ui.screen.dailyReviewSummary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minlish.ui.common.component.PrimaryButton
import com.minlish.ui.common.component.ProgressCard
import com.minlish.ui.theme.*

val words = 42
val accuracy = 94
val level = 5
val rank = "Master"
val text = "Continue Learning"
val progress = 20
@Composable
fun DailyReviewSummary() {
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
        StatsBentoGrid(words, accuracy)
        Spacer(modifier = Modifier.height(30.dp))
        ProgressCard(level, rank, progress)
        Spacer(modifier = Modifier.height(30.dp))
        PrimaryButton(primaryGradient, text)
    }
}

@Preview
@Composable
fun RV(){
    DailyReviewSummary()
}
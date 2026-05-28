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
import androidx.compose.ui.unit.dp
import com.minlish.ui.common.component.ProgressCard

val colorPrimary = Color(0xFF4F378A)
val colorSurface = Color(0xFFFDF7FF)
val colorOnSurface = Color(0xFF1D1B20)
val colorOnSurfaceVariant = Color(0xFF494551)
val colorGradientStart = Color(0xFF4F378A)
val colorGradientEnd = Color(0xFFF97316)
val primaryGradient = Brush.linearGradient(listOf(colorGradientStart, colorGradientEnd))

val words: Int = 42
val accuracy: Int = 94

val level: Int = 5
val rank: String = "Master"
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
        ProgressCard(level, rank)
        Spacer(modifier = Modifier.height(30.dp))
        PrimaryButton()
    }
}

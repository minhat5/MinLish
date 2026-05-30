package com.minlish.ui.screen.dailyReviewSummary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minlish.ui.common.component.StatCard
import com.minlish.ui.common.component.StreakCard
import com.minlish.ui.theme.*


val streaks: Int = 36
@Composable
fun StatsBentoGrid(
    words: Int,
    accuracy: Int
){
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ){
            StatCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Filled.LibraryBooks,
                value = words.toString(),
                label = "WORDS"
            )
            StatCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Filled.CheckCircle,
                value = "$accuracy%",
                label = "ACCURACY"
            )
        }
        StreakCard(primaryGradient, streaks)
    }
}
@Preview
@Composable
fun ReviewBento(){
    StatsBentoGrid(42, 94)
}
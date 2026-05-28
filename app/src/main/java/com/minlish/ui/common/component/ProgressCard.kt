package com.minlish.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.ui.screen.dailyReviewSummary.colorOnSurface
import com.minlish.ui.screen.dailyReviewSummary.colorPrimary
import com.minlish.ui.screen.dailyReviewSummary.colorSurface
import com.minlish.ui.screen.dailyReviewSummary.primaryGradient

val xp = 100
@Composable
fun ProgressCard(
    level: Int,
    rank: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
            .background(colorSurface, RoundedCornerShape(16.dp))
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Level: $level: $rank",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorOnSurface
            )
            Text(
                text = "$xp XP to level up",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = colorPrimary,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFE6E0E9))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.65f) //tiến trình
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(50))
                    .background(primaryGradient)
            )
        }
    }
}

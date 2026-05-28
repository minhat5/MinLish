package com.minlish.ui.screen.dailyReviewSummary

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CelebrationHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.size(128.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(CircleShape)
                    .shadow(elevation = 10.dp)
                    .background(primaryGradient)
            )

            Icon(
                imageVector = Icons.Filled.EmojiEvents,
                contentDescription = "Trophy",
                tint = Color.White,
                modifier = Modifier.size(60.dp)
            )
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.TopEnd)
                            .offset(x = (-4).dp, y =4.dp)
                    .clip(CircleShape)
                    .background(colorSurface),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.LocalFireDepartment,
                    contentDescription = "Fire",
                    tint = Color(0xFFC9A74D),
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.Center)
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Goal Met!",
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold,
            color = colorPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Incredible work today. You're unstoppable.",
            fontSize = 16.sp,
            color = colorPrimary
        )
    }
}
package com.minlish.ui.screen.vocabularyDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.ui.theme.*


@Composable
fun WordHeroSection(
    type: String,
    word: String,
    transcription: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(colorSecondaryContainer, RoundedCornerShape(50.dp))
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Text(
                text = type,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = colorOnSecondaryContainer
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = word,
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold,
            color = colorOnSurface
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "$transcription",
                fontSize = 16.sp,
                color = colorOnSurfaceVariant
            )
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .shadow(elevation = 2.dp, shape = CircleShape)
                    .background(colorSurface, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.VolumeUp,
                    contentDescription = "Play pronunciation",
                    tint = colorPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
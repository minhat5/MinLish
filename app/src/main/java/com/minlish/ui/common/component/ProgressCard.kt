package com.minlish.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.minlish.ui.theme.*

@Composable
fun ProgressCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    tag: String? = null,
    trailingText: String? = null,
    progress: Float,
    progressText: String? = null,
    onClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
            .background(colorSurface, RoundedCornerShape(16.dp))
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorOnSurface
            )
            if (tag != null) {
                Box(
                    modifier = Modifier
                        .background(colorSecondaryContainer, RoundedCornerShape(50))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = tag.uppercase(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorOnSecondaryContainer
                    )
                }
            } else if (trailingText != null) {
                Text(
                    text = trailingText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorPrimary
                )
            }
        }
        if (subtitle != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = subtitle,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colorPrimary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(if (progressText != null) 8.dp else 16.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFFE6E0E9))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress)
                        .clip(RoundedCornerShape(50))
                        .background(primaryGradient)
                )
            }

            if (progressText != null) {
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = progressText,
                    fontSize = 14.sp,
                    color = colorOnSurfaceVariant
                )
            }
        }
    }
}

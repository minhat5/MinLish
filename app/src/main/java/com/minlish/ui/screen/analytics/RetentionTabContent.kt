package com.minlish.ui.screen.analytics

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.ui.theme.colorOnSurface
import com.minlish.ui.theme.colorOnSurfaceVariant
import com.minlish.ui.theme.colorPrimary
import com.minlish.ui.theme.colorSurface
import com.minlish.ui.theme.primaryGradient
import java.text.NumberFormat
import java.util.Locale

data class RetentionLevelData(
    val label: String,
    val count: Int,
    val intervalRange: String
)

@Composable
fun RetentionTabContent(
    modifier: Modifier = Modifier,
    levels: List<RetentionLevelData> = defaultRetentionLevelData(),
    wordsReadyForReview: Int = 0,
    onStartReviewSession: () -> Unit = {}
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Retention Levels",
            color = colorOnSurface,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Your SM-2 memory distribution based on the latest review intervals.",
            color = colorOnSurfaceVariant,
            fontSize = 14.sp,
            lineHeight = 18.sp,
            modifier = Modifier.padding(top = 6.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))
        RetentionChartCard(levels = levels)

        Spacer(modifier = Modifier.height(24.dp))
        ReviewQueueSummary(wordsReadyForReview = wordsReadyForReview)

        Spacer(modifier = Modifier.height(14.dp))
        Button(
            onClick = onStartReviewSession,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = androidx.compose.foundation.layout.PaddingValues()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(primaryGradient, RoundedCornerShape(28.dp)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayCircle,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Start Review Session",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun RetentionChartCard(
    modifier: Modifier = Modifier,
    levels: List<RetentionLevelData> = defaultRetentionLevelData()
) {
    val safeLevels = levels.ifEmpty { defaultRetentionLevelData() }
    val maxCount = safeLevels.maxOf { it.count }.coerceAtLeast(1)
    val topLevel = safeLevels.maxByOrNull { it.count }
    val chartHeight = scaledChartHeight(maxCount)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(24.dp))
            .background(colorSurface, RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        RetentionLevelBars(
            levels = safeLevels,
            maxCount = maxCount,
            chartHeight = chartHeight
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF4EEFF))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(colorPrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.AutoGraph,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = retentionInsightText(topLevel),
                color = colorOnSurfaceVariant,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun RetentionLevelBars(
    levels: List<RetentionLevelData>,
    maxCount: Int,
    chartHeight: Dp
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(chartHeight)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            levels.forEachIndexed { index, level ->
                val normalizedHeight = (level.count.toFloat() / maxCount).coerceIn(0f, 1f)
                val barHeight = ((chartHeight - 42.dp) * normalizedHeight).coerceAtLeast(10.dp)
                ChartBar(
                    count = level.count,
                    height = barHeight,
                    brush = retentionBrushFor(index, levels.lastIndex)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            levels.forEach { level ->
                Text(
                    modifier = Modifier.width(42.dp),
                    text = level.label,
                    color = colorOnSurfaceVariant,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun ChartBar(
    count: Int,
    height: Dp,
    brush: Brush
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = NumberFormat.getNumberInstance(Locale.US).format(count),
            color = colorOnSurfaceVariant,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .width(42.dp)
                .height(height)
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomStart = 4.dp, bottomEnd = 4.dp))
                .background(brush = brush)
        )
    }
}

@Composable
private fun ReviewQueueSummary(
    wordsReadyForReview: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Schedule,
            contentDescription = null,
            tint = colorPrimary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${NumberFormat.getNumberInstance(Locale.US).format(wordsReadyForReview)} words ready for review",
            color = colorPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun retentionBrushFor(
    index: Int,
    lastIndex: Int
): Brush {
    return when (index) {
        0 -> Brush.verticalGradient(listOf(Color(0xFFFF9800), Color(0xFFFF5722)))
        lastIndex -> Brush.verticalGradient(listOf(Color(0xFF9575CD), Color(0xFF512DA8)))
        else -> Brush.verticalGradient(listOf(Color(0xFFB39DDB), Color(0xFF9575CD)))
    }
}

private fun scaledChartHeight(maxCount: Int): Dp {
    val minHeight = 154.dp
    val maxHeight = 220.dp
    val scale = (maxCount / 1000f).coerceIn(0f, 1f)
    return minHeight + (maxHeight - minHeight) * scale
}

private fun retentionInsightText(
    topLevel: RetentionLevelData?
): String {
    return when {
        topLevel == null || topLevel.count == 0 -> "No retention data is available yet. Complete review sessions to build your memory profile."
        topLevel.label.endsWith("1") -> "Most words are currently at Level 1. Focus on graduating more words to long-term memory."
        topLevel.label.endsWith("5") -> "Most words are in Level 5. Keep reviewing due words to protect long-term memory."
        else -> "Most words are currently in ${topLevel.label}. Keep reviewing consistently to move them toward Level 5."
    }
}

fun defaultRetentionLevelData() = listOf(
    RetentionLevelData("Level 1", count = 0, intervalRange = "I < 3 days"),
    RetentionLevelData("Level 2", count = 0, intervalRange = "3 <= I <= 7 days"),
    RetentionLevelData("Level 3", count = 0, intervalRange = "8 <= I <= 21 days"),
    RetentionLevelData("Level 4", count = 0, intervalRange = "22 <= I <= 45 days"),
    RetentionLevelData("Level 5", count = 0, intervalRange = "I > 45 days")
)

@Preview(showBackground = true)
@Composable
private fun RetentionTabContentPreview() {
    RetentionTabContent()
}

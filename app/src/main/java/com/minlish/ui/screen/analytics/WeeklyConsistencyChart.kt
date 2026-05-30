package com.minlish.ui.screen.analytics

import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.ui.theme.*

private val chartPurple = Color(0xFF5A4599)
private val chartYellow = Color(0xFFE7C34E)
private val chartMuted = Color(0xFFE6E0E9)

data class WeeklyConsistencyEntry(
    val dayLabel: String,
    val learningProgress: Float,
    val goalProgress: Float,
    val goalMet: Boolean
)

@Composable
fun WeeklyConsistencyChart(
    modifier: Modifier = Modifier,
    entries: List<WeeklyConsistencyEntry> = defaultWeeklyConsistencyEntries(),
    periodLabel: String = "This Week"
) {
    val chartEntries = entries.ifEmpty { defaultWeeklyConsistencyEntries() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(24.dp))
            .background(colorSurface, RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Weekly Consistency",
                color = colorOnSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = periodLabel,
                    color = colorPrimary,
                    fontSize = 12.sp
                )
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    tint = colorPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
        ConsistencyChartCanvas(entries = chartEntries)
        Spacer(modifier = Modifier.height(18.dp))
        ChartLegend()
    }
}

@Composable
private fun ConsistencyChartCanvas(
    entries: List<WeeklyConsistencyEntry>,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            val bottomLabelHeight = 22.dp.toPx()
            val chartHeight = size.height - bottomLabelHeight
            val slotWidth = size.width / entries.size
            val barWidth = 24.dp.toPx()
            val baseY = chartHeight

            entries.forEachIndexed { index, entry ->
                val left = slotWidth * index + (slotWidth - barWidth) / 2f
                val height = chartHeight * entry.learningProgress.coerceIn(0f, 1f)
                val top = baseY - height
                drawRoundRect(
                    color = if (entry.goalMet) chartPurple else chartMuted,
                    topLeft = Offset(left, top),
                    size = Size(barWidth, height),
                    cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                )
            }

            val goalPoints = entries.mapIndexed { index, entry ->
                Offset(
                    x = slotWidth * index + slotWidth / 2f,
                    y = chartHeight * (1f - entry.goalProgress.coerceIn(0f, 1f))
                )
            }
            val path = Path().apply {
                if (goalPoints.isNotEmpty()) {
                    moveTo(goalPoints.first().x, goalPoints.first().y)
                    for (index in 1 until goalPoints.size) {
                        val previous = goalPoints[index - 1]
                        val current = goalPoints[index]
                        val midPoint = Offset(
                            x = (previous.x + current.x) / 2f,
                            y = (previous.y + current.y) / 2f
                        )
                        quadraticTo(previous.x, previous.y, midPoint.x, midPoint.y)
                    }
                    lineTo(goalPoints.last().x, goalPoints.last().y)
                }
            }

            drawPath(
                path = path,
                color = chartYellow,
                style = Stroke(
                    width = 4.dp.toPx(),
                    cap = StrokeCap.Round,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10.dp.toPx(), 7.dp.toPx()))
                )
            )

            entries.forEachIndexed { index, entry ->
                drawContext.canvas.nativeCanvas.drawText(
                    entry.dayLabel,
                    slotWidth * index + slotWidth / 2f,
                    size.height - 4.dp.toPx(),
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.rgb(73, 69, 81)
                        textSize = 11.sp.toPx()
                        textAlign = android.graphics.Paint.Align.CENTER
                        isAntiAlias = true
                    }
                )
            }
        }
    }
}

private fun defaultWeeklyConsistencyEntries() = listOf(
    WeeklyConsistencyEntry("M", learningProgress = 0.42f, goalProgress = 0.55f, goalMet = false),
    WeeklyConsistencyEntry("T", learningProgress = 0.62f, goalProgress = 0.66f, goalMet = true),
    WeeklyConsistencyEntry("W", learningProgress = 0.82f, goalProgress = 0.88f, goalMet = true),
    WeeklyConsistencyEntry("T", learningProgress = 0.32f, goalProgress = 0.80f, goalMet = false),
    WeeklyConsistencyEntry("F", learningProgress = 0.92f, goalProgress = 0.12f, goalMet = true),
    WeeklyConsistencyEntry("S", learningProgress = 0.52f, goalProgress = 0.02f, goalMet = false),
    WeeklyConsistencyEntry("S", learningProgress = 0.10f, goalProgress = 0.90f, goalMet = false)
)

@Composable
private fun ChartLegend() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LegendItem(color = chartPurple, text = "Goal Met")
        LegendItem(color = chartMuted, text = "Learning")
    }
}

@Composable
private fun LegendItem(
    color: Color,
    text: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.size(6.dp))
        Text(
            text = text,
            color = colorOnSurfaceVariant,
            fontSize = 12.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WeeklyConsistencyChartPreview() {
    WeeklyConsistencyChart()
}

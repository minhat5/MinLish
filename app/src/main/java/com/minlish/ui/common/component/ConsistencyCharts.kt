package com.minlish.ui.common.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.ui.theme.colorOnSurfaceVariant

private val consistencyPurple = Color(0xFF6C63FF)
private val consistencyMuted = Color(0xFFE8E8F0)
private val consistencyToday = Color(0xFF34C759)
private val consistencyText = Color(0xFF494551)

data class WeeklyConsistencyChartEntry(
    val dayLabel: String,
    val hasStudied: Boolean,
    val isToday: Boolean = false
)

data class MonthlyConsistencyCalendarEntry(
    val dayOfMonth: Int,
    val hasStudied: Boolean,
    val isToday: Boolean = false
)

@Composable
fun WeeklyConsistencyPillChart(
    entries: List<WeeklyConsistencyChartEntry>,
    modifier: Modifier = Modifier
) {
    if (entries.isEmpty()) return

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            val bottomLabelHeight = 24.dp.toPx()
            val topDotSpace = 12.dp.toPx()
            val chartHeight = size.height - bottomLabelHeight - topDotSpace
            val slotWidth = size.width / entries.size
            val barWidth = minOf(24.dp.toPx(), slotWidth * 0.48f)
            val barHeight = chartHeight * 0.78f
            val baseY = topDotSpace + chartHeight
            val top = baseY - barHeight
            val cornerRadius = CornerRadius(barWidth / 2f, barWidth / 2f)
            val dotRadius = 4.dp.toPx()
            val labelPaint = android.graphics.Paint().apply {
                color = consistencyText.toArgb()
                textSize = 11.sp.toPx()
                textAlign = android.graphics.Paint.Align.CENTER
                isAntiAlias = true
            }
            val dotPaint = glowDotPaint(shadowBlur = 6.dp.toPx())

            entries.forEachIndexed { index, entry ->
                val centerX = slotWidth * index + slotWidth / 2f
                val left = centerX - barWidth / 2f
                drawRoundRect(
                    color = entry.chartColor(),
                    topLeft = Offset(left, top),
                    size = Size(barWidth, barHeight),
                    cornerRadius = cornerRadius
                )

                if (entry.hasStudied) {
                    drawContext.canvas.nativeCanvas.drawCircle(
                        centerX,
                        top - 6.dp.toPx(),
                        dotRadius,
                        dotPaint
                    )
                }

                drawContext.canvas.nativeCanvas.drawText(
                    entry.dayLabel,
                    centerX,
                    size.height - 5.dp.toPx(),
                    labelPaint
                )
            }
        }
    }
}

@Composable
fun MonthlyConsistencyCalendar(
    entries: List<MonthlyConsistencyCalendarEntry>,
    firstDayOffset: Int,
    modifier: Modifier = Modifier
) {
    if (entries.isEmpty()) return

    val studiedDays = entries.filter { it.hasStudied }.map { it.dayOfMonth }.toSet()
    val today = entries.firstOrNull { it.isToday }?.dayOfMonth
    val daysInMonth = entries.maxOfOrNull { it.dayOfMonth } ?: 31
    val normalizedOffset = firstDayOffset.coerceIn(0, 6)
    val calendarCells = List(normalizedOffset) { null } + (1..daysInMonth).map { it }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("M", "T", "W", "T", "F", "S", "S").forEach { label ->
                Text(
                    text = label,
                    color = colorOnSurfaceVariant,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        calendarCells.chunked(7).forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                week.forEach { day ->
                    CalendarDayCell(
                        day = day,
                        isStudied = day != null && day in studiedDays,
                        isToday = day != null && day == today,
                        modifier = Modifier.weight(1f)
                    )
                }
                repeat(7 - week.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun CalendarDayCell(
    day: Int?,
    isStudied: Boolean,
    isToday: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .then(
                if (isStudied || isToday) {
                    Modifier.shadow(elevation = 6.dp, shape = RoundedCornerShape(10.dp))
                } else {
                    Modifier
                }
            )
            .background(
                color = calendarCellColor(
                    day = day,
                    isStudied = isStudied,
                    isToday = isToday
                ),
                shape = RoundedCornerShape(10.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (day != null) {
            Text(
                text = day.toString(),
                color = if (isStudied || isToday) Color.White else consistencyText,
                fontSize = 12.sp,
                fontWeight = if (isStudied || isToday) FontWeight.Bold else FontWeight.Medium
            )
            if (isStudied) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(5.dp)
                        .size(8.dp)
                        .shadow(elevation = 6.dp, shape = CircleShape)
                        .background(Color.White, CircleShape)
                )
            }
        }
    }
}

private fun WeeklyConsistencyChartEntry.chartColor() = when {
    isToday -> consistencyToday
    hasStudied -> consistencyPurple
    else -> consistencyMuted
}

private fun calendarCellColor(
    day: Int?,
    isStudied: Boolean,
    isToday: Boolean
) = when {
    day == null -> Color.Transparent
    isToday -> consistencyToday
    isStudied -> consistencyPurple
    else -> consistencyMuted
}

private fun glowDotPaint(shadowBlur: Float) = android.graphics.Paint().apply {
    color = android.graphics.Color.WHITE
    isAntiAlias = true
    setShadowLayer(
        shadowBlur,
        0f,
        0f,
        android.graphics.Color.argb(170, 255, 255, 255)
    )
}

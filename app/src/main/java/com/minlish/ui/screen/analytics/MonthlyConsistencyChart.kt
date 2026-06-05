package com.minlish.ui.screen.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.ui.common.component.MonthlyConsistencyCalendar
import com.minlish.ui.common.component.MonthlyConsistencyCalendarEntry
import com.minlish.ui.common.component.WeeklyConsistencyChartEntry
import com.minlish.ui.common.component.WeeklyConsistencyPillChart
import com.minlish.ui.theme.colorOnSurface
import com.minlish.ui.theme.colorOnSurfaceVariant
import com.minlish.ui.theme.colorPrimary
import com.minlish.ui.theme.colorSurface
import java.util.Calendar

private val chartStudied = Color(0xFF2196F3)
private val chartMuted = Color(0xFFE8E8F0)
private val chartToday = Color(0xFF34C759)

private enum class ConsistencyPeriod(val label: String) {
    Week("This Week"),
    Month("This Month")
}

@Composable
fun ConsistencyChart(
    modifier: Modifier = Modifier,
    weeklyEntries: List<WeeklyConsistencyChartEntry> = defaultWeeklyConsistencyEntries(),
    monthlyEntries: List<MonthlyConsistencyCalendarEntry> = defaultMonthlyConsistencyEntries(),
    firstMonthDayOffset: Int = currentMonthFirstDayOffset(),
    hasStudiedToday: Boolean = weeklyEntries.any { it.isToday && it.hasStudied } ||
        monthlyEntries.any { it.isToday && it.hasStudied }
) {
    var selectedPeriod by rememberSaveable { mutableStateOf(ConsistencyPeriod.Week) }
    val chartTitle = when (selectedPeriod) {
        ConsistencyPeriod.Week -> "Weekly Consistency"
        ConsistencyPeriod.Month -> "Monthly Consistency"
    }

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
                text = chartTitle,
                color = colorOnSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            PeriodDropdown(
                selectedPeriod = selectedPeriod,
                onPeriodSelected = { selectedPeriod = it }
            )
        }

        Spacer(modifier = Modifier.height(28.dp))
        when (selectedPeriod) {
            ConsistencyPeriod.Week -> WeeklyConsistencyPillChart(
                entries = weeklyEntries
                    .ifEmpty { defaultWeeklyConsistencyEntries() }
                    .withCurrentWeekDayMarked(hasStudiedToday = hasStudiedToday)
            )
            ConsistencyPeriod.Month -> MonthlyConsistencyCalendar(
                entries = monthlyEntries
                    .ifEmpty { defaultMonthlyConsistencyEntries() }
                    .withCurrentMonthDayMarked(hasStudiedToday = hasStudiedToday),
                firstDayOffset = firstMonthDayOffset
            )
        }
        Spacer(modifier = Modifier.height(18.dp))
        ChartLegend()
    }
}

@Composable
private fun PeriodDropdown(
    selectedPeriod: ConsistencyPeriod,
    onPeriodSelected: (ConsistencyPeriod) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box {
        Row(
            modifier = Modifier.clickable { expanded = true },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedPeriod.label,
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
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            ConsistencyPeriod.entries.forEach { period ->
                DropdownMenuItem(
                    text = { Text(text = period.label) },
                    onClick = {
                        onPeriodSelected(period)
                        expanded = false
                    }
                )
            }
        }
    }
}

private fun defaultWeeklyConsistencyEntries(): List<WeeklyConsistencyChartEntry> {
    val studiedDays = setOf(1, 2, 4)

    return listOf("M", "T", "W", "T", "F", "S", "S").mapIndexed { index, label ->
        WeeklyConsistencyChartEntry(
            dayLabel = label,
            hasStudied = index in studiedDays,
            isToday = false
        )
    }
}

private fun defaultMonthlyConsistencyEntries(): List<MonthlyConsistencyCalendarEntry> {
    val daysInMonth = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)
    val studiedDays = setOf(2, 3, 5, 8, 9, 13, 16, 17, 21, 24, 27, 30)

    return (1..daysInMonth).map { day ->
        MonthlyConsistencyCalendarEntry(
            dayOfMonth = day,
            hasStudied = day in studiedDays,
            isToday = false
        )
    }
}

private fun List<WeeklyConsistencyChartEntry>.withCurrentWeekDayMarked(
    hasStudiedToday: Boolean
): List<WeeklyConsistencyChartEntry> {
    val todayIndex = todayWeekIndex()
    return mapIndexed { index, entry ->
        if (index == todayIndex) {
            entry.copy(
                hasStudied = entry.hasStudied || hasStudiedToday,
                isToday = true
            )
        } else {
            entry
        }
    }
}

private fun List<MonthlyConsistencyCalendarEntry>.withCurrentMonthDayMarked(
    hasStudiedToday: Boolean
): List<MonthlyConsistencyCalendarEntry> {
    val today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    return map { entry ->
        if (entry.dayOfMonth == today) {
            entry.copy(
                hasStudied = entry.hasStudied || hasStudiedToday,
                isToday = true
            )
        } else {
            entry
        }
    }
}

private fun todayWeekIndex(): Int {
    val dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
    return (dayOfWeek + 5) % 7
}

private fun currentMonthFirstDayOffset(): Int {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, 1)
    }
    return (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7
}

@Composable
private fun ChartLegend() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LegendItem(color = chartStudied, text = "Studied")
        LegendItem(color = chartMuted, text = "No Study")
        LegendItem(color = chartToday, text = "Today")
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
private fun ConsistencyChartPreview() {
    ConsistencyChart()
}

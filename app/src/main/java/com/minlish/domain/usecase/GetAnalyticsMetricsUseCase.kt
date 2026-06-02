package com.minlish.domain.usecase

import com.minlish.core.constant.SrsRating
import com.minlish.domain.model.ReviewLog
import com.minlish.domain.repository.AnalyticsRepository
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

data class AnalyticsMetrics(
    val masteredWordsThisWeek: Int = 0,
    val atRiskWordsThisWeek: Int = 0,
    val weeklyEasyReviews: Int = 0,
    val weeklyTotalReviews: Int = 0,
    val retentionRate: Float = 0f,
    val hasStudiedToday: Boolean = false,
    val weeklyStudyDays: List<WeeklyStudyDayMetric> = emptyList(),
    val monthlyStudyDays: List<MonthlyStudyDayMetric> = emptyList(),
    val firstMonthDayOffset: Int = 0
)

data class WeeklyStudyDayMetric(
    val dayIndex: Int,
    val hasStudied: Boolean,
    val isToday: Boolean
)

data class MonthlyStudyDayMetric(
    val dayOfMonth: Int,
    val hasStudied: Boolean,
    val isToday: Boolean
)

class GetAnalyticsMetricsUseCase(
    private val analyticsRepository: AnalyticsRepository
) {
    suspend operator fun invoke(userId: String): AnalyticsMetrics {
        if (userId.isBlank()) throw IllegalArgumentException("User ID cannot be empty")

        val zone = ZoneId.systemDefault()
        val today = LocalDate.now(zone)
        val weekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val weekEnd = weekStart.plusDays(DAYS_IN_WEEK.toLong())
        val monthStart = today.withDayOfMonth(1)
        val monthEnd = monthStart.plusMonths(1)

        val reviewLogs = analyticsRepository.getReviewLogs(userId)
        val logsByDate = reviewLogs
            .mapNotNull { log -> log.reviewDate(zone)?.let { date -> log to date } }

        val weeklyLogs = logsByDate
            .filter { (_, date) -> date >= weekStart && date < weekEnd }
            .map { (log, _) -> log }
        val weeklyEasyReviews = weeklyLogs.count { it.rating == SrsRating.EASY }
        val weeklyTotalReviews = weeklyLogs.size
        val hasStudiedToday = logsByDate.any { (_, date) -> date == today }

        return AnalyticsMetrics(
            masteredWordsThisWeek = weeklyEasyReviews,
            atRiskWordsThisWeek = weeklyLogs.count { it.rating == SrsRating.AGAIN },
            weeklyEasyReviews = weeklyEasyReviews,
            weeklyTotalReviews = weeklyTotalReviews,
            retentionRate = if (weeklyTotalReviews > 0) {
                weeklyEasyReviews / weeklyTotalReviews.toFloat()
            } else {
                0f
            },
            hasStudiedToday = hasStudiedToday,
            weeklyStudyDays = buildWeeklyStudyDays(
                weekStart = weekStart,
                today = today,
                studiedDates = logsByDate.map { (_, date) -> date }.toSet()
            ),
            monthlyStudyDays = buildMonthlyStudyDays(
                monthStart = monthStart,
                monthEnd = monthEnd,
                today = today,
                studiedDates = logsByDate.map { (_, date) -> date }.toSet()
            ),
            firstMonthDayOffset = monthStart.mondayFirstDayOffset()
        )
    }

    private fun buildWeeklyStudyDays(
        weekStart: LocalDate,
        today: LocalDate,
        studiedDates: Set<LocalDate>
    ): List<WeeklyStudyDayMetric> {
        return (0 until DAYS_IN_WEEK).map { index ->
            val date = weekStart.plusDays(index.toLong())
            WeeklyStudyDayMetric(
                dayIndex = index,
                hasStudied = date in studiedDates,
                isToday = date == today
            )
        }
    }

    private fun buildMonthlyStudyDays(
        monthStart: LocalDate,
        monthEnd: LocalDate,
        today: LocalDate,
        studiedDates: Set<LocalDate>
    ): List<MonthlyStudyDayMetric> {
        val daysInMonth = monthEnd.minusDays(1).dayOfMonth
        return (1..daysInMonth).map { day ->
            val date = monthStart.withDayOfMonth(day)
            MonthlyStudyDayMetric(
                dayOfMonth = day,
                hasStudied = date in studiedDates,
                isToday = date == today
            )
        }
    }

    private fun ReviewLog.reviewDate(zone: ZoneId): LocalDate? {
        if (reviewedAt <= 0L) return null

        val epochMillis = if (reviewedAt < EPOCH_MILLIS_THRESHOLD) {
            reviewedAt * MILLIS_PER_SECOND
        } else {
            reviewedAt
        }
        return Instant.ofEpochMilli(epochMillis).atZone(zone).toLocalDate()
    }

    private fun LocalDate.mondayFirstDayOffset(): Int {
        return dayOfWeek.value - 1
    }

    private companion object {
        const val DAYS_IN_WEEK = 7
        const val MILLIS_PER_SECOND = 1000L
        const val EPOCH_MILLIS_THRESHOLD = 100_000_000_000L
    }
}

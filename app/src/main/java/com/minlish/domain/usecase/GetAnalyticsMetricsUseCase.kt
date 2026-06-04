package com.minlish.domain.usecase

import com.minlish.core.constant.SrsRating
import com.minlish.domain.model.DailyActivity
import com.minlish.domain.model.ReviewLog
import com.minlish.domain.model.UserProgress
import com.minlish.domain.repository.AnalyticsRepository
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

data class AnalyticsMetrics(
    val masteredWordsThisWeek: Int = 0,
    val atRiskWordsThisWeek: Int = 0,
    val weeklyRememberedReviews: Int = 0,
    val weeklyTotalReviews: Int = 0,
    val retentionRate: Float = 0f,
    val hasStudiedToday: Boolean = false,
    val weeklyStudyDays: List<WeeklyStudyDayMetric> = emptyList(),
    val monthlyStudyDays: List<MonthlyStudyDayMetric> = emptyList(),
    val firstMonthDayOffset: Int = 0,
    val retentionLevels: List<RetentionLevelMetric> = emptyList(),
    val wordsReadyForReview: Int = 0
)

data class RetentionLevelMetric(
    val label: String,
    val count: Int,
    val intervalRange: String
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

        val dailyActivities = analyticsRepository.getDailyActivities(userId)
        val reviewLogs = analyticsRepository.getReviewLogs(userId)
        val userProgresses = analyticsRepository.getUserProgresses(userId)
        val now = System.currentTimeMillis()
        val logsByDate = reviewLogs
            .mapNotNull { log -> log.reviewDate(zone)?.let { date -> log to date } }
        val activitiesByDate = dailyActivities
            .mapNotNull { activity -> activity.activityDate()?.let { date -> activity to date } }
        val studiedDates = logsByDate.map { (_, date) -> date }.toSet() +
            activitiesByDate
                .filter { (activity, _) -> activity.hasStudyActivity() }
                .map { (_, date) -> date }

        val weeklyLogs = logsByDate
            .filter { (_, date) -> date >= weekStart && date < weekEnd }
            .map { (log, _) -> log }
        val weeklyActivities = activitiesByDate
            .filter { (_, date) -> date >= weekStart && date < weekEnd }
            .map { (activity, _) -> activity }
        val weeklyTotalReviews = weeklyLogs.totalReviewsOrFallback(weeklyActivities)
        val weeklyRememberedReviews = weeklyLogs.rememberedReviewsOrFallback(weeklyActivities)
        val masteredWordsThisWeek = weeklyLogs.countNewlyMasteredWordsOrFallback(userProgresses)
        val atRiskWordsThisWeek = weeklyLogs.countAtRiskWordsOrFallback(userProgresses)
        val hasStudiedToday = today in studiedDates

        return AnalyticsMetrics(
            masteredWordsThisWeek = masteredWordsThisWeek,
            atRiskWordsThisWeek = atRiskWordsThisWeek,
            weeklyRememberedReviews = weeklyRememberedReviews,
            weeklyTotalReviews = weeklyTotalReviews,
            retentionRate = if (weeklyTotalReviews > 0) {
                weeklyRememberedReviews / weeklyTotalReviews.toFloat()
            } else {
                0f
            },
            hasStudiedToday = hasStudiedToday,
            weeklyStudyDays = buildWeeklyStudyDays(
                weekStart = weekStart,
                today = today,
                studiedDates = studiedDates
            ),
            monthlyStudyDays = buildMonthlyStudyDays(
                monthStart = monthStart,
                monthEnd = monthEnd,
                today = today,
                studiedDates = studiedDates
            ),
            firstMonthDayOffset = monthStart.mondayFirstDayOffset(),
            retentionLevels = buildRetentionLevels(userProgresses),
            wordsReadyForReview = userProgresses.count { progress ->
                progress.nextReviewDate.toEpochMillisOrNull()?.let { it <= now } == true
            }
        )
    }

    private fun buildRetentionLevels(
        progresses: List<UserProgress>
    ): List<RetentionLevelMetric> {
        val reviewedProgresses = progresses.filter { progress ->
            progress.repetition > 0 || progress.interval > 0 || progress.lastRating.isNotBlank()
        }

        return listOf(
            RetentionLevelMetric(
                label = "Level 1",
                count = reviewedProgresses.count { it.interval < 3 },
                intervalRange = "I < 3 days"
            ),
            RetentionLevelMetric(
                label = "Level 2",
                count = reviewedProgresses.count { it.interval in 3..7 },
                intervalRange = "3 <= I <= 7 days"
            ),
            RetentionLevelMetric(
                label = "Level 3",
                count = reviewedProgresses.count { it.interval in 8..21 },
                intervalRange = "8 <= I <= 21 days"
            ),
            RetentionLevelMetric(
                label = "Level 4",
                count = reviewedProgresses.count { it.interval in 22..45 },
                intervalRange = "22 <= I <= 45 days"
            ),
            RetentionLevelMetric(
                label = "Level 5",
                count = reviewedProgresses.count { it.interval > 45 },
                intervalRange = "I > 45 days"
            )
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
        val epochMillis = reviewedAt.toEpochMillisOrNull() ?: return null
        return Instant.ofEpochMilli(epochMillis).atZone(zone).toLocalDate()
    }

    private fun DailyActivity.activityDate(): LocalDate? {
        return runCatching { LocalDate.parse(date) }.getOrNull()
    }

    private fun DailyActivity.hasStudyActivity(): Boolean {
        return newWordsLearned > 0 || reviewsCompleted > 0 || totalAnswers > 0
    }

    private fun Long.toEpochMillisOrNull(): Long? {
        if (this <= 0L) return null
        return if (this < EPOCH_MILLIS_THRESHOLD) {
            this * MILLIS_PER_SECOND
        } else {
            this
        }
    }

    private fun SrsRating.isRemembered(): Boolean {
//        return this != SrsRating.AGAIN
        return this == SrsRating.EASY
    }

    private fun List<ReviewLog>.totalReviewsOrFallback(
        weeklyActivities: List<DailyActivity>
    ): Int {
        if (isNotEmpty()) return size

        return weeklyActivities.sumOf { activity ->
            activity.totalAnswers.coerceAtLeast(activity.reviewsCompleted)
        }
    }

    private fun List<ReviewLog>.rememberedReviewsOrFallback(
        weeklyActivities: List<DailyActivity>
    ): Int {
        if (isNotEmpty()) return count { it.rating.isRemembered() }

        return weeklyActivities.sumOf { activity ->
            activity.correctAnswers.coerceIn(0, activity.totalAnswers.coerceAtLeast(activity.reviewsCompleted))
        }
    }

    private fun List<ReviewLog>.countNewlyMasteredWordsOrFallback(
        progresses: List<UserProgress>
    ): Int {
        if (isEmpty()) {
            return progresses.count { it.repetition > 0 }
        }

        return filter { log ->
            log.rating.isRemembered() &&
                log.previousIntervalDays < MASTERED_INTERVAL_DAYS &&
                log.nextIntervalDays >= MASTERED_INTERVAL_DAYS
        }
            .map { it.wordId }
            .distinct()
            .size
    }

    private fun List<ReviewLog>.countAtRiskWordsOrFallback(
        progresses: List<UserProgress>
    ): Int {
        if (isEmpty()) {
            return progresses.count { it.lastRating == SrsRating.AGAIN.name }
        }

        return filter { it.rating == SrsRating.AGAIN }
            .map { it.wordId }
            .distinct()
            .size
    }

    private fun LocalDate.mondayFirstDayOffset(): Int {
        return dayOfWeek.value - 1
    }

    private companion object {
        const val DAYS_IN_WEEK = 7
        const val MILLIS_PER_SECOND = 1000L
        const val EPOCH_MILLIS_THRESHOLD = 100_000_000_000L
        const val MASTERED_INTERVAL_DAYS = 21
    }
}

package com.minlish.domain.usecase

import com.minlish.domain.model.ProgressSnapshot
import com.minlish.domain.model.DailyPlan
import com.minlish.domain.model.Deck
import com.minlish.domain.repository.DashboardRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class DashboardMetrics(
    val wordsLearned: Int = 0,
    val streakDays: Int = 0,
    val dailyGoalPercent: Int = 0,
    val timeRemaining: Int = 0,
    val weeklyProgress: Int = 0,
    val currentDeckTag: String = "Travel",
    val currentDeckSubtitle: String = "Common Verbs Deck",
    val deckProgress: Int = 0
)

class GetDashboardMetricsUseCase(
    private val dashboardRepository: DashboardRepository
) {
    suspend operator fun invoke(userId: String): DashboardMetrics {
        return try {
            val progressSnapshot = dashboardRepository.getProgressSnapshot(userId)
            val dailyPlan = dashboardRepository.getDailyPlan(userId)
            val currentDeck = dashboardRepository.getCurrentDeck(userId)

            // Calculate daily goal percent
            val dailyGoalPercent = if (progressSnapshot != null && dailyPlan != null) {
                calculateDailyGoalPercent(progressSnapshot, dailyPlan)
            } else {
                0
            }

            // Calculate deck progress
            val deckProgress = if (currentDeck != null) {
                if (currentDeck.totalWordCount > 0) {
                    (currentDeck.learnedWordCount * 100) / currentDeck.totalWordCount
                } else {
                    0
                }
            } else {
                0
            }

            DashboardMetrics(
                wordsLearned = progressSnapshot?.wordsLearned ?: 0,
                streakDays = progressSnapshot?.streakDays ?: 0,
                dailyGoalPercent = dailyGoalPercent,
                timeRemaining = calculateTimeRemaining(),
                weeklyProgress = calculateWeeklyProgress(progressSnapshot),
                currentDeckTag = currentDeck?.tags?.firstOrNull() ?: "Travel",
                currentDeckSubtitle = currentDeck?.title ?: "Common Verbs Deck",
                deckProgress = deckProgress
            )
        } catch (e: Exception) {
            DashboardMetrics()
        }
    }

    private fun calculateDailyGoalPercent(
        progressSnapshot: ProgressSnapshot,
        dailyPlan: DailyPlan
    ): Int {
        // Get today's activity
        val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        val todayActivity = progressSnapshot.dailyActivities.find { it.date == today }

        return if (todayActivity != null && dailyPlan.newWordsTarget > 0) {
            val newWordsCompleted = todayActivity.newWordsLearned
            (newWordsCompleted * 100) / dailyPlan.newWordsTarget
        } else {
            0
        }
    }

    private fun calculateWeeklyProgress(progressSnapshot: ProgressSnapshot?): Int {
        if (progressSnapshot == null || progressSnapshot.dailyActivities.isEmpty()) {
            return 0
        }

        // Get last 7 days
        val lastSevenDays = (0..6).mapNotNull { daysAgo ->
            val date = LocalDate.now().minusDays(daysAgo.toLong())
                .format(DateTimeFormatter.ISO_DATE)
            progressSnapshot.dailyActivities.find { it.date == date }
        }

        if (lastSevenDays.isEmpty()) return 0

        // Calculate average accuracy for the week
        val totalCorrect = lastSevenDays.sumOf { it.correctAnswers }
        val totalAnswers = lastSevenDays.sumOf { it.totalAnswers }

        return if (totalAnswers > 0) {
            (totalCorrect * 100) / totalAnswers
        } else {
            0
        }
    }

    private fun calculateTimeRemaining(): Int {
        // For now, calculate based on time remaining in the day
        val now = java.time.LocalTime.now()
        val endOfDay = java.time.LocalTime.of(23, 59, 59)
        
        val duration = java.time.Duration.between(now, endOfDay)
        return (duration.toMinutes()).toInt()
    }
}


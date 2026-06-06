package com.minlish.domain.usecase

import com.minlish.domain.model.ProgressSnapshot
import com.minlish.domain.repository.DashboardRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class DashboardMetrics(
    val wordsLearned: Int = 0,
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
            val decks = dashboardRepository.getDecksForUser(userId)
            val currentDeck = decks.maxByOrNull { it.updatedAt }
            val wordsLearned = decks.sumOf { it.learnedWordCount }

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
                wordsLearned = wordsLearned,
                weeklyProgress = calculateWeeklyProgress(progressSnapshot),
                currentDeckTag = currentDeck?.tags?.firstOrNull() ?: "Travel",
                currentDeckSubtitle = currentDeck?.title ?: "Common Verbs Deck",
                deckProgress = deckProgress
            )
        } catch (_: Exception) {
            DashboardMetrics()
        }
    }

    private fun calculateWeeklyProgress(progressSnapshot: ProgressSnapshot?): Int {
        if (progressSnapshot == null || progressSnapshot.dailyActivities.isEmpty()) {
            return 0
        }

        // Get last 7 days
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val lastSevenDays = (0..6).mapNotNull { daysAgo ->
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
            val date = dateFormat.format(calendar.time)
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
}


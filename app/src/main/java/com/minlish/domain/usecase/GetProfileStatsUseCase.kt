package com.minlish.domain.usecase

import com.minlish.core.constant.DeckStatus
import com.minlish.domain.model.DailyActivity
import com.minlish.domain.model.ProfileStats
import com.minlish.domain.repository.ProfileRepository
import kotlin.math.ceil
import kotlin.math.roundToInt

class GetProfileStatsUseCase(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(userId: String): ProfileStats {
        if (userId.isBlank()) throw IllegalArgumentException("User ID cannot be empty")

        val progress = profileRepository.getProgressSnapshot(userId)
        val decks = profileRepository.getDecksForUser(userId)
        val (totalReviewsFromLogs, perfectScoresFromLogs) = profileRepository.getReviewCounts(userId)
        val dailyActivities = progress?.dailyActivities.orEmpty()

        val totalReviewsFromActivity = dailyActivities.totalReviews()
        val totalReviews = totalReviewsFromActivity.takeIf { it > 0 } ?: totalReviewsFromLogs
        val perfectScoresFromActivity = dailyActivities.sumOf { it.correctAnswers }
        val perfectScores = perfectScoresFromLogs.takeIf { it > 0 } ?: perfectScoresFromActivity
        val wordsLearnedFromDecks = decks.sumOf { it.learnedWordCount }
        val wordsLearned = wordsLearnedFromDecks.takeIf { it > 0 } ?: (progress?.wordsLearned ?: 0)
        val streakDays = progress?.streakDays ?: 0

        return ProfileStats(
            decksMastered = decks.count { it.status == DeckStatus.MASTERED },
            perfectScores = perfectScores,
            wordsLearned = wordsLearned,
            studyTimeHours = estimateStudyTimeHours(wordsLearned, totalReviews),
            xp = calculateXp(wordsLearned, totalReviews, decks.count { it.status == DeckStatus.MASTERED }, streakDays),
            streakDays = streakDays,
            accuracyRate = calculateAccuracyRate(dailyActivities, progress?.accuracyRate ?: 0.0)
        )
    }

    private fun List<DailyActivity>.totalReviews(): Int {
        return sumOf { activity ->
            activity.totalAnswers.coerceAtLeast(activity.reviewsCompleted)
        }
    }

    private fun calculateAccuracyRate(
        dailyActivities: List<DailyActivity>,
        fallback: Double
    ): Double {
        val totalAnswers = dailyActivities.sumOf { it.totalAnswers }
        if (totalAnswers <= 0) return fallback

        val correctAnswers = dailyActivities.sumOf { it.correctAnswers }
        return (correctAnswers * 100.0 / totalAnswers).roundToInt().toDouble()
    }

    private fun estimateStudyTimeHours(wordsLearned: Int, totalReviews: Int): Int {
        val estimatedMinutes = wordsLearned * MINUTES_PER_NEW_WORD + totalReviews * MINUTES_PER_REVIEW
        return ceil(estimatedMinutes / MINUTES_PER_HOUR.toDouble()).toInt()
    }

    private fun calculateXp(
        wordsLearned: Int,
        totalReviews: Int,
        decksMastered: Int,
        streakDays: Int
    ): Int {
        return wordsLearned * XP_PER_WORD +
            totalReviews * XP_PER_REVIEW +
            decksMastered * XP_PER_MASTERED_DECK +
            streakDays * XP_PER_STREAK_DAY
    }

    private companion object {
        const val MINUTES_PER_NEW_WORD = 2
        const val MINUTES_PER_REVIEW = 1
        const val MINUTES_PER_HOUR = 60
        const val XP_PER_WORD = 10
        const val XP_PER_REVIEW = 2
        const val XP_PER_MASTERED_DECK = 100
        const val XP_PER_STREAK_DAY = 5
    }
}

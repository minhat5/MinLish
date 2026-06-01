package com.minlish.data.repository

import com.minlish.data.mapper.toDomain
import com.minlish.data.remote.FirebaseProfileService
import com.minlish.domain.model.ProfileStats
import com.minlish.domain.repository.ProfileRepository
import kotlin.math.ceil

class ProfileRepositoryImpl(
    private val firebaseProfileService: FirebaseProfileService
) : ProfileRepository {

    override suspend fun getProfileStats(userId: String): ProfileStats {
        val progress = firebaseProfileService.getProgressSnapshot(userId)?.toDomain()
        val decksMastered = firebaseProfileService.countMasteredDecks(userId)
        val reviewStats = firebaseProfileService.getReviewStats(userId)

        val wordsLearned = progress?.wordsLearned ?: 0
        val streakDays = progress?.streakDays ?: 0
        val accuracyRate = progress?.accuracyRate ?: 0.0
        val totalReviews = progress?.dailyActivities
            ?.sumOf { it.reviewsCompleted }
            ?.takeIf { it > 0 }
            ?: reviewStats.totalReviews

        return ProfileStats(
            decksMastered = decksMastered,
            perfectScores = reviewStats.perfectScores,
            wordsLearned = wordsLearned,
            studyTimeHours = estimateStudyTimeHours(wordsLearned, totalReviews),
            xp = calculateXp(wordsLearned, totalReviews, decksMastered, streakDays),
            streakDays = streakDays,
            accuracyRate = accuracyRate
        )
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

package com.minlish.data.repository

import com.minlish.data.mapper.toDomain
import com.minlish.data.remote.FirebaseProfileService
import com.minlish.domain.model.Deck
import com.minlish.domain.model.ProgressSnapshot
import com.minlish.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val firebaseProfileService: FirebaseProfileService
) : ProfileRepository {

    override suspend fun getProgressSnapshot(userId: String): ProgressSnapshot? {
        return firebaseProfileService.getProgressSnapshot(userId)?.toDomain()
    }

    override suspend fun getDecksForUser(userId: String): List<Deck> {
        return firebaseProfileService.getDecksForUser(userId)
    }

    override suspend fun getReviewCounts(userId: String): Pair<Int, Int> {
        val reviewStats = firebaseProfileService.getReviewStats(userId)
        return reviewStats.totalReviews to reviewStats.perfectScores
    }
}

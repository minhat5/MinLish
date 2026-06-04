package com.minlish.domain.repository

import com.minlish.domain.model.Deck
import com.minlish.domain.model.ProgressSnapshot

interface ProfileRepository {
    suspend fun getProgressSnapshot(userId: String): ProgressSnapshot?
    suspend fun getDecksForUser(userId: String): List<Deck>
    suspend fun getReviewCounts(userId: String): Pair<Int, Int>
}

package com.minlish.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.minlish.core.constant.DeckStatus
import com.minlish.core.constant.SrsRating
import com.minlish.data.dto.DeckDto
import com.minlish.data.dto.ProgressSnapshotDto
import com.minlish.data.mapper.toDomain
import com.minlish.domain.model.Deck
import kotlinx.coroutines.tasks.await

class FirebaseProfileService(
    private val firestore: FirebaseFirestore
) {
    suspend fun getProgressSnapshot(userId: String): ProgressSnapshotDto? {
        val directDocument = firestore.collection(FirebaseCollections.PROGRESS)
            .document(userId)
            .get()
            .await()

        val directProgress = directDocument.toObject(ProgressSnapshotDto::class.java)
        if (directProgress != null) {
            return if (directProgress.userId.isBlank()) {
                directProgress.copy(userId = userId)
            } else {
                directProgress
            }
        }

        val querySnapshot = firestore.collection(FirebaseCollections.PROGRESS)
            .whereEqualTo("userId", userId)
            .limit(1)
            .get()
            .await()

        return querySnapshot.documents
            .firstOrNull()
            ?.toObject(ProgressSnapshotDto::class.java)
    }

    suspend fun countMasteredDecks(userId: String): Int {
        return firestore.collection(FirebaseCollections.DECKS)
            .whereEqualTo("ownerId", userId)
            .whereEqualTo("status", DeckStatus.MASTERED.name)
            .get()
            .await()
            .size()
    }

    suspend fun getDecksForUser(userId: String): List<Deck> {
        return firestore.collection(FirebaseCollections.DECKS)
            .whereEqualTo("ownerId", userId)
            .get()
            .await()
            .documents
            .mapNotNull { document ->
                document.toObject(DeckDto::class.java)?.let { dto ->
                    val deckId = dto.id.ifBlank { document.id }
                    dto.copy(id = deckId).toDomain()
                }
            }
    }

    suspend fun getReviewStats(userId: String): ReviewStatsDto {
        val reviewLogs = firestore.collection(FirebaseCollections.REVIEW_LOGS)
            .whereEqualTo("userId", userId)
            .get()
            .await()

        val perfectScores = reviewLogs.documents.count { document ->
            document.getString("rating") == SrsRating.EASY.name
        }

        return ReviewStatsDto(
            totalReviews = reviewLogs.size(),
            perfectScores = perfectScores
        )
    }
}

data class ReviewStatsDto(
    val totalReviews: Int = 0,
    val perfectScores: Int = 0
)

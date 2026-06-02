package com.minlish.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.minlish.data.dto.ReviewLogDto
import com.minlish.data.mapper.toDomain
import com.minlish.data.remote.FirebaseCollections
import com.minlish.domain.model.ReviewLog
import com.minlish.domain.repository.AnalyticsRepository
import kotlinx.coroutines.tasks.await

class AnalyticsRepositoryImpl(
    private val firebaseFirestore: FirebaseFirestore
) : AnalyticsRepository {

    override suspend fun getReviewLogs(userId: String): List<ReviewLog> {
        return firebaseFirestore
            .collection(FirebaseCollections.REVIEW_LOGS)
            .whereEqualTo("userId", userId)
            .get()
            .await()
            .documents
            .mapNotNull { document ->
                document.toObject(ReviewLogDto::class.java)
                    ?.let { dto ->
                        if (dto.id.isBlank()) {
                            dto.copy(id = document.id)
                        } else {
                            dto
                        }
                    }
                    ?.toDomain()
            }
    }
}

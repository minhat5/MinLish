package com.minlish.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.minlish.data.dto.ProgressSnapshotDto
import com.minlish.data.dto.ReviewLogDto
import com.minlish.data.mapper.toDomain
import com.minlish.data.remote.FirebaseCollections
import com.minlish.domain.model.DailyActivity
import com.minlish.domain.model.ReviewLog
import com.minlish.domain.model.UserProgress
import com.minlish.domain.repository.AnalyticsRepository
import kotlinx.coroutines.tasks.await

class AnalyticsRepositoryImpl(
    private val firebaseFirestore: FirebaseFirestore
) : AnalyticsRepository {

    override suspend fun getDailyActivities(userId: String): List<DailyActivity> {
        return firebaseFirestore
            .collection(FirebaseCollections.PROGRESS)
            .document(userId)
            .get()
            .await()
            .toObject(ProgressSnapshotDto::class.java)
            ?.dailyActivities
            ?.map { activity ->
                DailyActivity(
                    date = activity.date,
                    newWordsLearned = activity.newWordsLearned,
                    reviewsCompleted = activity.reviewsCompleted,
                    totalAnswers = activity.totalAnswers,
                    correctAnswers = activity.correctAnswers
                )
            }
            ?: emptyList()
    }

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

    override suspend fun getUserProgresses(userId: String): List<UserProgress> {
        return firebaseFirestore
            .collection(FirebaseCollections.USERS)
            .document(userId)
            .collection(FirebaseCollections.PROGRESS)
            .get()
            .await()
            .documents
            .mapNotNull { document ->
                document.toObject(UserProgress::class.java)?.let { progress ->
                    val vocabId = progress.vocabId.ifBlank { document.id }
                    progress.copy(vocabId = vocabId)
                }
            }
    }
}

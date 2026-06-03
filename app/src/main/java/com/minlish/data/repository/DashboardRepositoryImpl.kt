package com.minlish.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.minlish.data.dto.ProgressSnapshotDto
import com.minlish.data.dto.DailyPlanDto
import com.minlish.data.dto.DeckDto
import com.minlish.data.mapper.toDomain
import com.minlish.domain.model.ProgressSnapshot
import com.minlish.domain.model.DailyPlan
import com.minlish.domain.model.Deck
import com.minlish.domain.repository.DashboardRepository
import com.minlish.data.remote.FirebaseCollections
import kotlinx.coroutines.tasks.await

class DashboardRepositoryImpl(
    private val firebaseFirestore: FirebaseFirestore
) : DashboardRepository {

    override suspend fun getProgressSnapshot(userId: String): ProgressSnapshot? {
        return try {
            val snapshot = firebaseFirestore
                .collection(FirebaseCollections.PROGRESS)
                .document(userId)
                .get()
                .await()

            snapshot.toObject(ProgressSnapshotDto::class.java)?.toDomain()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getDailyPlan(userId: String): DailyPlan? {
        return try {
            val snapshot = firebaseFirestore
                .collection(FirebaseCollections.DAILY_PLANS)
                .document(userId)
                .get()
                .await()

            snapshot.toObject(DailyPlanDto::class.java)?.let {
                DailyPlan(
                    userId = it.userId,
                    newWordsTarget = it.newWordsTarget,
                    reviewWordsTarget = it.reviewWordsTarget,
                    timezone = it.timezone,
                    generatedAt = it.generatedAt
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getCurrentDeck(userId: String): Deck? {
        return try {
            getDecksForUser(userId).maxByOrNull { it.updatedAt }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getDecksForUser(userId: String): List<Deck> {
        return try {
            val snapshot = firebaseFirestore
                .collection(FirebaseCollections.DECKS)
                .whereEqualTo("ownerId", userId)
                .get()
                .await()

            snapshot.documents
                .mapNotNull { it.toObject(DeckDto::class.java)?.toDomain() }
                .sortedByDescending { it.updatedAt }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getTodayDailyActivity(userId: String): Long? {
        return try {
            val snapshot = firebaseFirestore
                .collection(FirebaseCollections.PROGRESS)
                .document(userId)
                .get()
                .await()

            val progressDto = snapshot.toObject(ProgressSnapshotDto::class.java)
            // This is not used in the current implementation
            null
        } catch (e: Exception) {
            null
        }
    }
}



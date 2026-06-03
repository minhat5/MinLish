package com.minlish.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.minlish.data.remote.FirebaseCollections
import com.minlish.domain.model.UserProgress
import com.minlish.domain.model.Vocabulary
import com.minlish.domain.repository.FlashcardRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import com.minlish.data.dto.ProgressSnapshotDto
import com.minlish.data.dto.DailyActivityDto
import com.minlish.data.dto.UserProfileDto
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class FlashcardRepositoryImpl(
    private val firestore: FirebaseFirestore
) : FlashcardRepository {
    override fun getVocabulariesByDeck(deckId: String): Flow<List<Vocabulary>> = callbackFlow {
        val registration = firestore.collection(FirebaseCollections.VOCABULARIES)
            .whereEqualTo("deckId", deckId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val vocabularies = snapshot?.documents
                    ?.mapNotNull { it.toObject(Vocabulary::class.java) }
                    ?.sortedBy { it.word.lowercase() }
                    ?: emptyList()

                trySend(vocabularies)
            }

        awaitClose { registration.remove() }
    }

    override fun getUserProgressMap(userId: String, deckId: String): Flow<Map<String, UserProgress>> = callbackFlow {
        val registration = firestore.collection(FirebaseCollections.USERS)
            .document(userId)
            .collection(FirebaseCollections.PROGRESS)
            .whereEqualTo("deckId", deckId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val progressMap = snapshot?.documents
                    ?.mapNotNull { document ->
                        document.toObject(UserProgress::class.java)?.let { progress ->
                            val vocabId = progress.vocabId.ifBlank { document.id }
                            vocabId to progress.copy(vocabId = vocabId)
                        }
                    }
                    ?.toMap()
                    ?: emptyMap()

                trySend(progressMap)
            }

        awaitClose { registration.remove() }
    }

    override fun updateUserProgress(userId: String, progress: UserProgress): Task<Void> {
        return firestore.collection(FirebaseCollections.USERS)
            .document(userId)
            .collection(FirebaseCollections.PROGRESS)
            .document(progress.vocabId)
            .set(progress)
    }

    override suspend fun updateSessionProgress(
        userId: String,
        deckId: String,
        progresses: List<UserProgress>,
        learnedWordCount: Int,
        totalWordCount: Int,
        deckStatus: String
    ): Int {
        // 1. Fetch current user profile
        val userRef = firestore.collection(FirebaseCollections.USERS).document(userId)
        val userSnapshot = try {
            userRef.get().await().toObject(UserProfileDto::class.java)
        } catch (e: Exception) {
            null
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val today = dateFormat.format(Date())

        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -1)
        val yesterday = dateFormat.format(cal.time)

        var currentStreak = userSnapshot?.streak ?: 0
        val lastStudyDate = userSnapshot?.lastStudyDate

        val studiedTodayAlready = lastStudyDate == today
        val studiedYesterday = lastStudyDate == yesterday

        if (!studiedYesterday && !studiedTodayAlready) {
            currentStreak = 0
        }
        if (!studiedTodayAlready) {
            currentStreak += 1
        }

        // 2. Fetch progress snapshot to keep dailyActivities in sync
        val progressSnapshotRef = firestore.collection(FirebaseCollections.PROGRESS)
            .document(userId)
        val snapshot = try {
            progressSnapshotRef.get().await().toObject(ProgressSnapshotDto::class.java)
        } catch (e: Exception) {
            null
        }

        val dailyActivities = snapshot?.dailyActivities?.toMutableList() ?: mutableListOf()
        val todayActivityIndex = dailyActivities.indexOfFirst { it.date == today }

        val sessionReviewsCount = progresses.size
        val sessionEasyCount = progresses.count { it.lastRating == "EASY" }

        val todayActivity = if (todayActivityIndex >= 0) {
            val existing = dailyActivities[todayActivityIndex]
            existing.copy(
                reviewsCompleted = existing.reviewsCompleted + sessionReviewsCount,
                totalAnswers = existing.totalAnswers + sessionReviewsCount,
                correctAnswers = existing.correctAnswers + sessionEasyCount
            )
        } else {
            DailyActivityDto(
                date = today,
                reviewsCompleted = sessionReviewsCount,
                totalAnswers = sessionReviewsCount,
                correctAnswers = sessionEasyCount
            )
        }

        if (todayActivityIndex >= 0) {
            dailyActivities[todayActivityIndex] = todayActivity
        } else {
            dailyActivities.add(todayActivity)
        }

        val updatedProgressDto = (snapshot ?: ProgressSnapshotDto(userId = userId)).copy(
            streakDays = currentStreak,
            dailyActivities = dailyActivities,
            updatedAt = System.currentTimeMillis()
        )

        // 3. Commit batch
        val batch = firestore.batch()

        progresses.forEach { progress ->
            val progressRef = firestore.collection(FirebaseCollections.USERS)
                .document(userId)
                .collection(FirebaseCollections.PROGRESS)
                .document(progress.vocabId)
            batch.set(progressRef, progress)
        }

        val deckRef = firestore.collection(FirebaseCollections.DECKS)
            .document(deckId)
        batch.set(
            deckRef,
            mapOf(
                "learnedWordCount" to learnedWordCount,
                "totalWordCount" to totalWordCount,
                "status" to deckStatus,
                "updatedAt" to System.currentTimeMillis()
            ),
            SetOptions.merge()
        )

        // Update user profile document directly
        batch.update(
            userRef,
            mapOf(
                "streak" to currentStreak,
                "lastStudyDate" to today,
                "updatedAt" to System.currentTimeMillis()
            )
        )

        batch.set(progressSnapshotRef, updatedProgressDto)

        batch.commit().await()
        return currentStreak
    }
}

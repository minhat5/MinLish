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

    override fun updateSessionProgress(
        userId: String,
        deckId: String,
        progresses: List<UserProgress>,
        learnedWordCount: Int,
        totalWordCount: Int,
        deckStatus: String
    ): Task<Void> {
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

        return batch.commit()
    }
}

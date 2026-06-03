package com.minlish.data.remote

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.minlish.core.constant.DeckStatus
import com.minlish.data.dto.DeckDto
import com.minlish.data.mapper.toDomain
import com.minlish.domain.model.Deck
import com.minlish.domain.model.UserProgress
import com.minlish.domain.model.Vocabulary
import kotlinx.coroutines.tasks.await

class FirebaseDeckService(
    private val firestore: FirebaseFirestore
) {
    suspend fun getDecksForUser(userId: String): List<Deck> {
        val snapshot = firestore.collection(FirebaseCollections.DECKS)
            .whereEqualTo("ownerId", userId)
            .get()
            .await()

        val decks = snapshot.documents
            .mapNotNull { it.toObject(DeckDto::class.java)?.toDomain() }
            .sortedByDescending { it.updatedAt }

        return decks.map { deck -> refreshDeckProgress(userId, deck) }
    }

    suspend fun getDeckById(deckId: String): Deck? {
        return firestore.collection(FirebaseCollections.DECKS)
            .document(deckId)
            .get()
            .await()
            .toObject(DeckDto::class.java)
            ?.toDomain()
    }

    suspend fun createDeck(
        ownerId: String,
        title: String,
        description: String,
        iconKey: String,
        themeColorHex: String
    ): Deck {
        val deckRef = firestore.collection(FirebaseCollections.DECKS).document()
        val now = System.currentTimeMillis()
        val deckDto = DeckDto(
            id = deckRef.id,
            ownerId = ownerId,
            title = title,
            description = description,
            iconKey = iconKey,
            themeColorHex = themeColorHex,
            status = DeckStatus.NEW.name,
            learnedWordCount = 0,
            totalWordCount = 0,
            createdAt = now,
            updatedAt = now
        )

        deckRef.set(deckDto).await()
        return deckDto.toDomain()
    }

    suspend fun createVocabulary(
        ownerId: String,
        deckId: String,
        word: String,
        phonetic: String,
        meaning: String,
        example: String
    ) {
        val vocabularyRef = firestore.collection(FirebaseCollections.VOCABULARIES).document()
        val deckRef = firestore.collection(FirebaseCollections.DECKS).document(deckId)
        val now = System.currentTimeMillis()

        val vocabularyData = mapOf(
            "id" to vocabularyRef.id,
            "ownerId" to ownerId,
            "deckId" to deckId,
            "word" to word,
            "phonetic" to phonetic,
            "meaning" to meaning,
            "example" to example,
            "createdAt" to now,
            "updatedAt" to now
        )

        val batch = firestore.batch()
        batch.set(vocabularyRef, vocabularyData)
        batch.update(
            deckRef,
            mapOf(
                "totalWordCount" to FieldValue.increment(1),
                "updatedAt" to now
            )
        )
        batch.commit().await()
    }

    suspend fun getVocabularyForDeck(deckId: String): List<Vocabulary> {
        val snapshot = firestore.collection(FirebaseCollections.VOCABULARIES)
            .whereEqualTo("deckId", deckId)
            .get()
            .await()

        return snapshot.documents
            .mapNotNull { it.toObject(Vocabulary::class.java) }
            .sortedBy { it.word.lowercase() }
    }

    private suspend fun refreshDeckProgress(userId: String, deck: Deck): Deck {
        val vocabularies = getVocabularyForDeck(deck.id)
        val progressSnapshot = firestore.collection(FirebaseCollections.USERS)
            .document(userId)
            .collection(FirebaseCollections.PROGRESS)
            .whereEqualTo("deckId", deck.id)
            .get()
            .await()

        val progressMap = progressSnapshot.documents
            .mapNotNull { document ->
                document.toObject(UserProgress::class.java)?.let { progress ->
                    val vocabId = progress.vocabId.ifBlank { document.id }
                    vocabId to progress.copy(vocabId = vocabId)
                }
            }
            .toMap()

        val now = System.currentTimeMillis()
        val dueOrNewCount = vocabularies.count { vocabulary ->
            val progress = progressMap[vocabulary.toVocabId(deck.id)]
            progress == null || progress.nextReviewDate <= now
        }
        val totalWordCount = vocabularies.size
        val learnedWordCount = (totalWordCount - dueOrNewCount).coerceAtLeast(0)
        val status = when {
            totalWordCount > 0 && learnedWordCount >= totalWordCount -> DeckStatus.MASTERED
            learnedWordCount > 0 -> DeckStatus.LEARNING
            else -> DeckStatus.NEW
        }

        val refreshedDeck = deck.copy(
            learnedWordCount = learnedWordCount,
            totalWordCount = totalWordCount,
            status = status,
            updatedAt = now
        )

        if (
            deck.learnedWordCount != learnedWordCount ||
            deck.totalWordCount != totalWordCount ||
            deck.status != status
        ) {
            firestore.collection(FirebaseCollections.DECKS)
                .document(deck.id)
                .set(
                    mapOf(
                        "learnedWordCount" to learnedWordCount,
                        "totalWordCount" to totalWordCount,
                        "status" to status.name,
                        "updatedAt" to now
                    ),
                    SetOptions.merge()
                )
                .await()
        }

        return refreshedDeck
    }

    private fun Vocabulary.toVocabId(deckId: String): String {
        return "${deckId}_${word.trim().lowercase()}"
    }
}

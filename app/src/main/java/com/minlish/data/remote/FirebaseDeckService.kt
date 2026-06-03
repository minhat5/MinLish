package com.minlish.data.remote

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.minlish.core.constant.DeckStatus
import com.minlish.data.dto.DeckDto
import com.minlish.data.mapper.toDomain
import com.minlish.domain.model.Deck
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

        return snapshot.documents
            .mapNotNull { it.toObject(DeckDto::class.java)?.toDomain() }
            .sortedByDescending { it.updatedAt }
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
}

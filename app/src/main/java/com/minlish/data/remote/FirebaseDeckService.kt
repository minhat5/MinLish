package com.minlish.data.remote

import com.google.firebase.firestore.FirebaseFirestore
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

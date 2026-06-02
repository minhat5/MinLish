package com.minlish.domain.repository

import com.google.android.gms.tasks.Task
import com.minlish.domain.model.UserProgress
import com.minlish.domain.model.Vocabulary
import kotlinx.coroutines.flow.Flow

interface FlashcardRepository {
    fun getVocabulariesByDeck(deckId: String): Flow<List<Vocabulary>>

    fun getUserProgressMap(userId: String, deckId: String): Flow<Map<String, UserProgress>>

    fun updateUserProgress(userId: String, progress: UserProgress): Task<Void>
}
 
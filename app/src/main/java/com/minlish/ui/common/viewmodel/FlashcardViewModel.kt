package com.minlish.ui.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.minlish.core.constant.DeckStatus
import com.minlish.di.AppContainer
import com.minlish.domain.model.UserProgress
import com.minlish.domain.model.Vocabulary
import com.minlish.domain.repository.FlashcardRepository
import com.minlish.domain.srs.Sm2Scheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.minlish.ui.common.state.StreakState

data class FlashcardUiState(
    val isLoading: Boolean = false,
    val queue: List<Vocabulary> = emptyList(),
    val currentVocabulary: Vocabulary? = null,
    val currentIndex: Int = 0,
    val totalCount: Int = 0,
    val isSubmitting: Boolean = false,
    val isCompleted: Boolean = false,
    val errorMessage: String? = null,
    val wordsCount: Int = 0,
    val accuracy: Int = 0
)

class FlashcardViewModel(
    private val deckId: String,
    private val userId: String,
    private val repository: FlashcardRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(FlashcardUiState(isLoading = true))
    val uiState: StateFlow<FlashcardUiState> = _uiState.asStateFlow()

    private var baseProgressMap: Map<String, UserProgress> = emptyMap()
    private var sessionVocabularies: List<Vocabulary> = emptyList()
    private val pendingProgressMap = linkedMapOf<String, UserProgress>()
    private var totalVocabularyCount: Int = 0
    private var initialQueueSize: Int = 0
    private var easySelectedCount: Int = 0

    val calculateAccuracy: (Int, Int) -> Int = { easyCount, totalCount ->
        if (totalCount > 0) {
            ((easyCount.toDouble() / totalCount) * 100).toInt()
        } else {
            0
        }
    }

    init {
        StreakState.currentUserId = userId
        loadSessionQueue()
    }

    private fun loadSessionQueue() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val vocabularies = repository.getVocabulariesByDeck(deckId).first()
                val progressMap = repository.getUserProgressMap(userId, deckId).first()
                val queue = buildDueQueue(vocabularies, progressMap)

                baseProgressMap = progressMap
                sessionVocabularies = vocabularies
                totalVocabularyCount = vocabularies.size
                initialQueueSize = queue.size
                easySelectedCount = 0

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        queue = queue,
                        currentVocabulary = queue.firstOrNull(),
                        currentIndex = if (queue.isEmpty()) 0 else 1,
                        totalCount = queue.size,
                        isCompleted = false,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to load flashcards."
                    )
                }
            }
        }
    }

    fun onAnswerSelected(quality: Int) {
        if (_uiState.value.isSubmitting) return

        val vocabulary = _uiState.value.currentVocabulary ?: return
        if (quality == 5) {
            easySelectedCount++
        }

        val vocabId = vocabulary.toVocabId()
        val oldProgress = pendingProgressMap[vocabId] ?: baseProgressMap[vocabId]
        val sm2Result = Sm2Scheduler.calculate(
            quality = quality,
            oldEaseFactor = oldProgress?.easeFactor ?: 2.5,
            oldRepetition = oldProgress?.repetition ?: 0,
            oldInterval = oldProgress?.interval ?: 0
        )

        pendingProgressMap[vocabId] = UserProgress(
            vocabId = vocabId,
            deckId = deckId,
            easeFactor = sm2Result.easeFactor,
            repetition = sm2Result.repetition,
            interval = sm2Result.interval,
            nextReviewDate = sm2Result.nextReviewDate,
            lastRating = quality.toRatingLabel()
        )

        val remainingQueue = _uiState.value.queue.drop(1)
        if (remainingQueue.isEmpty()) {
            finishSession()
        } else {
            _uiState.update { state ->
                state.copy(
                    queue = remainingQueue,
                    currentVocabulary = remainingQueue.first(),
                    currentIndex = state.currentIndex + 1,
                    errorMessage = null
                )
            }
        }
    }

    private fun finishSession() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }
            try {
                val mergedProgress = baseProgressMap + pendingProgressMap
                val learnedWordCount = calculateLearnedWordCount(sessionVocabularies, mergedProgress)
                val deckStatus = when {
                    totalVocabularyCount > 0 && learnedWordCount >= totalVocabularyCount -> DeckStatus.MASTERED.name
                    learnedWordCount > 0 -> DeckStatus.LEARNING.name
                    else -> DeckStatus.NEW.name
                }

                val updatedStreak = repository.updateSessionProgress(
                    userId = userId,
                    deckId = deckId,
                    progresses = pendingProgressMap.values.toList(),
                    learnedWordCount = learnedWordCount,
                    totalWordCount = totalVocabularyCount,
                    deckStatus = deckStatus
                )
                StreakState.streakCount = updatedStreak

                baseProgressMap = mergedProgress
                pendingProgressMap.clear()

                val accuracy = calculateAccuracy(easySelectedCount, initialQueueSize)

                _uiState.update {
                    it.copy(
                        queue = emptyList(),
                        currentVocabulary = null,
                        currentIndex = 0,
                        isSubmitting = false,
                        isCompleted = true,
                        errorMessage = null,
                        wordsCount = initialQueueSize,
                        accuracy = accuracy
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        errorMessage = e.message ?: "Failed to save study progress."
                    )
                }
            }
        }
    }

    private fun buildDueQueue(
        vocabularies: List<Vocabulary>,
        progressMap: Map<String, UserProgress>
    ): List<Vocabulary> {
        val now = System.currentTimeMillis()
        return vocabularies
            .filter { vocabulary ->
                val progress = progressMap[vocabulary.toVocabId()]
                progress == null || progress.nextReviewDate <= now
            }
            .shuffled()
    }

    private fun calculateLearnedWordCount(
        vocabularies: List<Vocabulary>,
        progressMap: Map<String, UserProgress>
    ): Int {
        val now = System.currentTimeMillis()
        val dueOrNewCount = vocabularies.count { vocabulary ->
            val progress = progressMap[vocabulary.toVocabId()]
            progress == null || progress.nextReviewDate <= now
        }
        return (vocabularies.size - dueOrNewCount).coerceAtLeast(0)
    }

    private fun Vocabulary.toVocabId(): String {
        return "${deckId}_${word.trim().lowercase()}"
    }

    private fun Int.toRatingLabel(): String {
        return when (this) {
            1 -> "AGAIN"
            3 -> "HARD"
            4 -> "GOOD"
            5 -> "EASY"
            else -> "UNKNOWN"
        }
    }
}

class FlashcardViewModelFactory(
    private val deckId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlashcardViewModel::class.java)) {
            val userId = AppContainer.getCurrentUserId()
                ?: throw IllegalStateException("User must be logged in to study flashcards.")

            @Suppress("UNCHECKED_CAST")
            return FlashcardViewModel(
                deckId = deckId,
                userId = userId,
                repository = AppContainer.flashcardRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

package com.minlish.ui.screen.flashcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.minlish.di.AppContainer
import com.minlish.domain.model.UserProgress
import com.minlish.domain.model.Vocabulary
import com.minlish.domain.repository.FlashcardRepository
import com.minlish.domain.srs.Sm2Scheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class FlashcardUiState(
    val isLoading: Boolean = false,
    val queue: List<Vocabulary> = emptyList(),
    val currentVocabulary: Vocabulary? = null,
    val progressMap: Map<String, UserProgress> = emptyMap(),
    val currentIndex: Int = 0,
    val totalCount: Int = 0,
    val isSubmitting: Boolean = false,
    val isCompleted: Boolean = false,
    val errorMessage: String? = null
)

class FlashcardViewModel(
    private val deckId: String,
    private val userId: String,
    private val repository: FlashcardRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(FlashcardUiState(isLoading = true))
    val uiState: StateFlow<FlashcardUiState> = _uiState.asStateFlow()

    init {
        observeStudyQueue()
    }

    private fun observeStudyQueue() {
        viewModelScope.launch {
            combine(
                repository.getVocabulariesByDeck(deckId),
                repository.getUserProgressMap(userId, deckId)
            ) { vocabularies, progressMap ->
                progressMap to buildDueQueue(vocabularies, progressMap)
            }.catch { e ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to load flashcards."
                    )
                }
            }.collect { (progressMap, dueQueue) ->
                _uiState.update { currentState ->
                    val queue = currentState.mergeWithLatestDueQueue(dueQueue)
                    val currentVocabulary = queue.firstOrNull()
                    val nextIndex = currentState.nextIndexFor(currentVocabulary)
                    currentState.copy(
                        isLoading = false,
                        queue = queue,
                        currentVocabulary = currentVocabulary,
                        progressMap = progressMap,
                        currentIndex = nextIndex,
                        totalCount = if (currentState.totalCount > 0) currentState.totalCount else queue.size,
                        isCompleted = currentVocabulary == null,
                        errorMessage = null
                    )
                }
            }
        }
    }

    fun onAnswerSelected(quality: Int) {
        if (_uiState.value.isSubmitting) return

        val vocabulary = _uiState.value.currentVocabulary ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }

            try {
                val vocabId = vocabulary.toVocabId()
                val oldProgress = _uiState.value.progressMap[vocabId]
                val sm2Result = Sm2Scheduler.calculate(
                    quality = quality,
                    oldEaseFactor = oldProgress?.easeFactor ?: 2.5,
                    oldRepetition = oldProgress?.repetition ?: 0,
                    oldInterval = oldProgress?.interval ?: 0
                )

                val updatedProgress = UserProgress(
                    vocabId = vocabId,
                    deckId = deckId,
                    easeFactor = sm2Result.easeFactor,
                    repetition = sm2Result.repetition,
                    interval = sm2Result.interval,
                    nextReviewDate = sm2Result.nextReviewDate,
                    lastRating = quality.toRatingLabel()
                )

                repository.updateUserProgress(userId, updatedProgress).await()
                moveToNextCard()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        errorMessage = e.message ?: "Failed to update progress."
                    )
                }
            }
        }
    }

    private fun moveToNextCard() {
        _uiState.update { state ->
            val remainingQueue = state.queue.drop(1)
            val nextVocabulary = remainingQueue.firstOrNull()
            state.copy(
                queue = remainingQueue,
                currentVocabulary = nextVocabulary,
                currentIndex = if (nextVocabulary == null) 0 else state.currentIndex + 1,
                isSubmitting = false,
                isCompleted = nextVocabulary == null,
                errorMessage = null
            )
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

    private fun FlashcardUiState.mergeWithLatestDueQueue(latestDueQueue: List<Vocabulary>): List<Vocabulary> {
        if (queue.isEmpty() || isCompleted) {
            return latestDueQueue
        }

        val latestIds = latestDueQueue.map { it.toVocabId() }.toSet()
        val currentIds = queue.map { it.toVocabId() }.toSet()
        val preservedQueue = queue.filter { it.toVocabId() in latestIds }
        val newItems = latestDueQueue.filter { it.toVocabId() !in currentIds }
        return preservedQueue + newItems
    }

    private fun FlashcardUiState.nextIndexFor(nextVocabulary: Vocabulary?): Int {
        if (nextVocabulary == null) return 0
        if (currentIndex > 0 && currentVocabulary?.toVocabId() == nextVocabulary.toVocabId()) {
            return currentIndex
        }
        return if (currentIndex > 0 && queue.isNotEmpty()) currentIndex else 1
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

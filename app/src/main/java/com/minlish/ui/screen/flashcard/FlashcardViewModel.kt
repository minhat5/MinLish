package com.minlish.ui.screen.flashcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.minlish.di.AppContainer
import com.minlish.domain.model.Vocabulary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FlashcardUiState(
    val isLoading: Boolean = false,
    val vocabularies: List<Vocabulary> = emptyList(),
    val errorMessage: String? = null
)

class FlashcardViewModel(
    private val deckId: String,
    private val getVocabularyForDeck: suspend (String) -> List<Vocabulary>
) : ViewModel() {
    private val _uiState = MutableStateFlow(FlashcardUiState(isLoading = true))
    val uiState: StateFlow<FlashcardUiState> = _uiState.asStateFlow()

    init {
        loadVocabulary()
    }

    fun loadVocabulary() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val vocabularies = getVocabularyForDeck(deckId)
                _uiState.update {
                    it.copy(isLoading = false, vocabularies = vocabularies, errorMessage = null)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message ?: "Failed to load vocabulary.")
                }
            }
        }
    }
}

class FlashcardViewModelFactory(
    private val deckId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlashcardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FlashcardViewModel(
                deckId = deckId,
                getVocabularyForDeck = AppContainer.firebaseDeckService::getVocabularyForDeck
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

package com.minlish.ui.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.minlish.di.AppContainer
import com.minlish.domain.model.Deck
import com.minlish.domain.model.Vocabulary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DeckDetailUiState(
    val isLoading: Boolean = true,
    val deck: Deck? = null,
    val vocabularies: List<Vocabulary> = emptyList(),
    val errorMessage: String? = null
)

class DeckDetailViewModel(
    private val deckId: String,
    private val getDeckById: suspend (String) -> Deck?,
    private val getVocabularyForDeck: suspend (String) -> List<Vocabulary>
) : ViewModel() {
    private val _uiState = MutableStateFlow(DeckDetailUiState())
    val uiState: StateFlow<DeckDetailUiState> = _uiState.asStateFlow()

    init {
        loadDeckDetail()
    }

    fun loadDeckDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val deck = getDeckById(deckId)
                val vocabularies = getVocabularyForDeck(deckId)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        deck = deck,
                        vocabularies = vocabularies,
                        errorMessage = if (deck == null) "Deck not found." else null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to load deck."
                    )
                }
            }
        }
    }
}

class DeckDetailViewModelFactory(
    private val deckId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeckDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DeckDetailViewModel(
                deckId = deckId,
                getDeckById = AppContainer.firebaseDeckService::getDeckById,
                getVocabularyForDeck = AppContainer.firebaseDeckService::getVocabularyForDeck
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

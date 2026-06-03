package com.minlish.ui.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.minlish.di.AppContainer
import com.minlish.domain.model.Deck
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DeckUiState(
    val isLoading: Boolean = false,
    val decks: List<Deck> = emptyList(),
    val errorMessage: String? = null,
    val deckTitle: String = "",
    val deckDescription: String = "",
    val selectedIconKey: String = "book",
    val selectedThemeColorHex: String = "#E8E0F0",
    val isSubmitting: Boolean = false,
    val createSuccess: Boolean = false
)

class DeckViewModel(
    private val getCurrentUserId: () -> String?,
    private val getDecksForUser: suspend (String) -> List<Deck>,
    private val createDeckForUser: suspend (String, String, String, String, String) -> Deck
) : ViewModel() {
    private val _uiState = MutableStateFlow(DeckUiState(isLoading = true))
    val uiState: StateFlow<DeckUiState> = _uiState.asStateFlow()

    init {
        loadDecks()
    }

    fun loadDecks() {
        viewModelScope.launch {
            val userId = getCurrentUserId()
            if (userId.isNullOrBlank()) {
                _uiState.update {
                    it.copy(isLoading = false, decks = emptyList(), errorMessage = "Please login to view your decks.")
                }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val decks = getDecksForUser(userId)
                _uiState.update { it.copy(isLoading = false, decks = decks, errorMessage = null) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message ?: "Failed to load decks.")
                }
            }
        }
    }

    fun onDeckTitleChange(value: String) {
        _uiState.update { it.copy(deckTitle = value, createSuccess = false, errorMessage = null) }
    }

    fun onDeckDescriptionChange(value: String) {
        _uiState.update { it.copy(deckDescription = value, createSuccess = false, errorMessage = null) }
    }

    fun onDeckIconSelected(iconKey: String) {
        _uiState.update { it.copy(selectedIconKey = iconKey, createSuccess = false) }
    }

    fun onThemeColorSelected(colorHex: String) {
        _uiState.update { it.copy(selectedThemeColorHex = colorHex, createSuccess = false) }
    }

    fun clearCreateSuccess() {
        _uiState.update { it.copy(createSuccess = false) }
    }

    fun createDeck() {
        val state = _uiState.value
        val title = state.deckTitle.trim()
        val description = state.deckDescription.trim()

        if (title.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter a deck title.") }
            return
        }

        viewModelScope.launch {
            val userId = getCurrentUserId()
            if (userId.isNullOrBlank()) {
                _uiState.update {
                    it.copy(isSubmitting = false, errorMessage = "Please login to create a deck.")
                }
                return@launch
            }

            _uiState.update { it.copy(isSubmitting = true, errorMessage = null, createSuccess = false) }
            try {
                val createdDeck = createDeckForUser(
                    userId,
                    title,
                    description,
                    state.selectedIconKey,
                    state.selectedThemeColorHex
                )
                _uiState.update {
                    it.copy(
                        decks = listOf(createdDeck) + it.decks,
                        deckTitle = "",
                        deckDescription = "",
                        isSubmitting = false,
                        errorMessage = null,
                        createSuccess = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isSubmitting = false, errorMessage = e.message ?: "Failed to create deck.")
                }
            }
        }
    }
}

class DeckViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeckViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DeckViewModel(
                getCurrentUserId = AppContainer::getCurrentUserId,
                getDecksForUser = AppContainer.firebaseDeckService::getDecksForUser,
                createDeckForUser = AppContainer.firebaseDeckService::createDeck
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

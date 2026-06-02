package com.minlish.ui.screen.deck

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
    val errorMessage: String? = null
)

class DeckViewModel(
    private val getCurrentUserId: () -> String?,
    private val getDecksForUser: suspend (String) -> List<Deck>
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
}

class DeckViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeckViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DeckViewModel(
                getCurrentUserId = AppContainer::getCurrentUserId,
                getDecksForUser = AppContainer.firebaseDeckService::getDecksForUser
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

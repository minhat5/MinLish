package com.minlish.ui.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.minlish.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddVocabularyUiState(
    val word: String = "",
    val phonetic: String = "",
    val meaning: String = "",
    val example: String = "",
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val createSuccess: Boolean = false
)

class AddVocabularyViewModel(
    private val deckId: String,
    private val getCurrentUserId: () -> String?,
    private val createVocabulary: suspend (String, String, String, String, String, String) -> Unit
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddVocabularyUiState())
    val uiState: StateFlow<AddVocabularyUiState> = _uiState.asStateFlow()

    fun onWordChange(value: String) {
        _uiState.update { it.copy(word = value, errorMessage = null, createSuccess = false) }
    }

    fun onPhoneticChange(value: String) {
        _uiState.update { it.copy(phonetic = value, errorMessage = null, createSuccess = false) }
    }

    fun onMeaningChange(value: String) {
        _uiState.update { it.copy(meaning = value, errorMessage = null, createSuccess = false) }
    }

    fun onExampleChange(value: String) {
        _uiState.update { it.copy(example = value, errorMessage = null, createSuccess = false) }
    }

    fun clearCreateSuccess() {
        _uiState.update { it.copy(createSuccess = false) }
    }

    fun addVocabulary() {
        val state = _uiState.value
        val word = state.word.trim()
        val phonetic = state.phonetic.trim()
        val meaning = state.meaning.trim()
        val example = state.example.trim()

        if (deckId.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Deck not found.") }
            return
        }
        if (word.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter an English word.") }
            return
        }
        if (meaning.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter a definition.") }
            return
        }

        viewModelScope.launch {
            val ownerId = getCurrentUserId()
            if (ownerId.isNullOrBlank()) {
                _uiState.update { it.copy(errorMessage = "Please login to add words.") }
                return@launch
            }

            _uiState.update { it.copy(isSubmitting = true, errorMessage = null, createSuccess = false) }
            try {
                createVocabulary(ownerId, deckId, word, phonetic, meaning, example)
                _uiState.update {
                    it.copy(
                        word = "",
                        phonetic = "",
                        meaning = "",
                        example = "",
                        isSubmitting = false,
                        errorMessage = null,
                        createSuccess = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isSubmitting = false, errorMessage = e.message ?: "Failed to add word.")
                }
            }
        }
    }
}

class AddVocabularyViewModelFactory(
    private val deckId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddVocabularyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddVocabularyViewModel(
                deckId = deckId,
                getCurrentUserId = AppContainer::getCurrentUserId,
                createVocabulary = AppContainer.firebaseDeckService::createVocabulary
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

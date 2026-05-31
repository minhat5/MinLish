package com.minlish.ui.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.minlish.di.AppContainer
import com.minlish.domain.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val currentUser: UserProfile? = null,
    val dailyGoalPercent: Int = 0,
    val timeRemaining: Int = 0,
    val wordsLearned: Int = 0,
    val weeklyProgress: Int = 0,
    val currentDeckTag: String = "Travel",
    val currentDeckSubtitle: String = "Common Verbs Deck",
    val deckProgress: Int = 0
)

class HomeViewModel(
    private val getCurrentUser: suspend () -> UserProfile?
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val user = getCurrentUser.invoke()
                if (user != null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            currentUser = user,
                            dailyGoalPercent = 20,
                            timeRemaining = 50,
                            wordsLearned = 363,
                            weeklyProgress = 36,
                            deckProgress = 36
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "User not found"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to load user data"
                    )
                }
            }
        }
    }

    fun refresh() {
        loadUserData()
    }
}

class HomeViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(
                getCurrentUser = AppContainer.getCurrentUserUseCase::invoke
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


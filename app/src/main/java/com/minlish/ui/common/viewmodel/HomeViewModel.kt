package com.minlish.ui.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.minlish.di.AppContainer
import com.minlish.domain.model.UserProfile
import com.minlish.domain.usecase.DashboardMetrics
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.minlish.ui.common.state.StreakState

data class HomeUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val currentUser: UserProfile? = null,
    val wordsLearned: Int = 0,
    val weeklyProgress: Int = 0,
    val streakDays: Int = 0,
    val currentDeckTag: String = "Travel",
    val currentDeckSubtitle: String = "Common Verbs Deck",
    val deckProgress: Int = 0
)

class HomeViewModel(
    private val getCurrentUser: suspend () -> UserProfile?,
    private val getDashboardMetrics: suspend (userId: String) -> DashboardMetrics
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
                    StreakState.currentUserId = user.id
                    val metrics = getDashboardMetrics.invoke(user.id)
                    StreakState.streakCount = user.streak
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            currentUser = user,
                            wordsLearned = metrics.wordsLearned,
                            weeklyProgress = metrics.weeklyProgress,
                            streakDays = user.streak,
                            currentDeckTag = metrics.currentDeckTag,
                            currentDeckSubtitle = metrics.currentDeckSubtitle,
                            deckProgress = metrics.deckProgress
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
                getCurrentUser = AppContainer.getCurrentUserUseCase::invoke,
                getDashboardMetrics = AppContainer.getDashboardMetricsUseCase::invoke
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


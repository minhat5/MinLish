package com.minlish.ui.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.minlish.di.AppContainer
import com.minlish.domain.model.ProfileStats
import com.minlish.domain.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val currentUser: UserProfile? = null,
    val stats: ProfileStats = ProfileStats()
)

class ProfileViewModel(
    private val getCurrentUser: suspend () -> UserProfile?,
    private val getProfileStats: suspend (String) -> ProfileStats
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun refresh() {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val user = getCurrentUser()
                if (user == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "User not found"
                        )
                    }
                    return@launch
                }

                val stats = getProfileStats(user.id)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        currentUser = user,
                        stats = stats
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to load profile"
                    )
                }
            }
        }
    }
}

class ProfileViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(
                getCurrentUser = AppContainer.getCurrentUserUseCase::invoke,
                getProfileStats = AppContainer.getProfileStatsUseCase::invoke
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

package com.minlish.ui.common.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.minlish.data.api.DictionaryApi
import com.minlish.data.api.model.toWordResponse
import com.minlish.di.AppContainer
import com.minlish.domain.model.ProfileStats
import com.minlish.domain.model.UserProfile
import com.minlish.domain.model.WordResponse
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

class VocabularyViewModel(private val api: DictionaryApi) : ViewModel() {
    var uiState by mutableStateOf<WordResponse?>(null)

    fun loadWord(word: String) {
        viewModelScope.launch {
            try {
                val response = api.getWordDefinition(word)
                uiState = response.toWordResponse()
            } catch (e: Exception) {
                uiState = null
            }
        }
    }

    companion object {
        fun provideFactory(api: DictionaryApi): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                if (modelClass.isAssignableFrom(VocabularyViewModel::class.java)) {
                    return VocabularyViewModel(api) as T
                }
                throw IllegalArgumentException("Lớp ViewModel không hợp lệ")
            }
        }
    }
}
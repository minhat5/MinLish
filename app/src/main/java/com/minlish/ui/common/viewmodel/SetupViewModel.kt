package com.minlish.ui.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.minlish.core.constant.CefrLevel
import com.minlish.core.constant.LearningGoal
import com.minlish.core.constant.LevelEstimate
import com.minlish.di.AppContainer
import com.minlish.domain.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SetupUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class SetupViewModel(
    private val updateUserProfile: suspend (UserProfile) -> Unit,
    private val getCurrentUser: suspend () -> UserProfile?
) : ViewModel() {

    private val _uiState = MutableStateFlow(SetupUiState())
    val uiState: StateFlow<SetupUiState> = _uiState.asStateFlow()

    fun saveLevel(level: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val currentUser = getCurrentUser.invoke()
                if (currentUser != null) {
                    val levelEnum = when (level) {
                        "Beginner" -> LevelEstimate.BEGINNER
                        "Intermediate" -> LevelEstimate.INTERMEDIATE
                        "Advanced" -> LevelEstimate.ADVANCED
                        else -> LevelEstimate.BEGINNER
                    }
                    val updatedUser = currentUser.copy(levelEstimate = levelEnum)
                    updateUserProfile.invoke(updatedUser)
                    _uiState.update { it.copy(isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Failed to save level") }
            }
        }
    }

    fun saveCefrLevel(level: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val currentUser = getCurrentUser.invoke()
                if (currentUser != null) {
                    val cefrEnum = when (level) {
                        "A1" -> CefrLevel.A1
                        "A2" -> CefrLevel.A2
                        "B1" -> CefrLevel.B1
                        "B2" -> CefrLevel.B2
                        "C1" -> CefrLevel.C1
                        "C2" -> CefrLevel.C2
                        else -> null
                    }
                    val updatedUser = currentUser.copy(cefrLevel = cefrEnum)
                    updateUserProfile.invoke(updatedUser)
                    _uiState.update { it.copy(isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Failed to save CEFR level") }
            }
        }
    }

    fun saveLearningGoal(goal: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val currentUser = getCurrentUser.invoke()
                if (currentUser != null) {
                    val goalEnum = when (goal) {
                        "Communication" -> LearningGoal.COMMUNICATION
                        "Career" -> LearningGoal.BUSINESS
                        "Exam Preparation" -> LearningGoal.IELTS
                        "Travel" -> LearningGoal.TRAVEL
                        else -> LearningGoal.COMMUNICATION
                    }
                    val updatedUser = currentUser.copy(learningGoal = goalEnum)
                    updateUserProfile.invoke(updatedUser)
                    _uiState.update { it.copy(isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Failed to save learning goal") }
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }
}

class SetupViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SetupViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SetupViewModel(
                updateUserProfile = AppContainer.updateUserProfileUseCase::invoke,
                getCurrentUser = AppContainer.getCurrentUserUseCase::invoke
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}




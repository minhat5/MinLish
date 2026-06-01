package com.minlish.ui.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.minlish.di.AppContainer
import com.minlish.domain.exception.AuthException
import com.minlish.domain.model.UserProfile
import com.minlish.ui.screen.auth.AuthUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val register: suspend (String, String, String) -> UserProfile,
    private val login: suspend (String, String) -> UserProfile,
    private val resetPassword: suspend (String) -> Unit,
    private val logout: () -> Unit
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            try {
                val user = login.invoke(email, password)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "Login successful.",
                        currentUser = user
                    )
                }
            } catch (e: AuthException.LoginFailedException) {
                setError(e.message ?: "Invalid email or password.")
            } catch (e: IllegalArgumentException) {
                setError(e.message ?: "Invalid input.")
            } catch (e: Exception) {
                setError(e.message ?: "Login failed.")
            }
        }
    }

    fun register(name: String, email: String, password: String, confirmPassword: String) {
        if (password != confirmPassword) {
            setError("Passwords do not match.")
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            try {
                val user = register.invoke(email, password, name)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "Registration successful.",
                        currentUser = user
                    )
                }
            } catch (_: AuthException.EmailAlreadyInUseException) {
                setError("Email already in use.")
            } catch (_: AuthException.InvalidEmailException) {
                setError("Invalid email format.")
            } catch (e: AuthException.WeakPasswordException) {
                setError(e.message ?: "Password is too weak.")
            } catch (e: IllegalArgumentException) {
                setError(e.message ?: "Invalid input.")
            } catch (e: Exception) {
                setError(e.message ?: "Registration failed.")
            }
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            try {
                resetPassword.invoke(email)
                _uiState.update { it.copy(isLoading = false, successMessage = "Password reset email sent.") }
            } catch (_: AuthException.InvalidEmailException) {
                setError("Invalid email format.")
            } catch (e: AuthException.ResetPasswordFailedException) {
                setError(e.message ?: "Reset password failed.")
            } catch (e: Exception) {
                setError(e.message ?: "Reset password failed.")
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }

    fun logout() {
        try {
            logout.invoke()
            _uiState.update { it.copy(currentUser = null, errorMessage = null) }
        } catch (e: Exception) {
            setError(e.message ?: "Logout failed.")
        }
    }

    private fun setError(message: String) {
        _uiState.update { it.copy(isLoading = false, errorMessage = message) }
    }
}

class AuthViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(
                register = AppContainer.registerUseCase::invoke,
                login = AppContainer.loginUseCase::invoke,
                resetPassword = AppContainer.resetPasswordUseCase::invoke,
                logout = AppContainer.logoutUseCase::invoke
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


package com.minlish.ui.screen.auth

import com.minlish.domain.model.UserProfile

data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val currentUser: UserProfile? = null
)


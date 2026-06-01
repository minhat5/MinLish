package com.minlish.domain.usecase

import com.minlish.domain.model.UserProfile
import com.minlish.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): UserProfile {
        // Validate inputs
        if (email.isBlank()) throw IllegalArgumentException("Email cannot be empty")
        if (password.isBlank()) throw IllegalArgumentException("Password cannot be empty")

        return authRepository.login(email, password)
    }
}


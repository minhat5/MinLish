package com.minlish.domain.usecase

import com.minlish.domain.model.UserProfile
import com.minlish.domain.repository.AuthRepository

class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        displayName: String
    ): UserProfile {
        // Validate inputs
        if (email.isBlank()) throw IllegalArgumentException("Email cannot be empty")
        if (password.isBlank()) throw IllegalArgumentException("Password cannot be empty")
        if (displayName.isBlank()) throw IllegalArgumentException("Display name cannot be empty")
        if (password.length < 6) throw IllegalArgumentException("Password must be at least 6 characters")
        if (!email.contains("@")) throw IllegalArgumentException("Invalid email format")

        return authRepository.register(email, password, displayName)
    }
}


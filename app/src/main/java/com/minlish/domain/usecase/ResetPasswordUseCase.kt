package com.minlish.domain.usecase

import com.minlish.domain.repository.AuthRepository

class ResetPasswordUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String) {
        if (email.isBlank()) throw IllegalArgumentException("Email cannot be empty")
        if (!email.contains("@")) throw IllegalArgumentException("Invalid email format")

        authRepository.resetPassword(email)
    }
}


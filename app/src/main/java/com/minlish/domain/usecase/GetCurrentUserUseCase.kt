package com.minlish.domain.usecase

import com.minlish.domain.model.UserProfile
import com.minlish.domain.repository.AuthRepository

class GetCurrentUserUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): UserProfile? {
        return authRepository.getCurrentUser()
    }
}


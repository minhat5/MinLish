package com.minlish.domain.usecase

import com.minlish.domain.repository.AuthRepository

class IsLoggedInUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Boolean {
        return authRepository.isLoggedIn()
    }
}


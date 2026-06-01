package com.minlish.domain.usecase

import com.minlish.domain.repository.AuthRepository

class LogoutUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke() {
        authRepository.logout()
    }
}


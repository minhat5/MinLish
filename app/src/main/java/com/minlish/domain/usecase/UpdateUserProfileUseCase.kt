package com.minlish.domain.usecase

import com.minlish.domain.model.UserProfile
import com.minlish.domain.repository.AuthRepository

class UpdateUserProfileUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(userProfile: UserProfile) {
        authRepository.updateUserProfile(userProfile)
    }
}


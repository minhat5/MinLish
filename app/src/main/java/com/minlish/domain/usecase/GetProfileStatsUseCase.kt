package com.minlish.domain.usecase

import com.minlish.domain.model.ProfileStats
import com.minlish.domain.repository.ProfileRepository

class GetProfileStatsUseCase(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(userId: String): ProfileStats {
        if (userId.isBlank()) throw IllegalArgumentException("User ID cannot be empty")
        return profileRepository.getProfileStats(userId)
    }
}

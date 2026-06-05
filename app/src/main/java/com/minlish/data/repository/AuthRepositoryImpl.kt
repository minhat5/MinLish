package com.minlish.data.repository

import com.minlish.data.mapper.toAuthResponse
import com.minlish.data.mapper.toDomain
import com.minlish.data.mapper.toDto
import com.minlish.data.remote.FirebaseAuthService
import com.minlish.domain.model.UserProfile
import com.minlish.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(
    private val firebaseAuthService: FirebaseAuthService
) : AuthRepository {

    override suspend fun register(
        email: String,
        password: String,
        displayName: String
    ): UserProfile {
        val authResponse = firebaseAuthService.register(email, password, displayName)
        return authResponse.toDomain()
    }

    override suspend fun login(email: String, password: String): UserProfile {
        val authResponse = firebaseAuthService.login(email, password)
        return authResponse.toDomain()
    }

    override fun logout() {
        firebaseAuthService.logout()
    }

    override suspend fun getCurrentUser(): UserProfile? {
        val authResponse = firebaseAuthService.getCurrentUser() ?: return null
        return authResponse.toDomain()
    }

    override fun getCurrentUserId(): String? {
        return firebaseAuthService.getCurrentUserId()
    }

    override fun isLoggedIn(): Boolean {
        return firebaseAuthService.isLoggedIn()
    }

    override suspend fun resetPassword(email: String) {
        firebaseAuthService.resetPassword(email)
    }

    override suspend fun updateUserProfile(userProfile: UserProfile) {
        val userProfileDto = userProfile.toDto()
        firebaseAuthService.updateUserProfile(userProfile.id, userProfileDto)
    }
}


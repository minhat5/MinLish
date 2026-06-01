package com.minlish.domain.repository

import com.minlish.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    /**
     * Register a new user
     */
    suspend fun register(
        email: String,
        password: String,
        displayName: String
    ): UserProfile

    /**
     * Login user
     */
    suspend fun login(email: String, password: String): UserProfile

    /**
     * Logout user
     */
    fun logout()

    /**
     * Get current logged in user
     */
    suspend fun getCurrentUser(): UserProfile?

    /**
     * Get current user ID
     */
    fun getCurrentUserId(): String?

    /**
     * Check if user is logged in
     */
    fun isLoggedIn(): Boolean

    /**
     * Observe authentication state
     */
    fun observeAuthState(): Flow<String?>

    /**
     * Reset password
     */
    suspend fun resetPassword(email: String)

    /**
     * Update user profile
     */
    suspend fun updateUserProfile(userProfile: UserProfile)
}


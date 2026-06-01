package com.minlish.data.session

import android.util.Log
import com.minlish.domain.usecase.GetCurrentUserUseCase
import com.minlish.domain.usecase.IsLoggedInUseCase
import com.minlish.domain.usecase.LoginUseCase
import com.minlish.domain.usecase.LogoutUseCase
import com.minlish.domain.usecase.RegisterUseCase
import com.minlish.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

/**
 * Session Manager to handle authentication state and user session
 * This can be used as a single source of truth for auth state
 */
class AuthSessionManager(
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val isLoggedInUseCase: IsLoggedInUseCase
) {
    companion object {
        private const val TAG = "AuthSessionManager"
    }

    suspend fun register(email: String, password: String, displayName: String): UserProfile {
        Log.d(TAG, "Registering user with email: $email")
        return registerUseCase(email, password, displayName)
    }

    suspend fun login(email: String, password: String): UserProfile {
        Log.d(TAG, "Logging in user with email: $email")
        return loginUseCase(email, password)
    }

    fun logout() {
        Log.d(TAG, "Logging out user")
        logoutUseCase()
    }

    suspend fun getCurrentUser(): UserProfile? {
        return getCurrentUserUseCase()
    }

    fun isLoggedIn(): Boolean {
        return isLoggedInUseCase()
    }
}


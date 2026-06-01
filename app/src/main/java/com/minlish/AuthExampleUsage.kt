package com.minlish

import android.util.Log
import com.minlish.di.AppContainer
import com.minlish.domain.exception.AuthException
import kotlinx.coroutines.runBlocking

/**
 * Example usage of authentication backend
 * This demonstrates how to use the auth system in your UI layer
 */
class AuthExampleUsage {

    fun exampleRegister() = runBlocking {
        try {
            val registerUseCase = AppContainer.registerUseCase
            val userProfile = registerUseCase(
                email = "newuser@example.com",
                password = "SecurePassword123",
                displayName = "New User"
            )
            Log.d("Auth", "Registration successful: ${userProfile.displayName}")
        } catch (e: AuthException.EmailAlreadyInUseException) {
            Log.e("Auth", "Email already in use")
        } catch (e: AuthException.WeakPasswordException) {
            Log.e("Auth", "Password is too weak: ${e.message}")
        } catch (e: AuthException.RegistrationFailedException) {
            Log.e("Auth", "Registration failed: ${e.message}")
        }
    }

    fun exampleLogin() = runBlocking {
        try {
            val loginUseCase = AppContainer.loginUseCase
            val userProfile = loginUseCase(
                email = "user@example.com",
                password = "SecurePassword123"
            )
            Log.d("Auth", "Login successful: ${userProfile.displayName}")
        } catch (e: AuthException.LoginFailedException) {
            Log.e("Auth", "Login failed: ${e.message}")
        }
    }

    fun exampleLogout() {
        val logoutUseCase = AppContainer.logoutUseCase
        logoutUseCase()
        Log.d("Auth", "User logged out")
    }

    fun exampleGetCurrentUser() = runBlocking {
        val getCurrentUserUseCase = AppContainer.getCurrentUserUseCase
        val userProfile = getCurrentUserUseCase()
        if (userProfile != null) {
            Log.d("Auth", "Current user: ${userProfile.email}")
        } else {
            Log.d("Auth", "No user logged in")
        }
    }

    fun exampleCheckLoginStatus() {
        val isLoggedInUseCase = AppContainer.isLoggedInUseCase
        if (isLoggedInUseCase()) {
            Log.d("Auth", "User is logged in")
        } else {
            Log.d("Auth", "User is not logged in")
        }
    }

    fun exampleResetPassword() = runBlocking {
        try {
            val resetPasswordUseCase = AppContainer.resetPasswordUseCase
            resetPasswordUseCase("user@example.com")
            Log.d("Auth", "Password reset email sent")
        } catch (e: AuthException.InvalidEmailException) {
            Log.e("Auth", "Invalid email")
        } catch (e: AuthException.ResetPasswordFailedException) {
            Log.e("Auth", "Reset failed: ${e.message}")
        }
    }

    fun exampleUpdateUserProfile() = runBlocking {
        try {
            val getCurrentUserUseCase = AppContainer.getCurrentUserUseCase
            val updateUserProfileUseCase = AppContainer.updateUserProfileUseCase

            val currentUser = getCurrentUserUseCase()
            currentUser?.let {
                val updatedUser = it.copy(
                    displayName = "Updated Name",
                    photoUrl = "https://example.com/photo.jpg"
                )
                updateUserProfileUseCase(updatedUser)
                Log.d("Auth", "Profile updated successfully")
            }
        } catch (e: Exception) {
            Log.e("Auth", "Update failed: ${e.message}")
        }
    }
}


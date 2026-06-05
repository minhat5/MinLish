package com.minlish.data.remote

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.minlish.data.dto.AuthResponseDto
import com.minlish.data.dto.UserProfileDto
import com.minlish.domain.exception.AuthException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseAuthService(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    companion object {
        private const val TAG = "FirebaseAuthService"
    }

    /**
     * Register a new user with email and password
     */
    suspend fun register(
        email: String,
        password: String,
        displayName: String
    ): AuthResponseDto {
        return try {
            // Create user account in Firebase Auth
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: throw AuthException.RegistrationFailedException("Failed to create user")

            // Create user profile in Firestore
            val userProfileDto = UserProfileDto(
                id = userId,
                email = email,
                displayName = displayName,
                photoUrl = null,
                learningGoal = "COMMUNICATION",
                levelEstimate = "BEGINNER",
                cefrLevel = null,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )

            firestore.collection(FirebaseCollections.USERS)
                .document(userId)
                .set(userProfileDto)
                .await()

            // Return auth response
            AuthResponseDto(
                id = userId,
                email = email,
                displayName = displayName,
                photoUrl = null,
                learningGoal = "COMMUNICATION",
                levelEstimate = "BEGINNER",
                cefrLevel = null,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
        } catch (e: FirebaseAuthWeakPasswordException) {
            Log.e(TAG, "Registration failed - weak password: ${e.message}")
            throw AuthException.WeakPasswordException(e.message ?: "Password is too weak")
        } catch (e: FirebaseAuthUserCollisionException) {
            Log.e(TAG, "Registration failed - email already in use: ${e.message}")
            throw AuthException.EmailAlreadyInUseException(email)
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Log.e(TAG, "Registration failed - invalid email: ${e.message}")
            throw AuthException.InvalidEmailException(email)
        } catch (e: AuthException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Registration failed: ${e.message}")
            throw AuthException.RegistrationFailedException(e.message ?: "Unknown error")
        }
    }

    /**
     * Login user with email and password
     */
    suspend fun login(email: String, password: String): AuthResponseDto {
        return try {
            // Sign in with email and password
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: throw AuthException.LoginFailedException("Failed to login")

            // Fetch user profile from Firestore
            val userProfile = firestore.collection(FirebaseCollections.USERS)
                .document(userId)
                .get()
                .await()
                .toObject(UserProfileDto::class.java)
                ?: throw AuthException.UserNotFoundException(userId)

            // Convert to auth response
            AuthResponseDto(
                id = userProfile.id,
                email = userProfile.email,
                displayName = userProfile.displayName,
                photoUrl = userProfile.photoUrl,
                learningGoal = userProfile.learningGoal,
                levelEstimate = userProfile.levelEstimate,
                cefrLevel = userProfile.cefrLevel,
                streak = userProfile.streak,
                lastStudyDate = userProfile.lastStudyDate,
                createdAt = userProfile.createdAt,
                updatedAt = userProfile.updatedAt
            )
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Log.e(TAG, "Login failed - invalid credentials: ${e.message}")
            throw AuthException.LoginFailedException("Invalid email or password")
        } catch (e: AuthException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Login failed: ${e.message}")
            throw AuthException.LoginFailedException(e.message ?: "Unknown error")
        }
    }

    /**
     * Logout the current user
     */
    fun logout() {
        try {
            auth.signOut()
        } catch (e: Exception) {
            Log.e(TAG, "Logout failed: ${e.message}")
            throw e
        }
    }

    /**
     * Get current user profile
     */
    suspend fun getCurrentUser(): AuthResponseDto? {
        return try {
            val userId = auth.currentUser?.uid ?: return null

            val userProfile = firestore.collection(FirebaseCollections.USERS)
                .document(userId)
                .get()
                .await()
                .toObject(UserProfileDto::class.java)
                ?: return null

            AuthResponseDto(
                id = userProfile.id,
                email = userProfile.email,
                displayName = userProfile.displayName,
                photoUrl = userProfile.photoUrl,
                learningGoal = userProfile.learningGoal,
                levelEstimate = userProfile.levelEstimate,
                cefrLevel = userProfile.cefrLevel,
                streak = userProfile.streak,
                lastStudyDate = userProfile.lastStudyDate,
                createdAt = userProfile.createdAt,
                updatedAt = userProfile.updatedAt
            )
        } catch (e: Exception) {
            Log.e(TAG, "Get current user failed: ${e.message}")
            null
        }
    }

    /**
     * Get current user ID
     */
    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    /**
     * Check if user is logged in
     */
    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    /**
     * Observe authentication state changes
     */

    /**
     * Reset password for user
     */
    suspend fun resetPassword(email: String) {
        try {
            val registeredUser = firestore.collection(FirebaseCollections.USERS)
                .whereEqualTo("email", email)
                .limit(1)
                .get()
                .await()

            if (registeredUser.isEmpty) {
                throw AuthException.EmailNotRegisteredException(email)
            }

            auth.sendPasswordResetEmail(email).await()
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Log.e(TAG, "Reset password failed - invalid email: ${e.message}")
            throw AuthException.InvalidEmailException(email)
        } catch (e: AuthException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Reset password failed: ${e.message}")
            throw AuthException.ResetPasswordFailedException(e.message ?: "Unknown error")
        }
    }

    /**
     * Update user profile
     */
    suspend fun updateUserProfile(userId: String, userProfileDto: UserProfileDto): Void? {
        return try {
            firestore.collection(FirebaseCollections.USERS)
                .document(userId)
                .set(userProfileDto)
                .await()
        } catch (e: Exception) {
            Log.e(TAG, "Update user profile failed: ${e.message}")
            throw e
        }
    }
}





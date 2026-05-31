package com.minlish.domain.exception

/**
 * Base exception class for auth domain
 */
sealed class AuthException(message: String) : Exception(message) {
    data class RegistrationFailedException(val error: String) : AuthException("Registration failed: $error")
    data class LoginFailedException(val error: String) : AuthException("Login failed: $error")
    data class UserNotFoundException(val userId: String) : AuthException("User not found: $userId")
    data class InvalidEmailException(val email: String) : AuthException("Invalid email: $email")
    data class WeakPasswordException(val reason: String) : AuthException("Weak password: $reason")
    data class EmailAlreadyInUseException(val email: String) : AuthException("Email already in use: $email")
    class UserNotAuthenticatedException() : AuthException("User is not authenticated")
    data class ResetPasswordFailedException(val error: String) : AuthException("Reset password failed: $error")
}


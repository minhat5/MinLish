package com.minlish.core.utils

/**
 * Validation utilities for authentication inputs
 */
object AuthValidator {

    /**
     * Validate email format
     */
    fun isValidEmail(email: String): Boolean {
        return email.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)$"))
    }

    /**
     * Validate password strength
     * Requirements:
     * - At least 6 characters
     * - Contains at least one letter
     * - Contains at least one digit
     */
    fun isValidPassword(password: String): Boolean {
        if (password.length < 6) return false
        if (!password.any { it.isLetter() }) return false
        if (!password.any { it.isDigit() }) return false
        return true
    }

    /**
     * Validate display name
     */
    fun isValidDisplayName(displayName: String): Boolean {
        return displayName.isNotBlank() && displayName.length >= 2 && displayName.length <= 50
    }

    /**
     * Get password strength level
     */
    fun getPasswordStrength(password: String): PasswordStrength {
        return when {
            password.length < 6 -> PasswordStrength.WEAK
            password.length < 8 -> PasswordStrength.MODERATE
            password.any { it.isUpperCase() } && password.any { it.isSpecialCharacter() } -> PasswordStrength.STRONG
            else -> PasswordStrength.MODERATE
        }
    }

    private fun Char.isSpecialCharacter(): Boolean {
        return !isLetterOrDigit()
    }

    enum class PasswordStrength {
        WEAK, MODERATE, STRONG
    }
}


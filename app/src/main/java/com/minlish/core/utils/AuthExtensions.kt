package com.minlish.core.utils

import com.minlish.domain.exception.AuthException

/**
 * Extension functions for authentication
 */

/**
 * Validate email and throw exception if invalid
 */
fun String.validateEmail() {
    if (!AuthValidator.isValidEmail(this)) {
        throw AuthException.InvalidEmailException(this)
    }
}

/**
 * Validate password and throw exception if invalid
 */
fun String.validatePassword() {
    if (this.length < 6) {
        throw AuthException.WeakPasswordException("Password must be at least 6 characters")
    }
}

/**
 * Validate display name and throw exception if invalid
 */
fun String.validateDisplayName() {
    if (this.isBlank()) {
        throw IllegalArgumentException("Display name cannot be empty")
    }
    if (this.length < 2) {
        throw IllegalArgumentException("Display name must be at least 2 characters")
    }
    if (this.length > 50) {
        throw IllegalArgumentException("Display name must not exceed 50 characters")
    }
}


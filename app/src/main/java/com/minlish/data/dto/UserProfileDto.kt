package com.minlish.data.dto

data class UserProfileDto(
    val id: String = "",
    val email: String = "",
    val displayName: String = "",
    val photoUrl: String? = null,
    val learningGoal: String = "COMMUNICATION",
    val levelEstimate: String = "BEGINNER",
    val cefrLevel: String? = null,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)


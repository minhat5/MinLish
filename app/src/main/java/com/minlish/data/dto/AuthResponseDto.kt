package com.minlish.data.dto

data class AuthResponseDto(
    val id: String,
    val email: String,
    val displayName: String,
    val photoUrl: String? = null,
    val token: String? = null,
    val learningGoal: String = "COMMUNICATION",
    val levelEstimate: String = "BEGINNER",
    val cefrLevel: String? = null,
    val streak: Int = 0,
    val lastStudyDate: String? = null,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)


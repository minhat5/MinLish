package com.minlish.domain.model

data class UserProfile(
    val id: String,
    val email: String,
    val displayName: String,
    val photoUrl: String? = null,
    val learningGoal: LearningGoal = LearningGoal.COMMUNICATION,
    val levelEstimate: LevelEstimate = LevelEstimate.BEGINNER,
    val cefrLevel: CefrLevel? = null,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)


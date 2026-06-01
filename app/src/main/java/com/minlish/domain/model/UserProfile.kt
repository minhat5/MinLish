package com.minlish.domain.model

import com.minlish.core.constant.CefrLevel
import com.minlish.core.constant.LearningGoal
import com.minlish.core.constant.LevelEstimate

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


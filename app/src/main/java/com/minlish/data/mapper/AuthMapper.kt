package com.minlish.data.mapper

import com.minlish.data.dto.AuthResponseDto
import com.minlish.core.constant.CefrLevel
import com.minlish.core.constant.LearningGoal
import com.minlish.core.constant.LevelEstimate
import com.minlish.domain.model.UserProfile

fun AuthResponseDto.toDomain(): UserProfile {
    val parsedCefr = if (cefrLevel.isNullOrBlank()) null else parseEnum(cefrLevel, CefrLevel.A1)
    return UserProfile(
        id = id,
        email = email,
        displayName = displayName,
        photoUrl = photoUrl,
        learningGoal = parseEnum(learningGoal, LearningGoal.COMMUNICATION),
        levelEstimate = parseEnum(levelEstimate, LevelEstimate.BEGINNER),
        cefrLevel = parsedCefr,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun UserProfile.toAuthResponse(): AuthResponseDto {
    return AuthResponseDto(
        id = id,
        email = email,
        displayName = displayName,
        photoUrl = photoUrl,
        learningGoal = learningGoal.name,
        levelEstimate = levelEstimate.name,
        cefrLevel = cefrLevel?.name,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}


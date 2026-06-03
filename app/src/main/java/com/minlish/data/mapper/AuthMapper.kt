package com.minlish.data.mapper

import com.minlish.data.dto.AuthResponseDto
import com.minlish.core.constant.CefrLevel
import com.minlish.core.constant.LearningGoal
import com.minlish.core.constant.LevelEstimate
import com.minlish.domain.model.UserProfile

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun AuthResponseDto.toDomain(): UserProfile {
    val parsedCefr = if (cefrLevel.isNullOrBlank()) null else parseEnum(cefrLevel, CefrLevel.A1)
    
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val today = dateFormat.format(Date())
    
    val cal = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_YEAR, -1)
    val yesterday = dateFormat.format(cal.time)
    
    val validatedStreak = if (lastStudyDate == today || lastStudyDate == yesterday) {
        streak
    } else {
        0
    }

    return UserProfile(
        id = id,
        email = email,
        displayName = displayName,
        photoUrl = photoUrl,
        learningGoal = parseEnum(learningGoal, LearningGoal.COMMUNICATION),
        levelEstimate = parseEnum(levelEstimate, LevelEstimate.BEGINNER),
        cefrLevel = parsedCefr,
        streak = validatedStreak,
        lastStudyDate = lastStudyDate,
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
        streak = streak,
        lastStudyDate = lastStudyDate,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}


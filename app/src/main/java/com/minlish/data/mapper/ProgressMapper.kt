package com.minlish.data.mapper

import com.minlish.data.dto.DailyActivityDto
import com.minlish.data.dto.ProgressSnapshotDto
import com.minlish.domain.model.DailyActivity
import com.minlish.core.constant.LevelEstimate
import com.minlish.domain.model.ProgressSnapshot

fun ProgressSnapshotDto.toDomain(): ProgressSnapshot {
    return ProgressSnapshot(
        userId = userId,
        wordsLearned = wordsLearned,
        streakDays = streakDays,
        accuracyRate = accuracyRate,
        retentionRate = retentionRate,
        dailyActivities = dailyActivities.map { it.toDomain() },
        levelEstimate = parseEnum(levelEstimate, LevelEstimate.BEGINNER),
        updatedAt = updatedAt
    )
}

fun ProgressSnapshot.toDto(): ProgressSnapshotDto {
    return ProgressSnapshotDto(
        userId = userId,
        wordsLearned = wordsLearned,
        streakDays = streakDays,
        accuracyRate = accuracyRate,
        retentionRate = retentionRate,
        dailyActivities = dailyActivities.map { it.toDto() },
        levelEstimate = levelEstimate.name,
        updatedAt = updatedAt
    )
}

private fun DailyActivityDto.toDomain(): DailyActivity {
    return DailyActivity(
        date = date,
        newWordsLearned = newWordsLearned,
        reviewsCompleted = reviewsCompleted,
        totalAnswers = totalAnswers,
        correctAnswers = correctAnswers
    )
}

private fun DailyActivity.toDto(): DailyActivityDto {
    return DailyActivityDto(
        date = date,
        newWordsLearned = newWordsLearned,
        reviewsCompleted = reviewsCompleted,
        totalAnswers = totalAnswers,
        correctAnswers = correctAnswers
    )
}


package com.minlish.data.mapper

import com.minlish.data.dto.DailyActivityDto
import com.minlish.data.dto.ProgressSnapshotDto
import com.minlish.domain.model.DailyActivity
import com.minlish.core.constant.LevelEstimate
import com.minlish.domain.model.ProgressSnapshot
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun ProgressSnapshotDto.toDomain(): ProgressSnapshot {
    val domainActivities = dailyActivities.map { it.toDomain() }
    
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val today = dateFormat.format(Date())
    
    val cal = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_YEAR, -1)
    val yesterday = dateFormat.format(cal.time)

    val studiedToday = domainActivities.any { it.date == today && (it.totalAnswers > 0 || it.newWordsLearned > 0 || it.reviewsCompleted > 0) }
    val studiedYesterday = domainActivities.any { it.date == yesterday && (it.totalAnswers > 0 || it.newWordsLearned > 0 || it.reviewsCompleted > 0) }

    val validatedStreak = if (studiedToday || studiedYesterday) {
        streakDays
    } else {
        0
    }

    return ProgressSnapshot(
        userId = userId,
        wordsLearned = wordsLearned,
        streakDays = validatedStreak,
        accuracyRate = accuracyRate,
        retentionRate = retentionRate,
        dailyActivities = domainActivities,
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


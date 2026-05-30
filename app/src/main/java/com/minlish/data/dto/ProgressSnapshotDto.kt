package com.minlish.data.dto

data class ProgressSnapshotDto(
    val userId: String = "",
    val wordsLearned: Int = 0,
    val streakDays: Int = 0,
    val accuracyRate: Double = 0.0,
    val retentionRate: Double = 0.0,
    val dailyActivities: List<DailyActivityDto> = emptyList(),
    val levelEstimate: String = "BEGINNER",
    val updatedAt: Long = 0L
)


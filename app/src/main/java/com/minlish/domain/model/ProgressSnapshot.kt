package com.minlish.domain.model

data class ProgressSnapshot(
    val userId: String,
    val wordsLearned: Int = 0,
    val streakDays: Int = 0,
    val accuracyRate: Double = 0.0,
    val retentionRate: Double = 0.0,
    val dailyActivities: List<DailyActivity> = emptyList(),
    val levelEstimate: LevelEstimate = LevelEstimate.BEGINNER,
    val updatedAt: Long = 0L
)


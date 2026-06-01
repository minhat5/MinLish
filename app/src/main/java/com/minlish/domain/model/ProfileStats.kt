package com.minlish.domain.model

data class ProfileStats(
    val decksMastered: Int = 0,
    val perfectScores: Int = 0,
    val wordsLearned: Int = 0,
    val studyTimeHours: Int = 0,
    val xp: Int = 0,
    val streakDays: Int = 0,
    val accuracyRate: Double = 0.0
)

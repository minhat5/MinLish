package com.minlish.domain.model

data class DailyActivity(
    val date: String,
    val newWordsLearned: Int = 0,
    val reviewsCompleted: Int = 0,
    val totalAnswers: Int = 0,
    val correctAnswers: Int = 0
)


package com.minlish.domain.model

data class DailyPlan(
    val userId: String,
    val newWordsTarget: Int = 0,
    val reviewWordsTarget: Int = 0,
    val timezone: String = "UTC",
    val generatedAt: Long = 0L
)


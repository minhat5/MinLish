package com.minlish.data.dto

data class DailyPlanDto(
    val userId: String = "",
    val newWordsTarget: Int = 0,
    val reviewWordsTarget: Int = 0,
    val timezone: String = "UTC",
    val generatedAt: Long = 0L
)


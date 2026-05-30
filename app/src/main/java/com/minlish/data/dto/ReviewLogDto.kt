package com.minlish.data.dto

data class ReviewLogDto(
    val id: String = "",
    val userId: String = "",
    val deckId: String = "",
    val wordId: String = "",
    val rating: String = "GOOD",
    val reviewedAt: Long = 0L,
    val previousIntervalDays: Int = 0,
    val nextIntervalDays: Int = 0,
    val easeFactorAfter: Double = 2.5
)


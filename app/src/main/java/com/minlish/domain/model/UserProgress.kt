package com.minlish.domain.model

data class UserProgress(
    val vocabId: String = "",
    val deckId: String = "",
    val easeFactor: Double = 2.5,
    val repetition: Int = 0,
    val interval: Int = 0,
    val nextReviewDate: Long = 0L,
    val lastRating: String = ""
)

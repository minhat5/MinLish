package com.minlish.domain.model

data class SrsState(
    val repetition: Int = 0,
    val intervalDays: Int = 0,
    val easeFactor: Double = 2.5,
    val nextReviewAt: Long = 0L,
    val lastReviewedAt: Long? = null
)


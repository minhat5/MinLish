package com.minlish.domain.model

import com.minlish.core.constant.SrsRating

data class ReviewLog(
    val id: String,
    val userId: String,
    val deckId: String,
    val wordId: String,
    val rating: SrsRating,
    val reviewedAt: Long,
    val previousIntervalDays: Int,
    val nextIntervalDays: Int,
    val easeFactorAfter: Double
)


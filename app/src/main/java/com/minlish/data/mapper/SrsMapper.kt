package com.minlish.data.mapper

import com.minlish.data.dto.SrsStateDto
import com.minlish.domain.model.SrsState

fun SrsStateDto.toDomain(): SrsState {
    return SrsState(
        repetition = repetition,
        intervalDays = intervalDays,
        easeFactor = easeFactor,
        nextReviewAt = nextReviewAt,
        lastReviewedAt = lastReviewedAt
    )
}

fun SrsState.toDto(): SrsStateDto {
    return SrsStateDto(
        repetition = repetition,
        intervalDays = intervalDays,
        easeFactor = easeFactor,
        nextReviewAt = nextReviewAt,
        lastReviewedAt = lastReviewedAt
    )
}


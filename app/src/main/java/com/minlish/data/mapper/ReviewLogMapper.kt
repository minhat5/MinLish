package com.minlish.data.mapper

import com.minlish.data.dto.ReviewLogDto
import com.minlish.domain.model.ReviewLog
import com.minlish.core.constant.SrsRating

fun ReviewLogDto.toDomain(): ReviewLog {
    return ReviewLog(
        id = id,
        userId = userId,
        deckId = deckId,
        wordId = wordId,
        rating = parseEnum(rating, SrsRating.GOOD),
        reviewedAt = reviewedAt,
        previousIntervalDays = previousIntervalDays,
        nextIntervalDays = nextIntervalDays,
        easeFactorAfter = easeFactorAfter
    )
}

fun ReviewLog.toDto(): ReviewLogDto {
    return ReviewLogDto(
        id = id,
        userId = userId,
        deckId = deckId,
        wordId = wordId,
        rating = rating.name,
        reviewedAt = reviewedAt,
        previousIntervalDays = previousIntervalDays,
        nextIntervalDays = nextIntervalDays,
        easeFactorAfter = easeFactorAfter
    )
}


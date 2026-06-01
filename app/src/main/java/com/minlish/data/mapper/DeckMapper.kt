package com.minlish.data.mapper

import com.minlish.data.dto.DeckDto
import com.minlish.domain.model.Deck
import com.minlish.core.constant.DeckStatus

fun DeckDto.toDomain(): Deck {
    return Deck(
        id = id,
        ownerId = ownerId,
        title = title,
        description = description,
        tags = tags,
        iconKey = iconKey,
        themeColorHex = themeColorHex,
        status = parseEnum(status, DeckStatus.NEW),
        learnedWordCount = learnedWordCount,
        totalWordCount = totalWordCount,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Deck.toDto(): DeckDto {
    return DeckDto(
        id = id,
        ownerId = ownerId,
        title = title,
        description = description,
        tags = tags,
        iconKey = iconKey,
        themeColorHex = themeColorHex,
        status = status.name,
        learnedWordCount = learnedWordCount,
        totalWordCount = totalWordCount,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}


package com.minlish.data.mapper

import com.minlish.data.dto.ExampleDto
import com.minlish.data.dto.VocabularyDto
import com.minlish.domain.model.VocabularyExample
import com.minlish.domain.model.VocabularyWord

fun VocabularyDto.toDomain(): VocabularyWord {
    return VocabularyWord(
        id = id,
        deckId = deckId,
        ownerId = ownerId,
        word = word,
        pronunciation = pronunciation,
        meaning = meaning,
        description = description,
        partOfSpeech = partOfSpeech,
        examples = examples.map { it.toDomain() },
        collocations = collocations,
        relatedWords = relatedWords,
        note = note,
        audioUrl = audioUrl,
        tags = tags,
        isFavorite = isFavorite,
        srsState = srsState?.toDomain(),
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun VocabularyWord.toDto(): VocabularyDto {
    return VocabularyDto(
        id = id,
        deckId = deckId,
        ownerId = ownerId,
        word = word,
        pronunciation = pronunciation,
        meaning = meaning,
        description = description,
        partOfSpeech = partOfSpeech,
        examples = examples.map { it.toDto() },
        collocations = collocations,
        relatedWords = relatedWords,
        note = note,
        audioUrl = audioUrl,
        tags = tags,
        isFavorite = isFavorite,
        srsState = srsState?.toDto(),
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

private fun ExampleDto.toDomain(): VocabularyExample {
    return VocabularyExample(
        text = text,
        translation = translation
    )
}

private fun VocabularyExample.toDto(): ExampleDto {
    return ExampleDto(
        text = text,
        translation = translation
    )
}


package com.minlish.data.dto

data class VocabularyDto(
    val id: String = "",
    val deckId: String = "",
    val ownerId: String = "",
    val word: String = "",
    val pronunciation: String = "",
    val meaning: String = "",
    val description: String = "",
    val partOfSpeech: String = "",
    val examples: List<ExampleDto> = emptyList(),
    val collocations: List<String> = emptyList(),
    val relatedWords: List<String> = emptyList(),
    val note: String = "",
    val audioUrl: String? = null,
    val tags: List<String> = emptyList(),
    val isFavorite: Boolean = false,
    val srsState: SrsStateDto? = null,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)

data class ExampleDto(
    val text: String = "",
    val translation: String? = null
)


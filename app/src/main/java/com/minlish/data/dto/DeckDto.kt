package com.minlish.data.dto

data class DeckDto(
    val id: String = "",
    val ownerId: String = "",
    val title: String = "",
    val description: String = "",
    val tags: List<String> = emptyList(),
    val iconKey: String = "",
    val themeColorHex: String = "",
    val status: String = "NEW",
    val learnedWordCount: Int = 0,
    val totalWordCount: Int = 0,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)


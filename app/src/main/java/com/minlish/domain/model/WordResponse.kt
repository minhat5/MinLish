package com.minlish.domain.model

data class WordResponse(
    val word: String,
    val phonetic: String?,
    val phonetics: List<Phonetic>,
    val meanings: List<Meaning>
)
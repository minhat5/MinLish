package com.minlish.ui.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.minlish.data.remote.VocabularyImportItem
import com.minlish.data.remote.VocabularyImportResult
import com.minlish.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddVocabularyUiState(
    val word: String = "",
    val phonetic: String = "",
    val meaning: String = "",
    val example: String = "",
    val selectedCsvFileName: String = "",
    val importedWords: List<ImportedVocabularyWord> = emptyList(),
    val importErrors: List<String> = emptyList(),
    val isSubmitting: Boolean = false,
    val isImporting: Boolean = false,
    val errorMessage: String? = null,
    val infoMessage: String? = null,
    val createSuccess: Boolean = false
)

data class ImportedVocabularyWord(
    val word: String,
    val phonetic: String,
    val meaning: String,
    val example: String,
    val rowNumber: Int
)

class AddVocabularyViewModel(
    private val deckId: String,
    private val getCurrentUserId: () -> String?,
    private val createVocabulary: suspend (String, String, String, String, String, String) -> Unit,
    private val createVocabularies: suspend (String, String, List<VocabularyImportItem>) -> VocabularyImportResult
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddVocabularyUiState())
    val uiState: StateFlow<AddVocabularyUiState> = _uiState.asStateFlow()

    fun onWordChange(value: String) {
        _uiState.update { it.copy(word = value, errorMessage = null, infoMessage = null, createSuccess = false) }
    }

    fun onPhoneticChange(value: String) {
        _uiState.update { it.copy(phonetic = value, errorMessage = null, infoMessage = null, createSuccess = false) }
    }

    fun onMeaningChange(value: String) {
        _uiState.update { it.copy(meaning = value, errorMessage = null, infoMessage = null, createSuccess = false) }
    }

    fun onExampleChange(value: String) {
        _uiState.update { it.copy(example = value, errorMessage = null, infoMessage = null, createSuccess = false) }
    }

    fun clearCreateSuccess() {
        _uiState.update { it.copy(createSuccess = false) }
    }

    fun onCsvTextSelected(fileName: String, csvText: String) {
        val parseResult = parseCsv(csvText)
        _uiState.update {
            it.copy(
                selectedCsvFileName = fileName,
                importedWords = parseResult.words,
                importErrors = parseResult.errors,
                errorMessage = if (parseResult.words.isEmpty()) {
                    parseResult.errors.firstOrNull() ?: "No valid words found in CSV."
                } else {
                    null
                },
                infoMessage = null,
                createSuccess = false
            )
        }
    }

    fun clearCsvImport() {
        _uiState.update {
            it.copy(
                selectedCsvFileName = "",
                importedWords = emptyList(),
                importErrors = emptyList(),
                errorMessage = null,
                infoMessage = null
            )
        }
    }

    fun importCsvWords() {
        val state = _uiState.value
        if (deckId.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Deck not found.") }
            return
        }
        if (state.importedWords.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "No valid words to import.") }
            return
        }

        viewModelScope.launch {
            val ownerId = getCurrentUserId()
            if (ownerId.isNullOrBlank()) {
                _uiState.update { it.copy(errorMessage = "Please login to import words.") }
                return@launch
            }

            _uiState.update { it.copy(isImporting = true, errorMessage = null, infoMessage = null, createSuccess = false) }
            try {
                val result = createVocabularies(
                    ownerId,
                    deckId,
                    state.importedWords.map {
                        VocabularyImportItem(
                            word = it.word,
                            phonetic = it.phonetic,
                            meaning = it.meaning,
                            example = it.example
                        )
                    }
                )
                if (result.addedCount == 0) {
                    _uiState.update {
                        it.copy(
                            isImporting = false,
                            errorMessage = "All CSV words already exist in this deck.",
                            infoMessage = null
                        )
                    }
                    return@launch
                }
                _uiState.update {
                    it.copy(
                        selectedCsvFileName = "",
                        importedWords = emptyList(),
                        importErrors = emptyList(),
                        isImporting = false,
                        errorMessage = null,
                        infoMessage = if (result.skippedDuplicateCount > 0) {
                            "Imported ${result.addedCount} words. Skipped ${result.skippedDuplicateCount} duplicates."
                        } else {
                            null
                        },
                        createSuccess = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isImporting = false, errorMessage = e.message ?: "Failed to import CSV words.")
                }
            }
        }
    }

    fun addVocabulary() {
        val state = _uiState.value
        val word = state.word.trim()
        val phonetic = state.phonetic.trim()
        val meaning = state.meaning.trim()
        val example = state.example.trim()

        if (deckId.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Deck not found.") }
            return
        }
        if (word.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter an English word.") }
            return
        }
        if (meaning.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter a definition.") }
            return
        }

        viewModelScope.launch {
            val ownerId = getCurrentUserId()
            if (ownerId.isNullOrBlank()) {
                _uiState.update { it.copy(errorMessage = "Please login to add words.") }
                return@launch
            }

            _uiState.update { it.copy(isSubmitting = true, errorMessage = null, infoMessage = null, createSuccess = false) }
            try {
                createVocabulary(ownerId, deckId, word, phonetic, meaning, example)
                _uiState.update {
                    it.copy(
                        word = "",
                        phonetic = "",
                        meaning = "",
                        example = "",
                        isSubmitting = false,
                        errorMessage = null,
                        createSuccess = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isSubmitting = false, errorMessage = e.message ?: "Failed to add word.")
                }
            }
        }
    }

    private fun parseCsv(csvText: String): CsvParseResult {
        val rows = csvText
            .replace("\uFEFF", "")
            .lineSequence()
            .mapIndexed { index, line -> index + 1 to line }
            .filter { (_, line) -> line.isNotBlank() }
            .toList()

        if (rows.isEmpty()) {
            return CsvParseResult(errors = listOf("CSV file is empty."))
        }

        val firstColumns = splitCsvLine(rows.first().second)
        val hasHeader = firstColumns.any { it.equals("word", ignoreCase = true) } ||
            firstColumns.any { it.equals("meaning", ignoreCase = true) }

        val headerMap = if (hasHeader) {
            firstColumns.mapIndexed { index, column -> column.trim().lowercase() to index }.toMap()
        } else {
            emptyMap()
        }

        val wordIndex = headerMap["word"] ?: 0
        val phoneticIndex = if (hasHeader) {
            headerMap["phonetic"] ?: headerMap["pronunciation"]
        } else {
            1
        }
        val meaningIndex = headerMap["meaning"] ?: headerMap["definition"] ?: if (hasHeader) -1 else 2
        val exampleIndex = if (hasHeader) {
            headerMap["example"] ?: headerMap["example sentence"]
        } else {
            3
        }
        val dataRows = if (hasHeader) rows.drop(1) else rows
        val words = mutableListOf<ImportedVocabularyWord>()
        val errors = mutableListOf<String>()

        dataRows.forEach { (rowNumber, line) ->
            val columns = splitCsvLine(line)
            val word = columns.getOrNull(wordIndex).orEmpty().trim()
            val phonetic = phoneticIndex?.let { columns.getOrNull(it) }.orEmpty().trim()
            val meaning = columns.getOrNull(meaningIndex).orEmpty().trim()
            val example = exampleIndex?.let { columns.getOrNull(it) }.orEmpty().trim()

            if (word.isBlank() && meaning.isBlank()) {
                return@forEach
            }
            if (word.isBlank() || meaning.isBlank()) {
                errors.add("Row $rowNumber skipped: word and meaning are required.")
                return@forEach
            }

            words.add(
                ImportedVocabularyWord(
                    word = word,
                    phonetic = phonetic,
                    meaning = meaning,
                    example = example,
                    rowNumber = rowNumber
                )
            )
        }

        if (words.isEmpty() && errors.isEmpty()) {
            errors.add("No valid words found in CSV.")
        }

        return CsvParseResult(words = words, errors = errors)
    }

    private fun splitCsvLine(line: String): List<String> {
        val result = mutableListOf<String>()
        val current = StringBuilder()
        var inQuotes = false
        var index = 0

        while (index < line.length) {
            val char = line[index]
            when {
                char == '"' && inQuotes && index + 1 < line.length && line[index + 1] == '"' -> {
                    current.append('"')
                    index++
                }
                char == '"' -> {
                    inQuotes = !inQuotes
                }
                char == ',' && !inQuotes -> {
                    result.add(current.toString())
                    current.clear()
                }
                else -> current.append(char)
            }
            index++
        }

        result.add(current.toString())
        return result
    }

    private data class CsvParseResult(
        val words: List<ImportedVocabularyWord> = emptyList(),
        val errors: List<String> = emptyList()
    )
}

class AddVocabularyViewModelFactory(
    private val deckId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddVocabularyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddVocabularyViewModel(
                deckId = deckId,
                getCurrentUserId = AppContainer::getCurrentUserId,
                createVocabulary = AppContainer.firebaseDeckService::createVocabulary,
                createVocabularies = AppContainer.firebaseDeckService::createVocabularies
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

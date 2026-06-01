package com.minlish.ui.screen.vocabularyDetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.minlish.data.api.DictionaryApi
import com.minlish.data.api.model.toWordResponse
import com.minlish.domain.model.WordResponse
import kotlinx.coroutines.launch

class VocabularyViewModel(private val api: DictionaryApi) : ViewModel() {
    var uiState by mutableStateOf<WordResponse?>(null)

    fun loadWord(word: String) {
        viewModelScope.launch {
            try {
                val response = api.getWordDefinition(word)
                uiState = response.toWordResponse()
            } catch (e: Exception) {
                uiState = null
            }
        }
    }

    companion object {
        fun provideFactory(api: DictionaryApi): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                if (modelClass.isAssignableFrom(VocabularyViewModel::class.java)) {
                    return VocabularyViewModel(api) as T
                }
                throw IllegalArgumentException("Lớp ViewModel không hợp lệ")
            }
        }
    }
}
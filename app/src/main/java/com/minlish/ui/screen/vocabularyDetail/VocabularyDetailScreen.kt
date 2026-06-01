package com.minlish.ui.screen.vocabularyDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.minlish.data.api.RetrofitInstance
import com.minlish.ui.common.component.PrimaryButton
import com.minlish.ui.theme.*


val text = "Add to Favorites"

@Composable
fun VocabularyDetailScreen(
    word: String,
    viewModel: VocabularyViewModel = viewModel(
        factory = VocabularyViewModel.provideFactory(RetrofitInstance.api)
    )
) {
    LaunchedEffect(word) {
        viewModel.loadWord(word)
    }
    val data = viewModel.uiState
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = colorSurface,
    ) { paddingValues ->
        if (data == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = colorPrimary)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                WordHeroSection(
                    type = data.meanings.firstOrNull()?.partOfSpeech ?: "Unknown",
                    word = data.word,
                    transcription = data.phonetic ?: ""
                )
                DefinitionCard(
                    data.meanings.firstOrNull()?.definitions?.firstOrNull()?.definition ?: ""
                )
                val exampleList = data.meanings.flatMap { meaning ->
                    meaning.definitions.mapNotNull { definition ->
                        definition.example
                    }
                }

                ExamplesCard(
                    word = data.word,
                    examples = exampleList
                )
                PrimaryButton(colorPrimary, "Add to Favorites")
                PrimaryButton(colorPrimary, "Return to flashcard")

            }
        }
    }
}

@Preview
@Composable
fun rv() {
    VocabularyDetailScreen("ebullient")
}
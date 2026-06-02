package com.minlish.ui.screen.flashcard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.minlish.domain.model.Vocabulary
import com.minlish.ui.screen.flashcard.components.FlashcardItem
import com.minlish.ui.screen.flashcard.components.FlashcardLevelSelector
import com.minlish.ui.screen.flashcard.components.FlashcardProgressBar
import com.minlish.ui.theme.MinLishTheme
import com.minlish.ui.theme.colorPrimary

@Composable
fun FlashcardScreen(
    deckId: String,
    onBackToHome: () -> Unit = {},
    onViewDetailClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: FlashcardViewModel = viewModel(
        key = deckId,
        factory = FlashcardViewModelFactory(deckId)
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    FlashcardScreenContent(
        vocabList = uiState.vocabularies,
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        onBackToHome = onBackToHome,
        onViewDetailClick = onViewDetailClick,
        modifier = modifier
    )
}

@Composable
private fun FlashcardScreenContent(
    vocabList: List<Vocabulary>,
    isLoading: Boolean,
    errorMessage: String?,
    onBackToHome: () -> Unit,
    onViewDetailClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentIndex by remember { mutableIntStateOf(0) }

    if (currentIndex >= vocabList.size && vocabList.isNotEmpty()) {
        currentIndex = 0
    }

    val totalCount = vocabList.size
    val displayCurrent = if (totalCount > 0) currentIndex + 1 else 0

    val onNextCard = {
        if (currentIndex < vocabList.size - 1) {
            currentIndex++
        } else {
            onBackToHome()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(bottom = 64.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FlashcardProgressBar(
            current = displayCurrent,
            total = totalCount,
            onCloseClick = onBackToHome
        )

        Spacer(modifier = Modifier.height(24.dp))

        when {
            isLoading -> {
                Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            errorMessage != null -> {
                Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    Text(
                        text = errorMessage,
                        color = Color(0xFFC62828),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            vocabList.isEmpty() -> {
                Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    Text(
                        text = "No vocabulary in this deck.",
                        color = Color(0xFF757575),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            else -> {
                val currentVocab = vocabList[currentIndex]

                FlashcardItem(
                    word = currentVocab.word,
                    phonetic = currentVocab.phonetic,
                    meaning = currentVocab.meaning,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onViewDetailClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorPrimary
                    )
                ) {
                    Text(text = "View Details")
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (vocabList.isNotEmpty()) {
            FlashcardLevelSelector(
                onAgain = { onNextCard() },
                onHard = { onNextCard() },
                onGood = { onNextCard() },
                onEasy = { onNextCard() },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FlashcardScreenPreview() {
    MinLishTheme {
        FlashcardScreenContent(
            vocabList = listOf(Vocabulary("hello", "/heh-LOH/", "xin chao", "preview")),
            isLoading = false,
            errorMessage = null,
            onBackToHome = {},
            onViewDetailClick = {}
        )
    }
}

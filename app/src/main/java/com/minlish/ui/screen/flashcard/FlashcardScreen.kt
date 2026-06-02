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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

    LaunchedEffect(uiState.isCompleted, uiState.isLoading) {
        if (uiState.isCompleted && !uiState.isLoading) {
            onBackToHome()
        }
    }

    FlashcardScreenContent(
        currentVocabulary = uiState.currentVocabulary,
        currentIndex = uiState.currentIndex,
        totalCount = uiState.totalCount,
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        onBackToHome = onBackToHome,
        onViewDetailClick = onViewDetailClick,
        onAnswerSelected = viewModel::onAnswerSelected,
        modifier = modifier
    )
}

@Composable
private fun FlashcardScreenContent(
    currentVocabulary: Vocabulary?,
    currentIndex: Int,
    totalCount: Int,
    isLoading: Boolean,
    errorMessage: String?,
    onBackToHome: () -> Unit,
    onViewDetailClick: () -> Unit,
    onAnswerSelected: (quality: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(bottom = 64.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FlashcardProgressBar(
            current = currentIndex,
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

            currentVocabulary == null -> {
                Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    Text(
                        text = "No cards due now.",
                        color = Color(0xFF757575),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            else -> {
                FlashcardItem(
                    word = currentVocabulary.word,
                    phonetic = currentVocabulary.phonetic,
                    meaning = currentVocabulary.meaning,
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

        if (currentVocabulary != null) {
            FlashcardLevelSelector(
                onAgain = { onAnswerSelected(1) },
                onHard = { onAnswerSelected(3) },
                onGood = { onAnswerSelected(4) },
                onEasy = { onAnswerSelected(5) },
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
            currentVocabulary = Vocabulary("hello", "/heh-LOH/", "xin chao", "preview"),
            currentIndex = 1,
            totalCount = 2,
            isLoading = false,
            errorMessage = null,
            onBackToHome = {},
            onViewDetailClick = {},
            onAnswerSelected = {}
        )
    }
}

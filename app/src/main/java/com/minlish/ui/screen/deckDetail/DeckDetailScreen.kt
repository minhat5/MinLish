package com.minlish.ui.screen.deckDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.minlish.domain.model.Deck
import com.minlish.domain.model.Vocabulary
import com.minlish.ui.common.component.TopBar
import com.minlish.ui.common.viewmodel.DeckDetailViewModel
import com.minlish.ui.common.viewmodel.DeckDetailViewModelFactory
import com.minlish.ui.theme.colorPrimary

@Composable
fun DeckDetailScreen(
    deckId: String,
    onStartLearning: (String) -> Unit,
    onAddWord: (String) -> Unit,
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: DeckDetailViewModel = viewModel(
        key = deckId,
        factory = DeckDetailViewModelFactory(deckId)
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    DeckDetailContent(
        deck = uiState.deck,
        vocabularies = uiState.vocabularies,
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        onStartLearning = { onStartLearning(deckId) },
        onAddWord = { onAddWord(deckId) },
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@Composable
private fun DeckDetailContent(
    deck: Deck?,
    vocabularies: List<Vocabulary>,
    isLoading: Boolean,
    errorMessage: String?,
    onStartLearning: () -> Unit,
    onAddWord: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        isLoading -> {
            Box(modifier = modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        errorMessage != null -> {
            Box(modifier = modifier.fillMaxSize().padding(24.dp)) {
                Text(
                    text = errorMessage,
                    color = Color(0xFFC62828),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        deck != null -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color(0xFFF9F9FF)),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    TopBar(
                        mainTitle = "MINLISH",
                        subTitle = "Deck Detail",
                        showCloseButton = true,
                        onCloseClick = onBackClick
                    )
                }

                item {
                    DeckHeader(deck = deck)
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = onStartLearning,
                            modifier = Modifier.weight(1f).height(52.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = colorPrimary),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.size(8.dp))
                            Text("Start Learning")
                        }

                        OutlinedButton(
                            onClick = onAddWord,
                            modifier = Modifier.weight(1f).height(52.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.size(8.dp))
                            Text("Add Word")
                        }
                    }
                }

                item {
                    Text(
                        text = "Vocabulary",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF311B92),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                if (vocabularies.isEmpty()) {
                    item {
                        EmptyVocabularyCard()
                    }
                } else {
                    items(vocabularies, key = { "${it.deckId}_${it.word}" }) { vocabulary ->
                        VocabularyRow(vocabulary = vocabulary)
                    }
                }
            }
        }
    }
}

@Composable
private fun DeckHeader(deck: Deck) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = deck.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = deck.description.ifBlank { "No description" },
                fontSize = 15.sp,
                color = Color(0xFF757575)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "${deck.learnedWordCount}/${deck.totalWordCount} words learned",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF4A6572)
            )
        }
    }
}

@Composable
private fun EmptyVocabularyCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "No words in this deck yet.",
            fontSize = 15.sp,
            color = Color(0xFF757575),
            modifier = Modifier.padding(20.dp)
        )
    }
}

@Composable
private fun VocabularyRow(vocabulary: Vocabulary) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = vocabulary.word,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )
            if (vocabulary.phonetic.isNotBlank()) {
                Text(
                    text = vocabulary.phonetic,
                    fontSize = 13.sp,
                    color = Color(0xFF6C63FF),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            if (vocabulary.meaning.isNotBlank()) {
                Text(
                    text = vocabulary.meaning,
                    fontSize = 14.sp,
                    color = Color(0xFF4A6572),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

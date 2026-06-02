package com.minlish.ui.screen.deck

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.minlish.domain.model.Deck
import com.minlish.ui.screen.deck.components.DeckItem
import com.minlish.ui.theme.MinLishTheme
import com.minlish.ui.theme.colorPrimary

data class DeckData(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val iconTint: Color,
    val iconBgColor: Color,
    val learnedWords: Int,
    val totalWords: Int,
    val status: String = ""
)

@Composable
fun DeckScreen(
    onDeckSelect: (deckId: String) -> Unit = {},
    onAddDeckClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: DeckViewModel = viewModel(factory = DeckViewModelFactory())
) {
    val uiState by viewModel.uiState.collectAsState()

    DeckScreenContent(
        decks = uiState.decks.map { it.toDeckData() },
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        onDeckSelect = onDeckSelect,
        onAddDeckClick = onAddDeckClick,
        modifier = modifier
    )
}

@Composable
private fun DeckScreenContent(
    decks: List<DeckData>,
    isLoading: Boolean,
    errorMessage: String?,
    onDeckSelect: (deckId: String) -> Unit,
    onAddDeckClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        containerColor = Color(0xFFF9F9FF),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddDeckClick,
                containerColor = colorPrimary,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Deck"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Text(text = "Your Decks", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1A1A1A))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Ready to learn something new?", fontSize = 15.sp, color = Color(0xFF757575))

            Spacer(modifier = Modifier.height(16.dp))

            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(androidx.compose.ui.Alignment.Center))
                    }
                }

                errorMessage != null -> {
                    Text(
                        text = errorMessage,
                        fontSize = 15.sp,
                        color = Color(0xFFC62828)
                    )
                }

                decks.isEmpty() -> {
                    Text(
                        text = "No decks yet.",
                        fontSize = 15.sp,
                        color = Color(0xFF757575)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(bottom = 96.dp)
                    ) {
                        items(decks, key = { it.id }) { deck ->
                            DeckItem(
                                title = deck.title,
                                description = deck.description,
                                icon = deck.icon,
                                iconTint = deck.iconTint,
                                iconBackgroundColor = deck.iconBgColor,
                                learnedWords = deck.learnedWords,
                                totalWords = deck.totalWords,
                                status = deck.status,
                                onDeckClick = { onDeckSelect(deck.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun Deck.toDeckData(): DeckData {
    val icon = when (iconKey.lowercase()) {
        "chat" -> Icons.Default.Chat
        "school" -> Icons.Default.School
        "business", "briefcase" -> Icons.Default.BusinessCenter
        "travel", "flight" -> Icons.Default.Flight
        else -> Icons.Default.Star
    }

    val baseColor = themeColorHex.toColorOrNull() ?: Color(0xFF1565C0)
    return DeckData(
        id = id,
        title = title,
        description = description,
        icon = icon,
        iconTint = baseColor,
        iconBgColor = baseColor.copy(alpha = 0.12f),
        learnedWords = learnedWordCount,
        totalWords = totalWordCount,
        status = status.name
    )
}

private fun String.toColorOrNull(): Color? {
    return runCatching { Color(android.graphics.Color.parseColor(this)) }.getOrNull()
}

@Preview(showBackground = true)
@Composable
fun DeckScreenPreview() {
    MinLishTheme {
        DeckScreenContent(
            decks = listOf(
                DeckData(
                    id = "preview",
                    title = "Daily Communication",
                    description = "Common words for everyday conversations.",
                    icon = Icons.Default.Chat,
                    iconTint = Color(0xFF4F46E5),
                    iconBgColor = Color(0x1F4F46E5),
                    learnedWords = 0,
                    totalWords = 2,
                    status = "NEW"
                )
            ),
            isLoading = false,
            errorMessage = null,
            onDeckSelect = {},
            onAddDeckClick = {}
        )
    }
}

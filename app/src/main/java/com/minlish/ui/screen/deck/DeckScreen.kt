package com.minlish.ui.screen.deck

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.ui.screen.deck.components.DeckItem
import com.minlish.ui.theme.MinLishTheme

data class DeckData(
    val id: Int,
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
    onDeckSelect: (deckId: Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val decks = remember {
        listOf(
            DeckData(
                id = 1,
                title = "Core 1000",
                description = "The most frequent words",
                icon = Icons.Default.Star,
                iconTint = Color(0xFF1565C0),
                iconBgColor = Color(0xFFE3F2FD),
                learnedWords = 450,
                totalWords = 1000,
                status = "NEW"
            ),
            DeckData(
                id = 2,
                title = "Business English",
                description = "Meetings & Emails",
                icon = Icons.Default.BusinessCenter,
                iconTint = Color(0xFFF57F17),
                iconBgColor = Color(0xFFFFF8E1),
                learnedWords = 246,
                totalWords = 300,
                status = "LEARNING"
            ),
            DeckData(
                id = 3,
                title = "Travel Phrases",
                description = "Survival basics",
                icon = Icons.Default.Flight,
                iconTint = Color(0xFFC62828),
                iconBgColor = Color(0xFFFFEBEE),
                learnedWords = 150,
                totalWords = 150,
                status = "MASTERED"
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(text = "Your Decks", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1A1A1A))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Ready to learn something new?", fontSize = 15.sp, color = Color(0xFF757575))

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(decks) { deck ->
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

@Preview(showBackground = true)
@Composable
fun DeckScreenPreview() {
    MinLishTheme {
        DeckScreen()
    }
}
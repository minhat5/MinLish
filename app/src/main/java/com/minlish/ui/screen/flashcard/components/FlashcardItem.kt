package com.minlish.ui.screen.flashcard.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.minlish.ui.theme.MinLishTheme

@Composable
fun FlashcardItem(word: String, meaning: String, phonetic: String = "/həˈloʊ/", modifier: Modifier = Modifier) {
    var isFlipped by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
//            .padding(16.dp)
            .clickable { isFlipped = !isFlipped },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE9DDFF)),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),

            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isFlipped) meaning else word,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4F378A),
                    textAlign = TextAlign.Center
                )

                if (!isFlipped && phonetic.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = phonetic,
                        fontSize = 20.sp,
                        color = Color(0xFF706687)
                    )
                }
            }

            if (!isFlipped) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Tap to reveal",
                    fontSize = 12.sp,
                    color = Color(0xFFBDBDBD)
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun FlashcardItemPreview() {
    MinLishTheme {
        FlashcardItem(word = "Helloooo", meaning = "Xin chào", phonetic = "/həˈloʊ/")
    }
}


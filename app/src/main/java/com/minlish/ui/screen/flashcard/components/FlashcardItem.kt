package com.minlish.ui.screen.flashcard.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.ui.theme.MinLishTheme

@Composable
fun FlashcardItem(
    word: String,
    meaning: String,
    phonetic: String = "",
    modifier: Modifier = Modifier
) {
    var isFlipped by remember(word, meaning) { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 420),
        label = "flashcardFlip"
    )
    val isShowingBack = rotation > 90f
    val displayText = if (isShowingBack) meaning else word
    val displayFontSize = displayText.flashcardFontSize(isMeaning = isShowingBack)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .clickable { isFlipped = !isFlipped },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE9DDFF)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .graphicsLayer {
                    if (isShowingBack) {
                        rotationY = 180f
                    }
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = displayText,
                    fontSize = displayFontSize,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4F378A),
                    textAlign = TextAlign.Center,
                    maxLines = if (isShowingBack) 5 else 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = (displayFontSize.value * 1.12f).sp
                )

                if (!isShowingBack && phonetic.isNotBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = phonetic,
                        fontSize = 20.sp,
                        color = Color(0xFF706687),
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 24.sp
                    )
                }
            }

            if (!isShowingBack) {
                Text(
                    text = "Tap to reveal",
                    fontSize = 12.sp,
                    color = Color(0xFFBDBDBD)
                )
            }
        }
    }
}

private fun String.flashcardFontSize(isMeaning: Boolean): TextUnit {
    return when {
        length <= 10 -> if (isMeaning) 44.sp else 48.sp
        length <= 18 -> if (isMeaning) 38.sp else 42.sp
        length <= 28 -> if (isMeaning) 32.sp else 34.sp
        length <= 45 -> 26.sp
        else -> 22.sp
    }
}

@Preview(showBackground = true)
@Composable
fun FlashcardItemPreview() {
    MinLishTheme {
        FlashcardItem(
            word = "environment",
            meaning = "Moi truong song tu nhien va xa hoi",
            phonetic = "/in-VAI-ruhn-muhnt/"
        )
    }
}

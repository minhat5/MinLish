package com.minlish.ui.screen.flashcard.components

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.minlish.ui.theme.MinLishTheme

@Composable
fun FlashcardLevelSelector(
    onAgain: () -> Unit,
    onHard: () -> Unit,
    onGood: () -> Unit,
    onEasy: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
//            .padding(16.dp),
        color = Color(0xFFf4ebf0),
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            SelectorButton(
                label = "Again",
                time = "<1m",
                textColor = Color(0xFFfc324b),
                backgroundColor = Color(0xFFffc2c2),
                onClick = onAgain
            )

            SelectorButton(
                label = "Hard",
                time = "6m",
                textColor = Color(0xFF382c0d),
                backgroundColor = Color(0xFFffdf93),
                onClick = onHard
            )

            SelectorButton(
                label = "Good",
                time = "10m",
                textColor = Color(0xFF4f378a),
                backgroundColor = Color(0xFFe9ddff),
                onClick = onGood
            )

            SelectorButton(
                label = "Easy",
                time = "4d",
                textColor = Color(0xFFffffff),
                backgroundColor = Color(0xFF4f378a),
                onClick = onEasy
            )
        }
    }



}

@Composable
fun SelectorButton(
    label: String,
    time: String,
    textColor: Color,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .width(70.dp)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable { onClick() }
    ) {
        Text(
            text = label,
            color = textColor,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        
        Text(
            text = time,
            fontSize = 14.sp,
            color = textColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FlashcardLevelSelectorPreview() {
    MinLishTheme {
        FlashcardLevelSelector(
            onAgain = { println("Again clicked") },
            onHard = { println("Hard clicked") },
            onGood = { println("Good clicked") },
            onEasy = { println("Easy clicked") }
        )
    }
}
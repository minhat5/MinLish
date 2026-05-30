package com.minlish.ui.screen.deck.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.ui.screen.deck.DeckScreen
import com.minlish.ui.theme.MinLishTheme

@Composable
fun DeckItem(
    title: String,
    description: String,
    icon: ImageVector,
    iconTint: Color,
    iconBackgroundColor: Color,
    learnedWords: Int,
    totalWords: Int,
    status: String,
    onDeckClick: () -> Unit
) {
    val progressFactor = if (totalWords > 0) learnedWords.toFloat() / totalWords.toFloat() else 0f
    val progressPercentage = (progressFactor * 100).toInt()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onDeckClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(24.dp),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(color = iconBackgroundColor, shape = RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = iconTint,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = description, fontSize = 14.sp, color = Color(0xFF757575))
                }

                DeckBadge(status = status)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Progress",
                    fontSize = 13.sp,
                    color = Color(0xFF757575),
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "$progressPercentage% ($learnedWords/$totalWords)",
                    fontSize = 13.sp,
                    color = if (progressPercentage > 0) Color(0xFFC6A036) else Color(0xFF757575),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { progressFactor },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = Color(0xFFC6A036),
                trackColor = Color(0xFFEFEFEF),
                strokeCap = StrokeCap.Round
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun DeckItemNewPreview() {
    MinLishTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            DeckItem(
                title = "Core 1000",
                description = "The most frequent words",
                icon = Icons.Default.Star,
                iconTint = Color(0xFF1565C0),
                iconBackgroundColor = Color(0xFFE3F2FD),
                learnedWords = 450,
                totalWords = 1000,
                status = "NEW",
                onDeckClick = { }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeckItemLearningPreview() {
    MinLishTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            DeckItem(
                title = "Business English",
                description = "Meetings & Emails",
                icon = Icons.Default.BusinessCenter,
                iconTint = Color(0xFFF57F17),
                iconBackgroundColor = Color(0xFFFFF8E1),
                learnedWords = 246,
                totalWords = 300,
                status = "LEARNING",
                onDeckClick = { }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeckItemMasteredPreview() {
    MinLishTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            DeckItem(
                title = "Travel Phrases",
                description = "Survival basics",
                icon = Icons.Default.Flight,
                iconTint = Color(0xFFC62828),
                iconBackgroundColor = Color(0xFFFFEBEE),
                learnedWords = 150,
                totalWords = 150,
                status = "MASTERED",
                onDeckClick = { }
            )
        }
    }
}
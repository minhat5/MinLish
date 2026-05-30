package com.minlish.ui.screen.deck.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.ui.theme.MinLishTheme

@Composable
fun DeckBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    if (status.isEmpty()) return

    val (backgroundColor, textColor) = when (status) {
        "NEW" -> Pair(Color(0xFF651FFF), Color.White)
        "LEARNING" -> Pair(Color(0xFFE3F2FD), Color(0xFF1565C0))
        "MASTERED" -> Pair(Color(0xFFE8F5E9), Color(0xFF2E7D32))
        else -> return
    }

    Text(
        text = status,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = textColor,
        textAlign = TextAlign.Center,
        modifier = modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun DeckBadgeNewPreview() {
    MinLishTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            DeckBadge(status = "NEW")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeckBadgeLearningPreview() {
    MinLishTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            DeckBadge(status = "LEARNING")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeckBadgeMasteredPreview() {
    MinLishTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            DeckBadge(status = "MASTERED")
        }
    }
}

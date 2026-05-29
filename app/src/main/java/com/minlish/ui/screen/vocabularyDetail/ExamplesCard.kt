package com.minlish.ui.screen.vocabularyDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExamplesCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp))
            .background(colorSurface, RoundedCornerShape(12.dp))
            .padding(24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ChatBubble,
                contentDescription = "Examples",
                tint = colorPrimary,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "Examples",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorPrimary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        ExampleItem(
            text = buildHighlightedString(
                fullText = "She sounded ebullient and happy to see him again after so long.",
                targetWord = word,
                highlightColor = colorPrimary
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        ExampleItem(
            text = buildHighlightedString(
                fullText = "The award winner was in an ebullient mood at the victory party.",
                targetWord = word,
                highlightColor = colorPrimary
            )
        )
    }
}
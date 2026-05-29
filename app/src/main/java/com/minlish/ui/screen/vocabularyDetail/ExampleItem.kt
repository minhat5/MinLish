package com.minlish.ui.screen.vocabularyDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExampleItem(
    text: AnnotatedString,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(20.dp)
                .background(colorSecondaryContainer)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            color = colorOnSurface,
            lineHeight = 24.sp
        )
    }
}
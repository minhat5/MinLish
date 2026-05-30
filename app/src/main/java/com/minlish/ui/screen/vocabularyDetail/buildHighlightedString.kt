package com.minlish.ui.screen.vocabularyDetail

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight

fun buildHighlightedString(
    fullText: String,
    targetWord: String,
    highlightColor: Color
): AnnotatedString {
    return buildAnnotatedString {
        append(fullText)

        var startIndex = fullText.indexOf(string = targetWord, ignoreCase = true)

        while (startIndex >= 0) {
            val endIndex = startIndex + targetWord.length

            addStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = highlightColor
                ),
                start = startIndex,
                end = endIndex
            )

            startIndex = fullText.indexOf(
                string = targetWord,
                startIndex = endIndex,
                ignoreCase = true
            )
        }
    }
}
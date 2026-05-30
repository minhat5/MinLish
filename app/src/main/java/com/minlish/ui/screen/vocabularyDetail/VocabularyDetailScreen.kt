package com.minlish.ui.screen.vocabularyDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minlish.ui.common.component.PrimaryButton
import com.minlish.ui.theme.*


val type = "Adjective"
val word = "Ebullient"
val transcription = "ɪˈbʌliənt"
val definition = "Cheerful and full of energy; overflowing with fervor, enthusiasm, or excitement."
val text = "Add to Favorites"
@Composable
fun VocabularyDetailScreen(){
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = colorSurface,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            WordHeroSection(type, word, transcription)
            DefinitionCard(definition)
            ExamplesCard()
            PrimaryButton(colorPrimary, text)
        }
    }
}

@Preview
@Composable
fun rv(){
    VocabularyDetailScreen()
}
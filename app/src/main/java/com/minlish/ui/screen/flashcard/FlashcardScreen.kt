package com.minlish.ui.screen.flashcard


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minlish.ui.screen.flashcard.components.FlashcardProgressBar
import com.minlish.ui.screen.flashcard.components.FlashcardItem
import com.minlish.ui.screen.flashcard.components.FlashcardLevelSelector
import com.minlish.ui.theme.MinLishTheme

data class Vocabulary(
    val word: String,
    val phonetic: String,
    val meaning: String
)

@Composable
fun FlashcardScreen(
    onBackToHome: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val vocabList = remember {
        listOf(
            Vocabulary("Serendipity", "/ˌser.ənˈdɪp.ə.ti/", "may mắn"),
            Vocabulary("Ephemeral", "/ɪˈfem.ər.əl/", "Phù du, chóng tàn"),
            Vocabulary("Eloquent", "/ˈel.ə.kwənt/", "Hùng biện, lưu loát"),
            Vocabulary("Melancholy", "/ˈmel.əŋ.kɒl.i/", "U sầu, u uất"),
            Vocabulary("Solitude", "/ˈsɒl.ɪ.tʃuːd/", "Sự cô độc thanh tịnh")
        )
    }

    var currentIndex by remember { mutableIntStateOf(0) }

    val totalCount = vocabList.size
    val displayCurrent = if (totalCount > 0) currentIndex + 1 else 0

    val onNextCard = {
        if (currentIndex < vocabList.size - 1) {
            currentIndex++
        } else {
            currentIndex = 0
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(bottom = 64.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FlashcardProgressBar(
            current = displayCurrent,
            total = totalCount,
            onCloseClick = onBackToHome
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (vocabList.isNotEmpty()) {
            val currentVocab = vocabList[currentIndex]

            FlashcardItem(
                word = currentVocab.word,
                phonetic = currentVocab.phonetic,
                meaning = currentVocab.meaning,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        FlashcardLevelSelector(
            onAgain = { onNextCard() },
            onHard = { onNextCard() },
            onGood = { onNextCard() },
            onEasy = { onNextCard() },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FlashcardScreenPreview() {
    MinLishTheme {
        FlashcardScreen()
    }
}
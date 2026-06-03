package com.minlish.ui.screen.vocabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.minlish.ui.common.component.AddToDeckButton
import com.minlish.ui.common.component.FormCard
import com.minlish.ui.common.component.TextFieldVocabs
import com.minlish.ui.common.component.TopBar
import com.minlish.ui.common.viewmodel.AddVocabularyViewModel
import com.minlish.ui.common.viewmodel.AddVocabularyViewModelFactory

@Composable
fun AddVocabsScreen(
    deckId: String = "",
    onWordAdded: () -> Unit = {},
    onBackClick: () -> Unit = {},
    viewModel: AddVocabularyViewModel = viewModel(
        key = deckId,
        factory = AddVocabularyViewModelFactory(deckId)
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.createSuccess) {
        if (uiState.createSuccess) {
            viewModel.clearCreateSuccess()
            onWordAdded()
        }
    }

    Scaffold(
        containerColor = Color(0xFFF9F9FF)
    ) { paddingValues ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            TopBar(
                mainTitle = "MINLISH",
                subTitle = "Add Words",
                showCloseButton = true,
                onCloseClick = onBackClick
            )
            Spacer(modifier = Modifier.height(16.dp))
            FormCard(
                title = "Manual Entry",
                icon = Icons.Default.Edit
            ) {
                TextFieldVocabs(
                    label = "English Word",
                    placeholder = "e.g. Hello",
                    value = uiState.word,
                    onValueChange = viewModel::onWordChange
                )
                TextFieldVocabs(
                    label = "Pronunciation",
                    placeholder = "/heh-LOH/",
                    value = uiState.phonetic,
                    onValueChange = viewModel::onPhoneticChange
                )
                TextFieldVocabs(
                    label = "Definition",
                    placeholder = "A greeting used when meeting someone",
                    value = uiState.meaning,
                    onValueChange = viewModel::onMeaningChange
                )
                TextFieldVocabs(
                    label = "Example Sentence",
                    placeholder = "Hello, nice to meet you.",
                    value = uiState.example,
                    onValueChange = viewModel::onExampleChange
                )
            }
            QuickImportSection()
            uiState.errorMessage?.let { message ->
                Text(
                    text = message,
                    color = Color(0xFFC62828),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )
            }
            AddToDeckButton(
                text = if (uiState.isSubmitting) "Adding..." else "Add to Deck",
                enabled = !uiState.isSubmitting,
                onClick = viewModel::addVocabulary
            )
        }
    }
}

@Composable
fun QuickImportSection() {
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = null,
                tint = Color(0xFF311B92),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Quick Import",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                color = Color(0xFF311B92)
            )
        }
        val dashEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f), 0f)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawRoundRect(
                        color = Color.LightGray,
                        style = Stroke(width = 3f, pathEffect = dashEffect),
                        cornerRadius = CornerRadius(16.dp.toPx())
                    )
                }
                .padding(4.dp)
                .background(Color.White, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .clickable { }
                .padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.Description,
                        contentDescription = "CSV",
                        tint = Color(0xFF9575CD),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        Icons.Default.TableChart,
                        contentDescription = "Excel",
                        tint = Color(0xFFFFD54F),
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Drop your CSV or Excel here", fontWeight = FontWeight.Bold, color = Color(0xFF4A6572))
                Text("or click to browse your files", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddVocabsPreview() {
    AddVocabsScreen()
}

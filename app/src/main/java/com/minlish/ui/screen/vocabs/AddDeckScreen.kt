package com.minlish.ui.screen.vocabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Work
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.minlish.ui.common.component.AddToDeckButton
import com.minlish.ui.common.component.FormCard
import com.minlish.ui.common.component.TextFieldVocabs
import com.minlish.ui.common.component.TopBar
import com.minlish.ui.common.viewmodel.DeckViewModel
import com.minlish.ui.common.viewmodel.DeckViewModelFactory

private data class DeckIconOption(
    val key: String,
    val icon: ImageVector
)

@Composable
fun AddDeckScreen(
    modifier: Modifier = Modifier,
    onDeckCreated: () -> Unit = {},
    onBackClick: () -> Unit = {},
    viewModel: DeckViewModel = viewModel(factory = DeckViewModelFactory())
) {
    val scrollState = rememberScrollState()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.createSuccess) {
        if (uiState.createSuccess) {
            viewModel.clearCreateSuccess()
            onDeckCreated()
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = Color(0xFFF9F9FF),
        topBar = {
            TopBar(
                mainTitle = "MinLish",
                subTitle = "Create Deck",
                showCloseButton = true,
                onCloseClick = onBackClick,
                bottomPadding = 4.dp
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(top = 8.dp, bottom = 16.dp)
        ) {
            FormCard(
                title = "Create New Deck",
                icon = Icons.Default.Edit
            ) {
                TextFieldVocabs(
                    label = "Deck Title",
                    placeholder = "e.g. Business",
                    value = uiState.deckTitle,
                    onValueChange = viewModel::onDeckTitleChange
                )
                TextFieldVocabs(
                    label = "Description",
                    placeholder = "Briefly describe what's in this deck...",
                    value = uiState.deckDescription,
                    onValueChange = viewModel::onDeckDescriptionChange
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            IconSelectionSection(
                selectedIconKey = uiState.selectedIconKey,
                onIconSelected = viewModel::onDeckIconSelected
            )
            ThemeColorSection(
                selectedColorHex = uiState.selectedThemeColorHex,
                onColorSelected = viewModel::onThemeColorSelected
            )
            uiState.errorMessage?.let { message ->
                Text(
                    text = message,
                    color = Color(0xFFC62828),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
            AddToDeckButton(
                text = if (uiState.isSubmitting) "Creating..." else "Create Deck",
                enabled = !uiState.isSubmitting,
                onClick = viewModel::createDeck
            )
        }
    }
}

@Composable
fun IconSelectionSection(
    selectedIconKey: String,
    onIconSelected: (String) -> Unit
) {
    val icons = listOf(
        DeckIconOption("book", Icons.Default.Book),
        DeckIconOption("home", Icons.Default.Home),
        DeckIconOption("flight", Icons.Default.Flight),
        DeckIconOption("science", Icons.Default.Science),
        DeckIconOption("restaurant", Icons.Default.Restaurant),
        DeckIconOption("camera", Icons.Default.Camera),
        DeckIconOption("work", Icons.Default.Work),
        DeckIconOption("public", Icons.Default.Public)
    )

    Column(
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        Text(
            text = "Choose an icon",
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            color = Color(0xFF311B92)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp), spotColor = Color.LightGray)
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column {
                icons.chunked(4).forEach { rowIcons ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        rowIcons.forEach { option ->
                            val isSelected = option.key == selectedIconKey

                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) Color(0xFF6C63FF) else Color.Transparent)
                                    .clickable { onIconSelected(option.key) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = option.icon,
                                    contentDescription = null,
                                    tint = if (isSelected) Color.White else Color(0xFF4A6572)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ThemeColorSection(
    selectedColorHex: String,
    onColorSelected: (String) -> Unit
) {
    val colors = listOf(
        "#E8E0F0" to Color(0xFFE8E0F0),
        "#F3E5F5" to Color(0xFFF3E5F5),
        "#FFEFEA" to Color(0xFFFFEFEA),
        "#F0E68C" to Color(0xFFF0E68C),
        "#E0E0E0" to Color(0xFFE0E0E0)
    )

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp)
    ) {
        Text(
            text = "Theme Color",
            fontWeight = FontWeight.Bold,
            color = Color(0xFF311B92),
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp), spotColor = Color.LightGray)
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                colors.forEach { (hex, color) ->
                    val isSelected = hex == selectedColorHex
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (isSelected) 2.dp else 0.dp,
                                color = if (isSelected) Color(0xFF311B92) else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable { onColorSelected(hex) }
                    )
                }
            }
        }
    }
}

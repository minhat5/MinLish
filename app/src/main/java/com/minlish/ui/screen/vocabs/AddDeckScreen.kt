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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.ui.common.component.AddToDeckButton
import com.minlish.ui.common.component.FormCard
import com.minlish.ui.common.component.TextFieldVocabs

@Composable
fun AddDeckScreen(
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(vertical = 16.dp)
    ) {
        FormCard(
            title = "Create New Deck",
            icon = Icons.Default.Edit
        ) {
            TextFieldVocabs(label = "Deck Title", placeholder = "e.g. Business")
            TextFieldVocabs(label = "Description", placeholder = "Briefly describe what's in this deck...")
        }
        Spacer(modifier = Modifier.height(32.dp))
        IconSelectionSection()
        ThemeColorSection()
        AddToDeckButton()
    }
}

@Composable
fun IconSelectionSection() {
    val icons = listOf(
        Icons.Default.Book,
        Icons.Default.Home,
        Icons.Default.Flight,
        Icons.Default.Science,
        Icons.Default.Restaurant,
        Icons.Default.Camera,
        Icons.Default.Work,
        Icons.Default.Public
    )
    var selectedIndex by remember { mutableIntStateOf(0) }

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
                icons.chunked(4).forEachIndexed { rowIndex, rowIcons ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        rowIcons.forEachIndexed { colIndex, icon ->
                            val actualIndex = rowIndex * 4 + colIndex
                            val isSelected = actualIndex == selectedIndex

                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) Color(0xFF6C63FF) else Color.Transparent)
                                    .clickable { selectedIndex = actualIndex },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = icon,
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
fun ThemeColorSection() {
    val colors = listOf(
        Color(0xFFE8E0F0),
        Color(0xFFF3E5F5),
        Color(0xFFFFEFEA),
        Color(0xFFF0E68C),
        Color(0xFFE0E0E0)
    )
    var selectedColorIndex by remember { mutableIntStateOf(0) }

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
                colors.forEachIndexed { index, color ->
                    val isSelected = index == selectedColorIndex
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
                            .clickable { selectedColorIndex = index }
                    )
                }
            }
        }
    }
}

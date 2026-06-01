package com.minlish.ui.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.ui.common.component.AuthHeader
import com.minlish.ui.common.component.ButtonAuth

@Composable
fun SelectCefrLevelScreen(
    onCefrLevelSelected: (String) -> Unit = {},
    onSkip: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val selectedLevel = remember { mutableIntStateOf(-1) }
    Surface(
        color = Color(0xFFF7EEFE),
        modifier = Modifier
            .padding(24.dp)
            .shadow(6.dp, RoundedCornerShape(16.dp))
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF22005D)
                    )
                }
                Text(
                    text = "Skip",
                    color = Color(0xFF22005D),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onSkip() }
                )
            }
            
            AuthHeader("Select CEFR Level", "Choose your current level (Optional)")
            SelectCefrLevel(onCefrLevelSelected = { level ->
                selectedLevel.intValue = level
            })
            ButtonAuth(
                onClick = {
                    if (selectedLevel.intValue >= 0) {
                        val levels = listOf("A1", "A2", "B1", "B2", "C1", "C2")
                        onCefrLevelSelected(levels[selectedLevel.intValue])
                    }
                },
                text = "Continue",
                enabled = selectedLevel.intValue >= 0
            )
        }
    }
}

@Composable
fun SelectCefrLevel(onCefrLevelSelected: (Int) -> Unit) {
    val selectedLevel = remember { mutableIntStateOf(-1) }
    val levels = listOf("A1", "A2", "B1", "B2", "C1", "C2")

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth(0.85f)
    ) {
        levels.forEachIndexed { index, levelName ->
            CefrLevelItem(
                name = levelName,
                isSelected = selectedLevel.intValue == index,
                onLevelSelected = {
                    selectedLevel.intValue = index
                    onCefrLevelSelected(index)
                }
            )
        }
    }
}

@Composable
fun CefrLevelItem(
    name: String,
    isSelected: Boolean,
    onLevelSelected: () -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFF22005D) else Color.White
    val textColor = if (isSelected) Color.White else Color(0xFF22005D)
    val borderColor = Color(0xFF22005D)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onLevelSelected() }
            .padding(20.dp)
    ) {
        Text(
            text = name,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}


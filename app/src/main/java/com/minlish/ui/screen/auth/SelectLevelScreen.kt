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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.ui.common.component.AuthHeader
import com.minlish.ui.common.component.ButtonAuth
import com.minlish.ui.theme.MinLishTheme

@Composable
fun SelectLevelScreen(
    onLevelSelected: (String) -> Unit = {},
    onSkip: () -> Unit = {}
) {
    val selectedLevel = remember { mutableStateOf<String?>(null) }
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
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Skip",
                    color = Color(0xFF22005D),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onSkip() }
                )
            }
            
            AuthHeader("Select Your Level", "Select your level to start learning")
            SelectLevel(
                onLevelSelected = { level ->
                    selectedLevel.value = level
                }
            )
            ButtonAuth(
                onClick = {
                    selectedLevel.value?.let { onLevelSelected(it) }
                },
                text = "Continue",
                enabled = selectedLevel.value != null
            )
        }
    }
}

@Composable
fun SelectLevel(onLevelSelected: (String) -> Unit) {
    val selectedLevel = remember { mutableIntStateOf(-1) }
    val listLevel = listOf("Beginner", "Intermediate", "Advanced")

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth(0.85f)
    ) {
        listLevel.forEachIndexed { index, levelName ->
            Level(
                name = levelName,
                isSelected = selectedLevel.intValue == index,
                onLevelSelected = {
                    selectedLevel.intValue = index
                    onLevelSelected(levelName)
                }
            )
        }
    }
}

@Composable
fun Level(
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

@Preview(showBackground = true)
@Composable
fun SelectLevelPreview() {
    MinLishTheme {
        SelectLevelScreen()
    }
}